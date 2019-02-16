package com.delicacy.controller;

import com.delicacy.bean.ResponseBean;
import com.delicacy.domain.User;
import com.delicacy.enums.ErrorCodeEnum;
import com.delicacy.util.ConstraintViolationExceptionHandler;
import com.delicacy.util.UserUtil;
import com.delicacy.service.ArticleService;
import com.delicacy.service.PraiseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/praises")
public class PraiseController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private PraiseService praiseService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ResponseBean> createPraise(int articleId) {
        try {
            articleService.createPraise(articleId);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true,  "点赞成功!", StringUtils.EMPTY));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ResponseBean> delete(@PathVariable("id") int id, int articleId) {
        User user = praiseService.getPraiseById(id).getUser();
        User principal = UserUtil.isOwner();
        if (Objects.isNull(principal) || user.getId() != principal.getId()) {
            return ResponseEntity.ok().body(ResponseBean.of(false, ErrorCodeEnum.NO_AUTHORITY.getMessage()));
        }
        try {
            articleService.removePraise(articleId, id);
            praiseService.removePraise(id);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true,  "取消点赞成功！", StringUtils.EMPTY));
    }
}
