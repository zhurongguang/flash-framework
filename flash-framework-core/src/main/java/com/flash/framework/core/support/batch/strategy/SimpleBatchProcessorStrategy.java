package com.flash.framework.core.support.batch.strategy;

import com.flash.framework.core.support.batch.reader.ItemReader;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * 每页一处理，分页查询顺序执行，可以对一页数据一起处理，也可以一条一条处理，无并发
 *
 * @author zhurg
 * @date 2019/11/18 - 下午3:08
 */
@Data
public class SimpleBatchProcessorStrategy implements BatchProcessorStrategy<ItemReader> {

    private boolean processAll;

    @Override
    public void process(ItemReader itemReader, Consumer consumer) throws Exception {
        while (true) {
            List datas = itemReader.read();
            if (CollectionUtils.isEmpty(datas)) {
                break;
            }
            if (processAll) {
                consumer.accept(datas);
            } else {
                datas.forEach(consumer::accept);
            }
        }
    }
}
