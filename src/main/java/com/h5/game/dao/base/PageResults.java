package com.h5.game.dao.base;

import java.util.List;

/**
 * 分页封装类 用于做分页查询的基础类，封装了一些分页的相关属性
 * Created by 黄春怡 on 2017/4/1.
 */
public class PageResults<T>{

    //下一页
    private Integer pageNo;

    //当前页
    private Integer currentPage;

    //每一页的个数
    private Integer pageSize;

    //总条数
    private Integer totalCount;

    //总页数
    private Integer pageCount;

    // 记录
    private List<T> Data;


    public Integer getPageNo() {
        if(pageNo <= 0){
            return 1;
        }else {
            return pageNo > pageCount ? pageCount : pageNo;
        }

    }
    public void resetPageNo() {
        pageNo = currentPage + 1;
        pageCount = totalCount % pageSize == 0 ? totalCount / pageSize
                : totalCount / pageSize + 1;
    }

    public void setPageNo(Integer pageNo) {

        this.pageNo = pageNo;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getData() {
        return Data;
    }

    public void setData(List<T> data) {
        Data = data;
    }


}
