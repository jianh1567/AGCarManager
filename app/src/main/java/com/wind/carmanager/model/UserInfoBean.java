package com.wind.carmanager.model;

import java.io.Serializable;

/**
 * Created by W010003373 on 2018/7/23.
 */

public class UserInfoBean implements Serializable{
    private int code;
    private String message;
    private ResultBean result;

    public int getCode(){
        return this.code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public ResultBean getResultBean(){
        return this.result;
    }

    public void setResultBean(ResultBean result){
        this.result = result;
    }

    public static class ResultBean{
        private User user;

        public User getUser(){
            return this.user;
        }

        public void setUser(User user){
            this.user = user;
        }

        public static class User{
            private int id;
            private String phone;
            private String username;
            private String email;
            private String birthday;
            private int sex;
            private String create_time;
            private String update_time;

            public int getId(){
                return this.id;
            }

            public void setId(int id){
                this.id = id;
            }

            public String getPhone(){
                return this.phone;
            }

            public void setPhone(String phone){
                this.phone = phone;
            }

            public String getUsername(){
                return this.username;
            }

            public void setUsername(String username){
                this.username = username;
            }

            public String getEmail(){
                return this.email;
            }

            public void setEmail(String email){
                this.email = email;
            }

            public String getBirthday(){
                return this.birthday;
            }

            public void setBirthday(String birthday){
                this.birthday = birthday;
            }

            public int getSex(){
                return this.sex;
            }

            public void setSex(int sex){
                this.sex = sex;
            }

            public String getCreate_time(){
                return this.create_time;
            }

            public void setCreate_time(String create_time){
                this.create_time = create_time;
            }

            public String getUpdate_time(){
                return this.update_time;
            }

            public void setUpdate_time(String update_time){
                this.update_time = update_time;
            }
        }
    }
}
