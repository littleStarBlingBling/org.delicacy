package com.delicacy.service;

import com.delicacy.domain.Comment;

/**
 * @Author: MyDear
 */
public interface CommentService {
    Comment getCommentById(int id);

    void removeComment(int id);
}
