package com.yellowseed.webservices.response;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 13/7/18.
 */

public class BlockUserListResponse {


    /**
     * responseCode : 200
     * responseMessage : List fetched successfully.
     * block : [{"name":"Sunil Kumar Verma","email":"sunil1988_gold@ymail.com","user_name":"73d24afacebook","mobile":"","image":null}]
     * pagination : {"page_no":1,"per_page":10,"max_page_size":1,"total_records":1}
     */

    private int responseCode;
    private String responseMessage;
    private PaginationBean pagination;
    private List<BlockBean> users;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<BlockBean> getUsers() {
        return users;
    }

    public void setUsers(List<BlockBean> users) {
        this.users = users;
    }

    public static class PaginationBean {
        /**
         * page_no : 1
         * per_page : 10
         * max_page_size : 1
         * total_records : 1
         */

        private int page_no;
        private int per_page;
        private int max_page_size;
        private int total_records;

        public int getPage_no() {
            return page_no;
        }

        public void setPage_no(int page_no) {
            this.page_no = page_no;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getMax_page_size() {
            return max_page_size;
        }

        public void setMax_page_size(int max_page_size) {
            this.max_page_size = max_page_size;
        }

        public int getTotal_records() {
            return total_records;
        }

        public void setTotal_records(int total_records) {
            this.total_records = total_records;
        }
    }

    public static class BlockBean {
        /**
         * name : Sunil Kumar Verma
         * email : sunil1988_gold@ymail.com
         * user_name : 73d24afacebook
         * mobile :
         * image : null
         */
        private String id;
        private String name;
        private String email;
        private String user_name;
        private String mobile;
        private String image;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
