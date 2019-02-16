package com.delicacy.repository;

import com.delicacy.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: MyDear
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
