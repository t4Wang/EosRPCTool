package com.runze.eos.rpctool.exception;

/**
 {
     "code": 500,
     "message": "Internal Service Error",
     "error": {
         "code": 3015001,
         "name": "abi_not_found_exception",
         "what": "No ABI found",
         "details": []
     }
 }
 */
public class ErrorResponse {
    private String code;
    private String message;
    private Error error;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error {
        private String code;
        private String name;
        private String what;
        private String[] details;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWhat() {
            return what;
        }

        public void setWhat(String what) {
            this.what = what;
        }

        public String[] getDetails() {
            return details;
        }

        public void setDetails(String[] details) {
            this.details = details;
        }
    }
}
