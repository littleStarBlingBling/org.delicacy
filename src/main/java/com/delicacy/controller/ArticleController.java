package com.delicacy.controller;

import com.delicacy.domain.Article;
import com.delicacy.service.ArticleService;
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

import java.util.List;
import java.util.Objects;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public String listArticle(
            @RequestParam(value = "order", required = false, defaultValue = "new") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
            Model model) {
        Page<Article> page = null;
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "praiseSize", "createTime");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = articleService.listArticles(pageable);
        } catch (Exception e) {
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = articleService.listArticles(pageable);
        }

        if (Objects.isNull(page)) {
            return "index";
        }
        List<Article> list = page.getContent();
        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("articleList", list);

        return (async == true ? "index :: #mainContainerReplace" : "index");
    }

}
