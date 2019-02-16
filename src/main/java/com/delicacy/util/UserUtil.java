package com.delicacy.util;

import com.delicacy.domain.User;
import com.delicacy.enums.AuthorityEnum;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @Author: MyDear
 */
public class UserUtil {
    public static User isOwner() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals(AuthorityEnum.type.ROLE_ANON.getRole())) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (Objects.nonNull(principal)) {
                return principal;
            }
        }
        return null;
    }
}
