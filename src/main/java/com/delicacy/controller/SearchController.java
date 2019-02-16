package com.delicacy.controller;

import com.delicacy.enums.OrderEnum;
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

import java.util.Optional;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ArticleService articleService;

    private static String SEARCH = "search :: #mainContainerReplace";

    @GetMapping
    public String listArticleForSearch(
            @RequestParam(value = "order", required = false, defaultValue = "new") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
            Model model) {
        Page<Article> page = null;
        try {
            Sort sort = null;
            if (order.equals(OrderEnum.hot.getValue())) {
                sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "praiseSize", "createTime");
            } else if (order.equals(OrderEnum.latest.getValue())) {
                sort = new Sort(Sort.Direction.DESC, "createTime");
            }
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = articleService.listArticlesByKeyword(keyword, pageable);
        } catch (Exception e) {
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = articleService.listArticles(pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("articleList", page.getContent());

        Optional.ofNullable(articleService.listTop8LatestArticles()).ifPresent(item -> {
            model.addAttribute("newest", item);
        });
        Optional.ofNullable(articleService.listTop8HotArticles()).ifPresent(item -> {
            model.addAttribute("hotest", item);
        });
        Optional.ofNullable(articleService.listTop8Tags()).ifPresent(item -> {
            model.addAttribute("catalogs", item);
        });
        Optional.ofNullable(articleService.listTop8Users()).ifPresent(item -> {
            model.addAttribute("users", item);
        });

        return (async == true ? SEARCH : "search");
    }
}
