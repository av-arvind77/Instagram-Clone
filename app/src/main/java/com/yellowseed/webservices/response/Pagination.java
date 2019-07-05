package com.yellowseed.webservices.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 27/6/18.
 */

public class Pagination {

    @SerializedName("page_no")
    @Expose
    private Integer pageNo;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("max_page_size")
    @Expose
    private Integer maxPageSize;
    @SerializedName("total_records")
    @Expose
    private Integer totalRecords;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(Integer maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }
}
