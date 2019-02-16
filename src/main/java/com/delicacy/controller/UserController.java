package com.delicacy.controller;

import com.delicacy.bean.ResponseBean;
import com.delicacy.domain.Authority;
import com.delicacy.domain.User;
import com.delicacy.enums.ErrorCodeEnum;
import com.delicacy.service.AuthorityService;
import com.delicacy.service.UserService;
import com.delicacy.util.ConstraintViolationExceptionHandler;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;


/**
 * @Author: MyDear
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;

    @GetMapping
    public ModelAndView list(
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            Model model) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<User> page = userService.listUsersByUsernameLike(username, pageable);
        List<User> list = page.getContent();

        model.addAttribute("page", page);
        model.addAttribute("userList", list);
        return new ModelAndView(async == true ? "admin/user/list ::#mainContainerReplace" : "admin/user/list", "userModel", model);
    }

    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("admin/user/add", "userModel", model);
    }

    @PostMapping
    public ResponseEntity<ResponseBean> saveOrUpdate(User user, int authorityId) {
        List<Authority> authorityList = Lists.newArrayList(authorityService.getAuthorityById(authorityId));
        user.setAuthorityList(authorityList);
        try {
            userService.saveOrUpdateUser(user);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true, "保存用户成功！", user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseBean> delete(@PathVariable("id") int id) {
        if (id <= 0) {
            return ResponseEntity.ok().body(ResponseBean.of(false, ErrorCodeEnum.WRONG_PARAMETER.getMessage()));
        }

        try {
            userService.removeUser(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true, "删除用户成功！"));
    }

    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id")int id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("admin/user/edit", "userModel", model);
    }
}
