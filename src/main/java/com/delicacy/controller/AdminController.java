package com.delicacy.controller;

import com.delicacy.bean.MenuBean;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public ModelAndView listUsers(Model model) {
        List<MenuBean> list = Lists.newArrayList(MenuBean.of("用户管理", "/admin/user"), MenuBean.of("文章管理", "/admin/manage"));
        model.addAttribute("list", list);
        return new ModelAndView("/admin/index", "model", model);
    }
}
