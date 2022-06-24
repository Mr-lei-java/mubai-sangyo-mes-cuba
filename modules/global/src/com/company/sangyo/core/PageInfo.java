package com.company.sangyo.core;

import java.io.Serializable;
import java.util.List;

public class PageInfo<T> implements Serializable {

    //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;

    //总数
    private int size;

    //总页数
//    private int pages;

    private List<T> list;

    public PageInfo(List<T> list) {
        this.list = list;
    }

    public PageInfo(int pageNum, int pageSize, int size, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.size = size;
        this.list = list;
    }
}
