package com.wind.carmanager.model;

/**
 * 作者：Created by luow on 2018/7/20
 * 注释：
 */
public class ForgetPwdBean extends HttpResponse {

    /**
     * result : {"token":{"token":"eyJhbGciOiJIUzI1NiIsImlhdCI6MTUyOTg5MzAxNSwiZXhwIjoxNTMwNDk3ODE1fQ.eyJ1c2VyX2lkIjoxfQ.G4KatPutlvVeJQZMfHI5a9PIVGBRJDu8w810pwx0vJs"}}
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
         * token : {"token":"eyJhbGciOiJIUzI1NiIsImlhdCI6MTUyOTg5MzAxNSwiZXhwIjoxNTMwNDk3ODE1fQ.eyJ1c2VyX2lkIjoxfQ.G4KatPutlvVeJQZMfHI5a9PIVGBRJDu8w810pwx0vJs"}
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
             * token : eyJhbGciOiJIUzI1NiIsImlhdCI6MTUyOTg5MzAxNSwiZXhwIjoxNTMwNDk3ODE1fQ.eyJ1c2VyX2lkIjoxfQ.G4KatPutlvVeJQZMfHI5a9PIVGBRJDu8w810pwx0vJs
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
