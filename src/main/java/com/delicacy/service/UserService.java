package com.delicacy.service;

import com.delicacy.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author: MyDear
 */
public interface UserService {
    User saveOrUpdateUser(User user);

    User registerUser(User user);

    void removeUser(int id);

    User getUserById(int id);

    Page<User> listUsersByUsernameLike(String username, Pageable pageable);

    List<User> listUsersByUsername(List<String> username);
}
