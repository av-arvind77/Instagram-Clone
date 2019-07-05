package com.yellowseed.model.resModel;

import com.yellowseed.webservices.response.User;

import java.io.Serializable;
import java.util.List;

public class StoryListModel implements Serializable {

    /**
     * responseCode : 200
     * responseMessage : Stories fetched successfully.
     * stories : [{"user":{"id":"dd3239c2-5a8b-4efa-b9dd-a163d95c689e","email":"akshgaur001@gmail.com","name":"Sonu Gaur","user_name":""},"story":{"story_id":"98936d0c-a75c-4d6c-a931-e16fd9307dd0","story_image":{"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530162224/mfzzjwzbdgcpvqumujgy.jpg","width":"","height":"","file_type":""}}},{"user":{"id":"6713a06f-83f2-4f50-a7bf-a66634b732a8","email":"sunil1988_gold@ymail.com","name":"Sunil Kumar Verma","user_name":"fff4d6facebook"},"story":{"story_id":"7daa71df-57d6-4105-8850-39f6d739a4a3","story_image":{"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530160855/iqo0ltai6sfihzcsi6vu.jpg","width":"3000.0","height":"2002.0","file_type":"image"}}}]
     * pagination : {"page_no":1,"per_page":10,"max_page_size":1,"total_records":2}
     */

    private int responseCode;
    private String responseMessage;
    private PaginationBean pagination;
    private List<StoriesBean> stories;
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

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

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class PaginationBean {
        /**
         * page_no : 1
         * per_page : 10
         * max_page_size : 1
         * total_records : 2
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

    public static class StoriesBean implements Serializable{
        /**
         * user : {"id":"dd3239c2-5a8b-4efa-b9dd-a163d95c689e","email":"akshgaur001@gmail.com","name":"Sonu Gaur","user_name":""}
         * story : {"story_id":"98936d0c-a75c-4d6c-a931-e16fd9307dd0","story_image":{"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530162224/mfzzjwzbdgcpvqumujgy.jpg","width":"","height":"","file_type":""}}
         */

        private UserBean user;
        private StoryBean story;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public StoryBean getStory() {
            return story;
        }

        public void setStory(StoryBean story) {
            this.story = story;
        }

        public static class UserBean implements Serializable{
            /**
             * id : dd3239c2-5a8b-4efa-b9dd-a163d95c689e
             * email : akshgaur001@gmail.com
             * name : Sonu Gaur
             * user_name :
             */

            private String id;
            private String email;
            private String name;
            private String user_name;

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

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }
        }

        public static class StoryBean implements Serializable{
            /**
             * story_id : 98936d0c-a75c-4d6c-a931-e16fd9307dd0
             * story_image : {"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530162224/mfzzjwzbdgcpvqumujgy.jpg","width":"","height":"","file_type":""}
             */

            private String story_id;
            private StoryImageBean story_image;

            public String getStory_id() {
                return story_id;
            }

            public void setStory_id(String story_id) {
                this.story_id = story_id;
            }

            public StoryImageBean getStory_image() {
                return story_image;
            }

            public void setStory_image(StoryImageBean story_image) {
                this.story_image = story_image;
            }

            public static class StoryImageBean implements Serializable{
                /**
                 * img : http://res.cloudinary.com/di8lsuqdb/image/upload/v1530162224/mfzzjwzbdgcpvqumujgy.jpg
                 * width :
                 * height :
                 * file_type :
                 */

                private String img;
                private String width;
                private String height;
                private String file_type;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public String getFile_type() {
                    return file_type;
                }

                public void setFile_type(String file_type) {
                    this.file_type = file_type;
                }
            }
        }
    }
}
