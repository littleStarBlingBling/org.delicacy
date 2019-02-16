package com.delicacy.controller;

import com.delicacy.domain.Article;
import com.delicacy.service.ArticleService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/admin/manage")
public class ManagerController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ModelAndView listAllArticles(
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
            Model model) {

        Pageable pageable = new PageRequest(pageIndex, pageSize, new Sort(Sort.Direction.DESC));
        Page<Article> page = articleService.listArticles(pageable);
        if (Objects.nonNull(page)) {
            model.addAttribute("articleList", page.getContent());
        } else {
            model.addAttribute("articleList", Lists.newArrayList());
        }

        model.addAttribute("page", "page");
        return new ModelAndView(async == true ? "admins/list :: #mainContainerReplace" : "admins/list", "ManagerModel", model);
    }
}
