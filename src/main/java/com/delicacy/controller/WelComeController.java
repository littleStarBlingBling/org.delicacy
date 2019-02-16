package com.delicacy.controller;

import com.delicacy.domain.User;
import com.google.common.collect.Lists;
import com.delicacy.domain.Authority;
import com.delicacy.enums.AuthorityEnum;
import com.delicacy.service.AuthorityService;
import com.delicacy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

/**
 * @Author: MyDear
 */
@Controller
public class WelComeController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/articles";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，账号或者密码错误！");
        return "/login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        if (Objects.isNull(user)) {
            return "register";
        }
        Authority authority = authorityService.getAuthorityById(AuthorityEnum.type.ROLE_USER.getValue());
        if (Objects.isNull(authority)) {
            return "register";
        }
        user.setAuthorityList(Lists.newArrayList(authority));
        userService.registerUser(user);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser() {
        return "redirect:/index";
    }

    @PostMapping("/login?logout")
    public String loginOut() {
        return "welcome";
    }
}
