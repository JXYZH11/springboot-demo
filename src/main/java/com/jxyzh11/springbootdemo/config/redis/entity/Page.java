package com.jxyzh11.springbootdemo.config.redis.entity;

import java.util.List;

public class Page<T> {
    public static final int DEFAULT_PAGE_SIZE = 10;
    private int pageSize;
    private int pageNo;
    private int totalPage;
    private int totalCount;
    private List<T> result;

    public Page() {
        this(1, 10);
    }

    public Page(int pageNo, int pageSize) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getResult() {
        return this.result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public String toString() {
        return String.format("Page [pageSize=%s, pageNo=%s, totalPage=%s, totalCount=%s, result=%s]", this.pageSize, this.pageNo, this.totalPage, this.totalCount, this.result);
    }
}
