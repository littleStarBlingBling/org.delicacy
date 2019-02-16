package com.delicacy.controller;

import com.delicacy.bean.ResponseBean;
import com.delicacy.domain.Comment;
import com.delicacy.domain.User;
import com.delicacy.enums.ErrorCodeEnum;
import com.delicacy.service.CommentService;
import com.delicacy.util.ConstraintViolationExceptionHandler;
import com.delicacy.util.UserUtil;
import com.delicacy.domain.Article;
import com.delicacy.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    private static String USERSPACE_ARTICLE = "userspace/article";

    @GetMapping
    public String listComments(@RequestParam(value = "articleId") int id, Model model) {
        if (id < 0) {
            return USERSPACE_ARTICLE;
        }
        Article article = articleService.getArticleById(id);
        List<Comment> commentList = article.getCommentList();

        User user = UserUtil.isOwner();
        if (Objects.nonNull(user)) {
            model.addAttribute("commentOwner", user.getUsername());
        }

        model.addAttribute("comments", commentList);
        return "userspace/article :: #mainContainerReplace";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ResponseBean> createComment(int articleId, String commentContent) {
        try {
            if(StringUtils.isEmpty(commentContent)){
                return ResponseEntity.ok().body(ResponseBean.of(false, ErrorCodeEnum.EMPTY_CONTENT.getMessage()));
            }
            articleService.createComment(articleId, commentContent);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true, "发表评论成功！", StringUtils.EMPTY));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ResponseBean> delete(@PathVariable("id") int id, int articleId) {
        User principal = UserUtil.isOwner();
        User commentUser = commentService.getCommentById(id).getUser();
        if (Objects.isNull(principal) || principal.getId() != commentUser.getId()) {
            return ResponseEntity.ok().body(ResponseBean.of(false, ErrorCodeEnum.NO_AUTHORITY.getMessage()));
        }
        try {
            articleService.removeComment(articleId, id);
            commentService.removeComment(id);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true, "删除评论成功！", StringUtils.EMPTY));
    }
}
