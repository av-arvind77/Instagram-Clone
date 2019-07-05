package com.yellowseed.webservices.requests.posts;

public class PostRequest {
    String user_id;
    int page;

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
