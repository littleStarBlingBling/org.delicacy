package com.delicacy.enums;

/**
 * @Author: MyDear
 */
public enum OrderEnum {
    hot("hot"),
    latest("new"),
    ;

    private String value;

    OrderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }}
