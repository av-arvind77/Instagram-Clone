       package com.yellowseed.webservices.response.post;

        import android.os.Parcel;
        import android.os.Parcelable;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Pagination implements Parcelable
{

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
    public final static Parcelable.Creator<Pagination> CREATOR = new Creator<Pagination>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Pagination createFromParcel(Parcel in) {
            return new Pagination(in);
        }

        public Pagination[] newArray(int size) {
            return (new Pagination[size]);
        }

    }
            ;

    protected Pagination(Parcel in) {
        this.pageNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.perPage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxPageSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalRecords = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Pagination() {
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pageNo);
        dest.writeValue(perPage);
        dest.writeValue(maxPageSize);
        dest.writeValue(totalRecords);
    }

    public int describeContents() {
        return 0;
    }

}