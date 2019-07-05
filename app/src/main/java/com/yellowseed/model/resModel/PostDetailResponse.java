package com.yellowseed.model.resModel;

import com.yellowseed.model.reqModel.ViewerModel;
import com.yellowseed.webservices.response.homepost.Post;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 6/7/18.
 */

public class PostDetailResponse {


    /**
     * responseCode : 200
     * responseMessage : Post fetched successfully.
     * posts : [{"post":{"id":"76323ba1-83dd-443c-8eb0-2428ecd4343d","title":"","description":"","check_in":"","latitude":0,"longitude":0,"share_type":"public","status":true,"user_id":"2cdf7da8-9d04-4bb0-9a28-decb5ab46974","created_at":"2018-06-30T10:31:06.027Z","updated_at":"2018-06-30T10:31:06.027Z"},"post_images_attributes":[{"id":"fb94cc49-e637-4ba6-9d34-79e50ffc3310","file":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530354668/drxlnqnnocixuz6bk2at.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1530354668/drxlnqnnocixuz6bk2at.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1530354668/drxlnqnnocixuz6bk2at.jpg"}},"width":"3024.0","height":"4032.0","file_type":"image","imageable_id":"76323ba1-83dd-443c-8eb0-2428ecd4343d","imageable_type":"Post","created_at":"2018-06-30T10:31:06.028Z","updated_at":"2018-06-30T10:31:06.028Z"}],"user":{"id":"2cdf7da8-9d04-4bb0-9a28-decb5ab46974","email":"aa@ss.sss","name":"sss","user_name":"","image":null},"hash_tags":[],"tag_friends":[],"likes_count":1,"current_user_like":false,"isSaved":false,"comments":0,"comment_arr":[]}]
     */

    private int responseCode;
    private String responseMessage;
    private ViewerModel viewer;
    private List<Post> posts;

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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public static class PostsBean {
        /**
         * post : {"id":"76323ba1-83dd-443c-8eb0-2428ecd4343d","title":"","description":"","check_in":"","latitude":0,"longitude":0,"share_type":"public","status":true,"user_id":"2cdf7da8-9d04-4bb0-9a28-decb5ab46974","created_at":"2018-06-30T10:31:06.027Z","updated_at":"2018-06-30T10:31:06.027Z"}
         * post_images_attributes : [{"id":"fb94cc49-e637-4ba6-9d34-79e50ffc3310","file":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530354668/drxlnqnnocixuz6bk2at.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1530354668/drxlnqnnocixuz6bk2at.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1530354668/drxlnqnnocixuz6bk2at.jpg"}},"width":"3024.0","height":"4032.0","file_type":"image","imageable_id":"76323ba1-83dd-443c-8eb0-2428ecd4343d","imageable_type":"Post","created_at":"2018-06-30T10:31:06.028Z","updated_at":"2018-06-30T10:31:06.028Z"}]
         * user : {"id":"2cdf7da8-9d04-4bb0-9a28-decb5ab46974","email":"aa@ss.sss","name":"sss","user_name":"","image":null}
         * hash_tags : []
         * tag_friends : []
         * likes_count : 1
         * current_user_like : false
         * isSaved : false
         * comments : 0
         * comment_arr : []
         */

        private Post post;
        private UserBean user;
        private String likes_count;
        private boolean current_user_like;
        private boolean isSaved;
        private String comments;
        private List<PostImagesAttributesBean> post_images_attributes;
        private List<?> hash_tags;
        private List<?> tag_friends;
        private List<?> comment_arr;

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(String likes_count) {
            this.likes_count = likes_count;
        }

        public boolean isCurrent_user_like() {
            return current_user_like;
        }

        public void setCurrent_user_like(boolean current_user_like) {
            this.current_user_like = current_user_like;
        }

        public boolean isIsSaved() {
            return isSaved;
        }

        public void setIsSaved(boolean isSaved) {
            this.isSaved = isSaved;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public List<PostImagesAttributesBean> getPost_images_attributes() {
            return post_images_attributes;
        }

        public void setPost_images_attributes(List<PostImagesAttributesBean> post_images_attributes) {
            this.post_images_attributes = post_images_attributes;
        }

        public List<?> getHash_tags() {
            return hash_tags;
        }

        public void setHash_tags(List<?> hash_tags) {
            this.hash_tags = hash_tags;
        }

        public List<?> getTag_friends() {
            return tag_friends;
        }

        public void setTag_friends(List<?> tag_friends) {
            this.tag_friends = tag_friends;
        }

        public List<?> getComment_arr() {
            return comment_arr;
        }

        public void setComment_arr(List<?> comment_arr) {
            this.comment_arr = comment_arr;
        }


        public static class UserBean {
            /**
             * id : 2cdf7da8-9d04-4bb0-9a28-decb5ab46974
             * email : aa@ss.sss
             * name : sss
             * user_name :
             * image : null
             */

            private String id;
            private String email;
            private String name;
            private String user_name;
            private String image;

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

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public static class PostImagesAttributesBean {
            /**
             * id : fb94cc49-e637-4ba6-9d34-79e50ffc3310
             * file : {"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530354668/drxlnqnnocixuz6bk2at.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1530354668/drxlnqnnocixuz6bk2at.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1530354668/drxlnqnnocixuz6bk2at.jpg"}}
             * width : 3024.0
             * height : 4032.0
             * file_type : image
             * imageable_id : 76323ba1-83dd-443c-8eb0-2428ecd4343d
             * imageable_type : Post
             * created_at : 2018-06-30T10:31:06.028Z
             * updated_at : 2018-06-30T10:31:06.028Z
             */

            private String id;
            private FileBean file;
            private String width;
            private String height;
            private String file_type;
            private String imageable_id;
            private String imageable_type;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public FileBean getFile() {
                return file;
            }

            public void setFile(FileBean file) {
                this.file = file;
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

            public String getImageable_id() {
                return imageable_id;
            }

            public void setImageable_id(String imageable_id) {
                this.imageable_id = imageable_id;
            }

            public String getImageable_type() {
                return imageable_type;
            }

            public void setImageable_type(String imageable_type) {
                this.imageable_type = imageable_type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public static class FileBean {
                /**
                 * url : http://res.cloudinary.com/di8lsuqdb/image/upload/v1530354668/drxlnqnnocixuz6bk2at.jpg
                 * thumb : {"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1530354668/drxlnqnnocixuz6bk2at.jpg"}
                 * thumb1 : {"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1530354668/drxlnqnnocixuz6bk2at.jpg"}
                 */

                private String url;
                private ThumbBean thumb;
                private Thumb1Bean thumb1;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public ThumbBean getThumb() {
                    return thumb;
                }

                public void setThumb(ThumbBean thumb) {
                    this.thumb = thumb;
                }

                public Thumb1Bean getThumb1() {
                    return thumb1;
                }

                public void setThumb1(Thumb1Bean thumb1) {
                    this.thumb1 = thumb1;
                }

                public static class ThumbBean {
                    /**
                     * url : http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1530354668/drxlnqnnocixuz6bk2at.jpg
                     */

                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }

                public static class Thumb1Bean {
                    /**
                     * url : http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1530354668/drxlnqnnocixuz6bk2at.jpg
                     */

                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }
        }
    }
}
