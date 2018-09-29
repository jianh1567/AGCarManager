package com.wind.carmanager.model;

/**
 * 作者：Created by luow on 2018/7/2
 * 注释：登录返回参数
 */
public class LoginBean extends HttpResponse {

    /**
     * result : {"token":{"token":"eyJhbGciOiJIUzI1NiIsImlhdCI6MTUzMDUzMDgyNywiZXhwIjoxNTMxMTM1NjI3fQ.eyJ1c2VyX2lkIjo2fQ.NYXKP0lFlwiLptEBvZILjUYsw6Eb5s2me9y2B5ouM3Q"}}
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
         * token : {"token":"eyJhbGciOiJIUzI1NiIsImlhdCI6MTUzMDUzMDgyNywiZXhwIjoxNTMxMTM1NjI3fQ.eyJ1c2VyX2lkIjo2fQ.NYXKP0lFlwiLptEBvZILjUYsw6Eb5s2me9y2B5ouM3Q"}
         */

        private TokenBean token;

        public TokenBean getToken() {
            return token;
        }

        public void setToken(TokenBean token) {
            this.token = token;
        }

        public static class TokenBean {
            /**
             * token : eyJhbGciOiJIUzI1NiIsImlhdCI6MTUzMDUzMDgyNywiZXhwIjoxNTMxMTM1NjI3fQ.eyJ1c2VyX2lkIjo2fQ.NYXKP0lFlwiLptEBvZILjUYsw6Eb5s2me9y2B5ouM3Q
             */

            private String token;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
