package com.wind.carmanager.model;

import java.util.List;

/**
 * 作者：Created by luow on 2018/7/23
 * 注释：获取所有设备
 */
public class GetBikeBean extends HttpResponse{

    /**
     * result : {"devices":[{"create_time":"2018-07-16 21:36:00","id":1,"name":"device1","nickname":"这是我的车！","sensitivity":null,"status":"ONLINE","update_time":"2018-07-16 21:36:01"},{"create_time":"2018-07-16 21:35:39","id":2,"name":"device2","nickname":"这也是我的车！","sensitivity":"HIGH","status":"ONLINE","update_time":"2018-07-16 21:35:41"}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<DevicesBean> devices;

        public List<DevicesBean> getDevices() {
            return devices;
        }

        public void setDevices(List<DevicesBean> devices) {
            this.devices = devices;
        }

        public static class DevicesBean {
            /**
             * create_time : 2018-07-16 21:36:00
             * id : 1
             * name : device1
             * nickname : 这是我的车！
             * sensitivity : null
             * status : ONLINE
             * update_time : 2018-07-16 21:36:01
             */

            private String create_time;
            private int id;
            private String name;
            private String nickname;
            private Object sensitivity;
            private String status;
            private String update_time;

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public Object getSensitivity() {
                return sensitivity;
            }

            public void setSensitivity(Object sensitivity) {
                this.sensitivity = sensitivity;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }
        }
    }
}
