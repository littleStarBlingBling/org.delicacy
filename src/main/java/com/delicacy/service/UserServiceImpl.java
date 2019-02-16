package com.delicacy.service;

import com.delicacy.domain.User;
import com.delicacy.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: MyDear
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(int id) {
        userRepository.delete(id);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findOne(id);
    }

    @Override
    public Page<User> listUsersByUsernameLike(String username, Pageable pageable) {
        if(StringUtils.isEmpty(username)){
            return userRepository.findAll(pageable);
        }
        username = "%" + username + "%";
        return userRepository.findByUsernameLike(username, pageable);
    }

    @Override
    public List<User> listUsersByUsername(List<String> username) {
        return userRepository.findByUsernameIn(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }


}
