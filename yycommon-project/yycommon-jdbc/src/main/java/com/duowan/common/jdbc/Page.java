package com.duowan.common.jdbc;

import com.duowan.common.utils.AssertUtil;

import java.util.List;

/**
 * 分页数据
 *
 * @author Arvin
 */
public class Page<T> {

    /**
     * 页码，从1开始
     */
    private int pageNo;
    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总数
     */
    private int count;

    /**
     * 数据列表
     */
    private List<T> dataList;

    public Page(int pageNo, int pageSize) {

        AssertUtil.assertPageParam(pageNo, pageSize);

        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
