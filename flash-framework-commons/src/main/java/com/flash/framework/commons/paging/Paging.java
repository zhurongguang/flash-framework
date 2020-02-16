package com.flash.framework.commons.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页实体
 *
 * @author zhurg
 * @date 2019/3/21 - 下午5:48
 */
@ToString
public class Paging<T> implements Serializable {

    private static final long serialVersionUID = -4444805400451519629L;

    /**
     * 查询数据列表
     */
    @Getter
    @Setter
    private List<T> records = Collections.emptyList();
    /**
     * 总数
     */
    @Getter
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    @Getter
    private long pageSize = 10;
    /**
     * 当前页
     */
    @Getter
    private long pageNo = 1;

    public void setPageSize(long pageSize) {
        if (pageSize <= 0) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }

    public void setTotal(long total) {
        if (total < 0) {
            total = 0;
        }
        this.total = total;
    }

    public void setPageNo(long pageNo) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        this.pageNo = pageNo;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.pageNo > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.pageNo < this.getPages();
    }

    /**
     * 当前分页总页数
     */
    public long getPages() {
        if (this.pageSize == 0) {
            return 0L;
        }
        long pages = getTotal() / this.pageSize;
        if (getTotal() % this.pageSize != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return null == records || records.isEmpty();
    }
}