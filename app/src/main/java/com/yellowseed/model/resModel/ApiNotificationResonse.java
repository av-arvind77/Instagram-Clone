package com.yellowseed.model.resModel;

import java.io.Serializable;
import java.util.List;

public class ApiNotificationResonse implements Serializable{


    /**
     * responseCode : 200
     * responseMessage : Notifications fetched successfully.
     * notifications : [{"id":"5a9db278-0a02-4ecc-ab69-7798b4c64f84","notifiable_id":"5e999f25-0208-41b2-9980-a49b7b1181b3","notifiable_type":"Post","title":"sexy boy liked your post.","content":"sexy boy liked shasha grey's post.","status":false,"receiver_id":"acc0e0b7-89d0-484b-967d-3c6651133708","user_id":"c547f486-d26f-45ae-a5cd-110eb12db177","notification_type":"post_like","created_at":"2018-07-25T10:35:18.005Z","updated_at":"2018-07-25T10:35:18.005Z","action_user":{"id":"c547f486-d26f-45ae-a5cd-110eb12db177","user_name":"","name":"sexy boy","image":null},"other_user":{"id":"acc0e0b7-89d0-484b-967d-3c6651133708","user_name":"","name":"shasha grey"},"post_info":{"id":"5e999f25-0208-41b2-9980-a49b7b1181b3","post_images_attributes":{"id":"7785d172-ef6a-46b8-8f5a-ba32e48a12bc","file":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1532164648/gnyxeou3wdk5ahayokqy.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1532164648/gnyxeou3wdk5ahayokqy.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1532164648/gnyxeou3wdk5ahayokqy.jpg"}},"width":"","height":"","file_type":"","imageable_id":"5e999f25-0208-41b2-9980-a49b7b1181b3","imageable_type":"Post","created_at":"2018-07-21T09:17:35.967Z","updated_at":"2018-07-21T09:17:35.967Z"}}}]
     * pagination : {"page_no":1,"per_page":10,"max_page_size":1,"total_records":1}
     */

    private int responseCode;
    private String responseMessage;
    private PaginationBean pagination;
    private List<NotificationsBean> notifications;

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

    public List<NotificationsBean> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationsBean> notifications) {
        this.notifications = notifications;
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

    public static class NotificationsBean implements Serializable{
        /**
         * id : 5a9db278-0a02-4ecc-ab69-7798b4c64f84
         * notifiable_id : 5e999f25-0208-41b2-9980-a49b7b1181b3
         * notifiable_type : Post
         * title : sexy boy liked your post.
         * content : sexy boy liked shasha grey's post.
         * status : false
         * receiver_id : acc0e0b7-89d0-484b-967d-3c6651133708
         * user_id : c547f486-d26f-45ae-a5cd-110eb12db177
         * notification_type : post_like
         * created_at : 2018-07-25T10:35:18.005Z
         * updated_at : 2018-07-25T10:35:18.005Z
         * action_user : {"id":"c547f486-d26f-45ae-a5cd-110eb12db177","user_name":"","name":"sexy boy","image":null}
         * other_user : {"id":"acc0e0b7-89d0-484b-967d-3c6651133708","user_name":"","name":"shasha grey"}
         * post_info : {"id":"5e999f25-0208-41b2-9980-a49b7b1181b3","post_images_attributes":{"id":"7785d172-ef6a-46b8-8f5a-ba32e48a12bc","file":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1532164648/gnyxeou3wdk5ahayokqy.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1532164648/gnyxeou3wdk5ahayokqy.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1532164648/gnyxeou3wdk5ahayokqy.jpg"}},"width":"","height":"","file_type":"","imageable_id":"5e999f25-0208-41b2-9980-a49b7b1181b3","imageable_type":"Post","created_at":"2018-07-21T09:17:35.967Z","updated_at":"2018-07-21T09:17:35.967Z"}}
         */

        private String id;
        private String notifiable_id;
        private String notifiable_type;
        private String title;
        private String content;
        private boolean status;
        private String receiver_id;
        private String user_id;
        private String notification_type;
        private String created_at;
        private String updated_at;
        private ActionUserBean action_user;
        private OtherUserBean other_user;
        private PostInfoBean post_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNotifiable_id() {
            return notifiable_id;
        }

        public void setNotifiable_id(String notifiable_id) {
            this.notifiable_id = notifiable_id;
        }

        public String getNotifiable_type() {
            return notifiable_type;
        }

        public void setNotifiable_type(String notifiable_type) {
            this.notifiable_type = notifiable_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(String receiver_id) {
            this.receiver_id = receiver_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNotification_type() {
            return notification_type;
        }

        public void setNotification_type(String notification_type) {
            this.notification_type = notification_type;
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

        public ActionUserBean getAction_user() {
            return action_user;
        }

        public void setAction_user(ActionUserBean action_user) {
            this.action_user = action_user;
        }

        public OtherUserBean getOther_user() {
            return other_user;
        }

        public void setOther_user(OtherUserBean other_user) {
            this.other_user = other_user;
        }

        public PostInfoBean getPost_info() {
            return post_info;
        }

        public void setPost_info(PostInfoBean post_info) {
            this.post_info = post_info;
        }

        public static class ActionUserBean implements Serializable{
            /**
             * id : c547f486-d26f-45ae-a5cd-110eb12db177
             * user_name :
             * name : sexy boy
             * image : null
             */

            private String id;
            private String user_name;
            private String name;
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
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
        }

        public static class OtherUserBean implements Serializable{
            /**
             * id : acc0e0b7-89d0-484b-967d-3c6651133708
             * user_name :
             * name : shasha grey
             */

            private String id;
            private String user_name;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PostInfoBean implements Serializable{
            /**
             * id : 5e999f25-0208-41b2-9980-a49b7b1181b3
             * post_images_attributes : {"id":"7785d172-ef6a-46b8-8f5a-ba32e48a12bc","file":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1532164648/gnyxeou3wdk5ahayokqy.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1532164648/gnyxeou3wdk5ahayokqy.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1532164648/gnyxeou3wdk5ahayokqy.jpg"}},"width":"","height":"","file_type":"","imageable_id":"5e999f25-0208-41b2-9980-a49b7b1181b3","imageable_type":"Post","created_at":"2018-07-21T09:17:35.967Z","updated_at":"2018-07-21T09:17:35.967Z"}
             */

            private String id;
            private PostImagesAttributesBean post_images_attributes;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public PostImagesAttributesBean getPost_images_attributes() {
                return post_images_attributes;
            }

            public void setPost_images_attributes(PostImagesAttributesBean post_images_attributes) {
                this.post_images_attributes = post_images_attributes;
            }

            public static class PostImagesAttributesBean implements Serializable{
                /**
                 * id : 7785d172-ef6a-46b8-8f5a-ba32e48a12bc
                 * file : {"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1532164648/gnyxeou3wdk5ahayokqy.jpg","thumb":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1532164648/gnyxeou3wdk5ahayokqy.jpg"},"thumb1":{"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1532164648/gnyxeou3wdk5ahayokqy.jpg"}}
                 * width :
                 * height :
                 * file_type :
                 * imageable_id : 5e999f25-0208-41b2-9980-a49b7b1181b3
                 * imageable_type : Post
                 * created_at : 2018-07-21T09:17:35.967Z
                 * updated_at : 2018-07-21T09:17:35.967Z
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

                public static class FileBean implements Serializable{
                    /**
                     * url : http://res.cloudinary.com/di8lsuqdb/image/upload/v1532164648/gnyxeou3wdk5ahayokqy.jpg
                     * thumb : {"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1532164648/gnyxeou3wdk5ahayokqy.jpg"}
                     * thumb1 : {"url":"http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1532164648/gnyxeou3wdk5ahayokqy.jpg"}
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
                         * url : http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_50,w_50/v1532164648/gnyxeou3wdk5ahayokqy.jpg
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
                         * url : http://res.cloudinary.com/di8lsuqdb/image/upload/c_fit,h_150,w_150/v1532164648/gnyxeou3wdk5ahayokqy.jpg
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
}
