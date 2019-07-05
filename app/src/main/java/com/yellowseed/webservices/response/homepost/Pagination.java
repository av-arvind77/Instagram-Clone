
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pagination implements Serializable
{

    @SerializedName("page_no")
    @Expose
    private String pageNo;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("max_page_size")
    @Expose
    private Integer maxPageSize;
    @SerializedName("total_records")
    @Expose
    private Integer totalRecords;




    public Pagination() {
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
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

    public int describeContents() {
        return  0;
    }

}
