package com.delicacy.enums;

import lombok.Data;

/**
 * @Author: MyDear
 */
public class AuthorityEnum {

    public enum type {
        ROLE_ADMIN(1, "管理", "admin"),
        ROLE_USER(2, "用户","user"),
        ROLE_ANON(3, "匿名", "anonymousUser"),;

        private int value;
        private String des;
        private String role;

        type(int value, String des, String role) {
            this.value = value;
            this.des = des;
            this.role = role;
        }

        public int getValue() {
            return value;
        }

        public String getRole() {
            return role;
        }
    }
}
