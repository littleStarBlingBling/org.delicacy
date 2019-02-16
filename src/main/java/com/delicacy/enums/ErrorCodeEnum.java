package com.delicacy.enums;

/**
 * @Author: MyDear
 */
public enum ErrorCodeEnum {
    WRONG_PARAMETER("参数错误"),
    USER_NOT_EXIST( "用户不存在"),
    NO_AUTHORITY("没有操作权限"),
    CATALOG_EXIST("分类已存在"),
    EMPTY_CONTENT("评论为空");
    ;

    private String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
