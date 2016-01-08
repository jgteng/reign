package com.reign.domain;

/**
 * Created by ji on 15-12-11.
 */
public class CommonBean {

    private Integer page;//分页开始

    private Integer rows;//分页结束

    private String sort;//排序方式 asc，desc

    private String sidx;//排序字段

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }
}
