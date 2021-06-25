package com.flash.framework.core.support.sequence.jdbc;

import com.flash.framework.core.exception.sequence.SequenceException;
import com.flash.framework.core.support.sequence.Sequence;
import com.flash.framework.core.support.sequence.SequenceRange;
import com.flash.framework.core.support.sequence.generator.IDRule;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhurg
 * @date 2021/6/25 - 上午10:46
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "flash.framework.sequence.enable", havingValue = "true")
public class JdbcSequence implements Sequence {

    private static final String SELECT_SQL = "SELECT `value`,`step` FROM `shard_sequence` WHERE name = ?";
    private static final String UPDATE_SQL = "UPDATE `shard_sequence` SET `value`=?, `updated_at`= now() WHERE `name`=? AND `value`=?";
    private static final String INSERT_SQL = "INSERT INTO `shard_sequence` (`name`, `value`, `step`, `updated_at`) VALUES (?, ?, ?, now())";
    private static final String TABLE = "CREATE TABLE IF NOT EXISTS `shard_sequence` (\n" +
            "  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '序列名称',\n" +
            "  `value` bigint(20) NOT NULL COMMENT '当前已使用的id',\n" +
            "  `step` bigint(20) NOT NULL COMMENT '步长',\n" +
            "  `updated_at` datetime NOT NULL COMMENT '上次更新时间',\n" +
            "  PRIMARY KEY (`name`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;";
    private final Lock lock = new ReentrantLock();
    private Map<String, SequenceRange> ranges = Maps.newConcurrentMap();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${flash.framework.sequence.retryTimes:1}")
    private int retryTimes;

    @Value("${flash.framework.sequence.defaultStep:1000}")
    private Long defaultStep;

    @PostConstruct
    public void init() {
        try {
            jdbcTemplate.update(TABLE);
        } catch (Exception e) {
            log.error("[Flash Framework] sequence table create failed,cause:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    @Transactional(
            propagation = Propagation.NOT_SUPPORTED
    )
    public SequenceRange nextRange(String name, IDRule idRule) throws SequenceException {
        int i = 0;
        while (i <= retryTimes) {
            Map<String, Long> currentValue = queryCurrentValue(name, idRule);
            Long next = currentValue.get("value") + currentValue.get("step");
            int row = jdbcTemplate.update(UPDATE_SQL, new Object[]{next, name, currentValue.get("value")});
            if (row != 0) {
                return new SequenceRange(currentValue.get("value") + 1, next);
            }

            i++;

            try {
                TimeUnit.MILLISECONDS.sleep(1L);
            } catch (InterruptedException e) {
            }
        }
        throw new SequenceException("[Flash Framework] sequence get next retry too many times, retryTimes :" + retryTimes);
    }

    @Override
    public long nextValue(String name, IDRule idRule) throws SequenceException {
        SequenceRange currentRange = ranges.computeIfAbsent(name, (n) -> nextRange(n, idRule));
        long value = currentRange.getAndIncrement();
        if (value == -1L) {
            lock.lock();
            currentRange = ranges.get(name);
            try {
                do {
                    if (currentRange.isOver()) {
                        currentRange = this.nextRange(name, idRule);
                        ranges.put(name, currentRange);
                    }
                    value = currentRange.getAndIncrement();
                } while (value == -1L);
            } finally {
                lock.unlock();
            }
        }

        if (value < 0L) {
            throw new SequenceException("[Flash Framework] sequence value overflow, value = " + value);
        } else {
            return value;
        }
    }

    /**
     * 获取当前序列范围
     *
     * @param name
     * @return
     */
    protected Map<String, Long> queryCurrentValue(String name, IDRule idRule) {
        Map<String, Long> data = queryCurrentValue(name);
        if (MapUtils.isEmpty(data)) {
            synchronized (this) {
                try {
                    long step = defaultStep;
                    long value = 0L;
                    if (Objects.nonNull(idRule)) {
                        step = idRule.step();
                        value = idRule.initValue();
                    }

                    jdbcTemplate.update(INSERT_SQL, new Object[]{name, value, step});
                    data = ImmutableMap.of(
                            "value", 0L,
                            "step", defaultStep
                    );
                } catch (DuplicateKeyException e) {
                    data = queryCurrentValue(name);
                } catch (Exception e) {
                    throw new SequenceException("[Flash Framework] failed to init sequence:" + name, e);
                }
            }
        }

        if (data.get("value") < 0L) {
            throw new SequenceException("[Flash Framework] sequence value for " + name + " less than zero");
        }

        if (data.get("value") > Long.MAX_VALUE) {
            throw new SequenceException("[Flash Framework] sequence value for " + name + " overflow");
        }

        return data;
    }

    private Map<String, Long> queryCurrentValue(String name) {
        return jdbcTemplate.query(SELECT_SQL, new Object[]{name}, (rs) -> {
            if (rs.next()) {
                Map<String, Long> result = Maps.newHashMap();
                result.put("value", rs.getLong("value"));
                result.put("step", rs.getLong("step"));
                return result;
            }
            return null;
        });
    }
}