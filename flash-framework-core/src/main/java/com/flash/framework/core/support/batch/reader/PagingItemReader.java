package com.flash.framework.core.support.batch.reader;

import com.flash.framework.commons.paging.Paging;
import lombok.Data;

import java.util.List;

/**
 * 分页数据读取接口
 *
 * @author zhurg
 * @date 2019/11/14 - 下午5:20
 */
@Data
public class PagingItemReader<T, P> implements ItemReader<T> {

    private final PagingHelper<T, P> helper;

    private PagingItemReader(PagingHelper<T, P> helper) {
        this.helper = helper;
    }


    public static PagingHelper builder() {
        return new PagingHelper();
    }


    @Override
    public List<T> read() throws Exception {
        if (this.helper.getPages() > 0 && this.helper.getPageNo() > this.helper.getPages()) {
            return null;
        }
        Paging<T> paging = this.helper.getPagingHandler().doRead(this.helper.getPageNo(), this.helper.getPageSize(), this.helper.getContext());
        this.helper.setPages(paging.getPages());
        if (paging.isEmpty()) {
            return null;
        }
        this.helper.setPageNo(this.helper.getPageNo() + 1);
        return paging.getRecords();
    }

    @FunctionalInterface
    public interface PagingHandler<T, P> {

        /**
         * 分页请求查询接口
         *
         * @param pageNo
         * @param pageSize
         * @param readerContext
         * @return
         */
        Paging<T> doRead(long pageNo, long pageSize, P readerContext);
    }

    @Data
    public static class PagingHelper<T, P> {

        private long pageNo = 1;

        private long pageSize = 10;

        private long pages;

        private P context;

        private PagingHandler<T, P> pagingHandler;

        public PagingHelper context(P context) {
            this.context = context;
            return this;
        }

        public PagingHelper pageNo(long pageNo) {
            if (pageNo <= 0) {
                this.pageNo = 1;
            } else {
                this.pageNo = pageNo;
            }
            return this;
        }

        public PagingHelper pageSize(long pageSize) {
            if (this.pageSize <= 0) {
                this.pageSize = 10;
            } else {
                this.pageSize = pageSize;
            }
            return this;
        }

        public PagingHelper handler(PagingHandler<T, P> pagingHandler) {
            this.pagingHandler = pagingHandler;
            return this;
        }

        public PagingItemReader build() {
            return new PagingItemReader(this);
        }
    }
}