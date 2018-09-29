package com.wind.carmanager.utils;

/**
 * Created by houjian on 2018/5/30.
 */

public class UserInfo {
    private  String tableName;
    private  String todo;
    private  String insertMethod;
    private  String username;
    private  String password;
    private  String registrationId;
    private  String method;

    private UserInfo(Builder builder){
        this.tableName = builder.tableName;
        this.todo = builder.todo;
        this.insertMethod = builder.insertMethod;
        this.username = builder.username;
        this.password = builder.password;
        this.registrationId = builder.registrationId;
        this.method = builder.method;
    }

    public static class Builder{
        private  String tableName;
        private  String todo;
        private  String insertMethod;
        private  String username;
        private  String password;
        private  String registrationId;
        private  String method;

        public Builder setTableName(String tableName){
            this.tableName = tableName;
            return this;
        }

        public Builder setTodo(String todo){
            this.todo = todo;
            return this;
        }

        public Builder setInsertMethod(String insertMethod){
            this.insertMethod = insertMethod;
            return this;
        }

        public Builder setUsername(String username){
            this.username = username;
            return this;
        }

        public Builder setPassword(String password){
            this.password = password;
            return this;
        }

        public Builder setRegistrationId(String registrationId){
            this.registrationId = registrationId;
            return this;
        }

        public Builder setMethod(String method){
            this.method = method;
            return this;
        }

        public UserInfo build(){
            return new UserInfo(this);
        }
    }
}
