package com.delicacy.service;

import com.delicacy.domain.Comment;
import com.delicacy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Author: MyDear
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeComment(int id) {
        commentRepository.delete(id);
    }
}
