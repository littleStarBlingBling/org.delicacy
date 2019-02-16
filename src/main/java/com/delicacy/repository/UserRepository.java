package com.delicacy.repository;

import com.delicacy.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: MyDear
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findByUsernameLike(String username, Pageable pageable);

    User findByUsername(String username);

    List<User> findByUsernameIn(List<String> username);
}
