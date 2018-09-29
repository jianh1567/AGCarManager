package com.wind.carmanager.model;

import java.util.List;

/**
 * 作者：Created by luow on 2018/7/23
 * 注释：
 */
public class GetWarnBean extends HttpResponse{

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<WarnBean> warn;

        public List<WarnBean> getWarn() {
            return warn;
        }

        public void setWarn(List<WarnBean> warn) {
            this.warn = warn;
        }

        public static class WarnBean {
            /**
             * device_id : 1
             * time : 2018-07-23 19:20:23
             * warn_content : 这不是演习！！！
             * warn_title : 警报警报
             * id:7
             */

            private int device_id;
            private int id;
            private String time;
            private String warn_content;
            private String warn_title;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getDevice_id() {
                return device_id;
            }

            public void setDevice_id(int device_id) {
                this.device_id = device_id;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getWarn_content() {
                return warn_content;
            }

            public void setWarn_content(String warn_content) {
                this.warn_content = warn_content;
            }

            public String getWarn_title() {
                return warn_title;
            }

            public void setWarn_title(String warn_title) {
                this.warn_title = warn_title;
            }
        }
    }
}
