package com.wind.carmanager.model;

/**
 * 作者：Created by luow on 2018/7/25
 * 注释：
 */
public class VersionNameBean extends HttpResponse{

    /**
     * result : {"version":{"package_name":"abc.apk","version_code":12,"version_name":"1.2.0","content":"这是一个安装包","force":1,"time":"2018-07-24 13:31:41"}}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * version : {"package_name":"abc.apk","version_code":12,"version_name":"1.2.0","content":"这是一个安装包","force":1,"time":"2018-07-24 13:31:41"}
         */

        private VersionBean version;

        public VersionBean getVersion() {
            return version;
        }

        public void setVersion(VersionBean version) {
            this.version = version;
        }

        public static class VersionBean {
            /**
             * package_name : abc.apk
             * version_code : 12
             * version_name : 1.2.0
             * content : 这是一个安装包
             * force : 1
             * time : 2018-07-24 13:31:41
             */

            private String package_name;
            private int version_code;
            private String version_name;
            private String content;
            private int force;
            private String time;

            public String getPackage_name() {
                return package_name;
            }

            public void setPackage_name(String package_name) {
                this.package_name = package_name;
            }

            public int getVersion_code() {
                return version_code;
            }

            public void setVersion_code(int version_code) {
                this.version_code = version_code;
            }

            public String getVersion_name() {
                return version_name;
            }

            public void setVersion_name(String version_name) {
                this.version_name = version_name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getForce() {
                return force;
            }

            public void setForce(int force) {
                this.force = force;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
