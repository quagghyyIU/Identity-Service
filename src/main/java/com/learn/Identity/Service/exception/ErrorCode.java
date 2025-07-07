package com.learn.Identity.Service.exception;

public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(1000, "Uncategorized exception"),
    INVALID_KEY(1004, "INVALID MESSAGE KEY"),
    USER_EXISTED(1001, "User already exists"),
    USERNAME_INVALID(1002, "Username must be between 3 and 50 characters long"),
    INVALID_PASSWORD(1003, "Password must be at least 8 characters long"),
    INVALID_CREDENTIALS(1004, "Invalid credentials"),
    ;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }


}
