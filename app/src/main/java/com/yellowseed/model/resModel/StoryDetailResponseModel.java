package com.yellowseed.model.resModel;

import com.yellowseed.model.reqModel.ViewerModel;
import com.yellowseed.webservices.requests.Viewer;

import java.io.Serializable;
import java.util.List;

public class StoryDetailResponseModel implements Serializable{

    /**
     * responseCode : 200
     * responseMessage : Story fetched successfully.
     * stories : [{"user":{"id":"ba28e5b0-b8a3-47ec-9359-994549abd833","email":"henant.singh@mobiloitte.in","name":"Hemantlocal","user_name":"","image":null},"story":{"story_id":"069f76f8-45e1-43e8-b6b8-753774d82c83","created_at":"2018-06-28T12:14:08.403Z","story_image":{"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530188065/k0x1skot3j7n6hdtpasi.jpg","width":"555","height":"558","file_type":".jpg"}},"tag_friends":[],"check_in":{"id":"e4670802-e27d-4b00-832f-d0d2096e91a0","location":"Ghaziabad, Uttar Pradesh, India","latitude":28.6691565,"longitude":77.4537578,"x_axis":"50","y_axis":"50","story_id":"069f76f8-45e1-43e8-b6b8-753774d82c83","created_at":"2018-06-28T12:14:37.208Z","updated_at":"2018-06-28T12:14:37.208Z"},"poll":{"id":"46da5829-3fb4-4cca-b59e-a6c86260b097","poll_title":"Poll One","type1":"type1","type2":"type2","x_axis":"50","y_axis":"50","story_id":"069f76f8-45e1-43e8-b6b8-753774d82c83","created_at":"2018-06-28T12:14:37.205Z","updated_at":"2018-06-28T12:14:37.205Z","current_user_poll":false,"type_1_count":0,"type_2_count":0}},{"user":{"id":"ba28e5b0-b8a3-47ec-9359-994549abd833","email":"henant.singh@mobiloitte.in","name":"Hemantlocal","user_name":"","image":null},"story":{"story_id":"7a3959e8-8a5f-4f1d-bcbc-69fa962fafcd","created_at":"2018-06-28T12:04:16.744Z","story_image":{"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530187458/cjoly2xxfw16fpipjzhb.jpg","width":"555","height":"558","file_type":".jpg"}},"tag_friends":[],"check_in":{"id":"beb3b219-af04-49fe-ae7c-a8b90f548384","location":"New Delhi, Delhi, India","latitude":28.6139391,"longitude":77.2090212,"x_axis":"50","y_axis":"50","story_id":"7a3959e8-8a5f-4f1d-bcbc-69fa962fafcd","created_at":"2018-06-28T12:04:20.137Z","updated_at":"2018-06-28T12:04:20.137Z"},"poll":{"id":"402a85a1-c404-4c8f-9180-4a252dabe95c","poll_title":"Poll One","type1":"type1","type2":"type2","x_axis":"50","y_axis":"50","story_id":"7a3959e8-8a5f-4f1d-bcbc-69fa962fafcd","created_at":"2018-06-28T12:04:20.109Z","updated_at":"2018-06-28T12:04:20.109Z","current_user_poll":false,"type_1_count":0,"type_2_count":0}}]
     */

    private int responseCode;
    private String responseMessage;


    private StoriesBean.StoryBean story;


    private List<StoriesBean> stories;


    public StoriesBean.StoryBean getStory() {
        return story;
    }

    public void setStory(StoriesBean.StoryBean story) {
        this.story = story;
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

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean implements Serializable{
        /**
         * user : {"id":"ba28e5b0-b8a3-47ec-9359-994549abd833","email":"henant.singh@mobiloitte.in","name":"Hemantlocal","user_name":"","image":null}
         * story : {"story_id":"069f76f8-45e1-43e8-b6b8-753774d82c83","created_at":"2018-06-28T12:14:08.403Z","story_image":{"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530188065/k0x1skot3j7n6hdtpasi.jpg","width":"555","height":"558","file_type":".jpg"}}
         * tag_friends : []
         * check_in : {"id":"e4670802-e27d-4b00-832f-d0d2096e91a0","location":"Ghaziabad, Uttar Pradesh, India","latitude":28.6691565,"longitude":77.4537578,"x_axis":"50","y_axis":"50","story_id":"069f76f8-45e1-43e8-b6b8-753774d82c83","created_at":"2018-06-28T12:14:37.208Z","updated_at":"2018-06-28T12:14:37.208Z"}
         * poll : {"id":"46da5829-3fb4-4cca-b59e-a6c86260b097","poll_title":"Poll One","type1":"type1","type2":"type2","x_axis":"50","y_axis":"50","story_id":"069f76f8-45e1-43e8-b6b8-753774d82c83","created_at":"2018-06-28T12:14:37.205Z","updated_at":"2018-06-28T12:14:37.205Z","current_user_poll":false,"type_1_count":0,"type_2_count":0}
         */

        private UserBean user;
        private int viewers_count;

        public int getViewers_count() {
            return viewers_count;
        }

        public void setViewers_count(int viewers_count) {
            this.viewers_count = viewers_count;
        }

        private StoryBean story;
        private CheckInBean check_in;
        private PollBean poll;
        private List<?> tag_friends;

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

        public CheckInBean getCheck_in() {
            return check_in;
        }

        public void setCheck_in(CheckInBean check_in) {
            this.check_in = check_in;
        }

        public PollBean getPoll() {
            return poll;
        }

        public void setPoll(PollBean poll) {
            this.poll = poll;
        }

        public List<?> getTag_friends() {
            return tag_friends;
        }

        public void setTag_friends(List<?> tag_friends) {
            this.tag_friends = tag_friends;
        }

        public static class UserBean {
            /**
             * id : ba28e5b0-b8a3-47ec-9359-994549abd833
             * email : henant.singh@mobiloitte.in
             * name : Hemantlocal
             * user_name :
             * image : null
             */

            private String id;
            private String email;
            private String name;
            private String user_name;
            private Object image;

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

            public Object getImage() {
                return image;
            }

            public void setImage(Object image) {
                this.image = image;
            }
        }

        public static class StoryBean {
            /**
             * story_id : 069f76f8-45e1-43e8-b6b8-753774d82c83
             * created_at : 2018-06-28T12:14:08.403Z
             * story_image : {"img":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530188065/k0x1skot3j7n6hdtpasi.jpg","width":"555","height":"558","file_type":".jpg"}
             */


            private List<StoriesBean.PollBean> polls;
            private List<ViewerModel> viewers;
            public List<StoriesBean.PollBean> getPolls() {
                return polls;
            }

            public void setPolls(List<StoriesBean.PollBean> polls) {
                this.polls = polls;
            }

            public List<ViewerModel> getViewers() {
                return viewers;
            }

            public void setViewers(List<ViewerModel> viewers) {
                this.viewers = viewers;
            }

            private String story_id;
            private int viewers_count;
            public int getViewers_count() {
                return viewers_count;
            }

            public void setViewers_count(int viewers_count) {
                this.viewers_count = viewers_count;
            }

            private String created_at;
            private StoryImageBean story_image;

            public String getStory_id() {
                return story_id;
            }

            public void setStory_id(String story_id) {
                this.story_id = story_id;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public StoryImageBean getStory_image() {
                return story_image;
            }

            public void setStory_image(StoryImageBean story_image) {
                this.story_image = story_image;
            }

            public static class StoryImageBean {
                /**
                 * img : http://res.cloudinary.com/di8lsuqdb/image/upload/v1530188065/k0x1skot3j7n6hdtpasi.jpg
                 * width : 555
                 * height : 558
                 * file_type : .jpg
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

        public static class CheckInBean {
            /**
             * id : e4670802-e27d-4b00-832f-d0d2096e91a0
             * location : Ghaziabad, Uttar Pradesh, India
             * latitude : 28.6691565
             * longitude : 77.4537578
             * x_axis : 50
             * y_axis : 50
             * story_id : 069f76f8-45e1-43e8-b6b8-753774d82c83
             * created_at : 2018-06-28T12:14:37.208Z
             * updated_at : 2018-06-28T12:14:37.208Z
             */

            private String id;
            private String location;
            private double latitude;
            private double longitude;
            private String x_axis;
            private String y_axis;
            private String story_id;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getX_axis() {
                return x_axis;
            }

            public void setX_axis(String x_axis) {
                this.x_axis = x_axis;
            }

            public String getY_axis() {
                return y_axis;
            }

            public void setY_axis(String y_axis) {
                this.y_axis = y_axis;
            }

            public String getStory_id() {
                return story_id;
            }

            public void setStory_id(String story_id) {
                this.story_id = story_id;
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
        }

        public  class PollBean implements Serializable{
            /**
             * id : 46da5829-3fb4-4cca-b59e-a6c86260b097
             * poll_title : Poll One
             * type1 : type1
             * type2 : type2
             * x_axis : 50
             * y_axis : 50
             * story_id : 069f76f8-45e1-43e8-b6b8-753774d82c83
             * created_at : 2018-06-28T12:14:37.205Z
             * updated_at : 2018-06-28T12:14:37.205Z
             * current_user_poll : false
             * type_1_count : 0
             * type_2_count : 0
             */

            private String id;
            private String name;
            private String email;
            private String poll_title;
            private String poll_content;
            private String type1;
            private String type2;
            private String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            private String x_axis;
            private String y_axis;
            private String story_id;
            private String created_at;
            private String updated_at;
            private boolean current_user_poll;
            private int type_1_count;
            private int type_2_count;

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

            public String getPoll_content() {
                return poll_content;
            }

            public void setPoll_content(String poll_content) {
                this.poll_content = poll_content;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPoll_title() {
                return poll_title;
            }

            public void setPoll_title(String poll_title) {
                this.poll_title = poll_title;
            }

            public String getType1() {
                return type1;
            }

            public void setType1(String type1) {
                this.type1 = type1;
            }

            public String getType2() {
                return type2;
            }

            public void setType2(String type2) {
                this.type2 = type2;
            }

            public String getX_axis() {
                return x_axis;
            }

            public void setX_axis(String x_axis) {
                this.x_axis = x_axis;
            }

            public String getY_axis() {
                return y_axis;
            }

            public void setY_axis(String y_axis) {
                this.y_axis = y_axis;
            }

            public String getStory_id() {
                return story_id;
            }

            public void setStory_id(String story_id) {
                this.story_id = story_id;
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

            public boolean isCurrent_user_poll() {
                return current_user_poll;
            }

            public void setCurrent_user_poll(boolean current_user_poll) {
                this.current_user_poll = current_user_poll;
            }

            public int getType_1_count() {
                return type_1_count;
            }

            public void setType_1_count(int type_1_count) {
                this.type_1_count = type_1_count;
            }

            public int getType_2_count() {
                return type_2_count;
            }

            public void setType_2_count(int type_2_count) {
                this.type_2_count = type_2_count;
            }
        }
    }
}
