package com.yellowseed.model.resModel;

import java.io.Serializable;
import java.util.List;

public class RequestListResonse implements Serializable{

    /**
     * responseCode : 200
     * responseMessage : List fetched successfully.
     * users : [{"id":"acc0e0b7-89d0-484b-967d-3c6651133708","email":"dsgsadg@hdsafsd.com","name":"shasha grey","image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531467277/pcqjeq7qsd7bdkdhdpfv.jpg","mutual_follower":0}]
     * pagination : {"page_no":1,"per_page":10,"max_page_size":1,"total_records":1}
     */

    private int responseCode;
    private String responseMessage;
    private PaginationBean pagination;
    private List<UsersBean> users;

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

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class PaginationBean implements Serializable{
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

    public static class UsersBean implements Serializable{
        /**
         * id : acc0e0b7-89d0-484b-967d-3c6651133708
         * email : dsgsadg@hdsafsd.com
         * name : shasha grey
         * image : http://res.cloudinary.com/di8lsuqdb/image/upload/v1531467277/pcqjeq7qsd7bdkdhdpfv.jpg
         * mutual_follower : 0
         */

        private String id;
        private String email;
        private String name;
        private String image;
        private int mutual_follower;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getMutual_follower() {
            return mutual_follower;
        }

        public void setMutual_follower(int mutual_follower) {
            this.mutual_follower = mutual_follower;
        }
    }
}
