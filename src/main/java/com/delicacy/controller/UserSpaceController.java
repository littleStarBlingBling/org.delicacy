package com.delicacy.controller;

import com.delicacy.bean.ResponseBean;
import com.delicacy.domain.Catalog;
import com.delicacy.domain.Praise;
import com.delicacy.domain.User;
import com.delicacy.enums.OrderEnum;
import com.delicacy.service.UserService;
import com.delicacy.util.ConstraintViolationExceptionHandler;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import com.delicacy.domain.Article;
import com.delicacy.enums.AuthorityEnum;
import com.delicacy.service.ArticleService;
import com.delicacy.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/u")
public class UserSpaceController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CatalogService catalogService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + user.getPinyinUsername() + "/articles";
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("userspace/profile", "userModel", model);
    }

    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId());
        if (Objects.isNull(originalUser)) {
            return StringUtils.EMPTY;
        }
        originalUser.setEmail(user.getEmail());

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String originPwd = originalUser.getPassword();
        String encodePwd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(originPwd, encodePwd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpdateUser(originalUser);
        return "redirect:/u/" + user.getPinyinUsername() + "/profile";
    }

    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("userspace/avatar", "userModel", model);
    }

    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseBean> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        String avatarUrl = user.getAvatar();
        User originUser = userService.getUserById(user.getId());
        originUser.setAvatar(avatarUrl);
        userService.saveOrUpdateUser(originUser);
        return ResponseEntity.ok().body(ResponseBean.of(true, "保存用户头像成功！", avatarUrl));
    }

    @GetMapping("/{username}/articles")
    public String listArticlesByOrder(
            @ModelAttribute
            @PathVariable(value = "username") String username,
            @RequestParam(value = "order", required = false, defaultValue = "new") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
            Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        Page<Article> page = null;
        Sort sort = null;
        if (OrderEnum.hot.getValue().equals(order)) {
            sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "praiseSize");
        } else if (OrderEnum.latest.getValue().equals(order)) {
            sort = new Sort(Sort.Direction.DESC, "createTime");
        }

        Pageable pageable = null;
        if (Objects.nonNull(sort)) {
            pageable = new PageRequest(pageIndex, pageSize, sort);
        } else {
            pageable = new PageRequest(pageIndex, pageSize);
        }

        page = articleService.listArticleByTitleLikeAndSort(user, keyword, pageable);
        if(Objects.nonNull(page)){
            model.addAttribute("articleList", page.getContent());
        }else {
            model.addAttribute("articleList", Lists.newArrayList());
        }
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        return (async == true ? "userspace/u :: #mainContainerReplace" : "userspace/u");
    }

    @GetMapping("/{username}/articles/{id}")
    public String getArticleById(@PathVariable("username") String username, @PathVariable("id") int id, Model model) {
        User user = null;
        Article article = articleService.getArticleById(id);
        articleService.readingIncrease(id);
        boolean isArticleOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals(AuthorityEnum.type.ROLE_ANON.getRole())
        ) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null && username.equals(user.getUsername())) {
                isArticleOwner = true;
            }
        }

        List<Praise> praiseList = article.getPraiseList();
        Praise currentPraise = null;

        if (user != null) {
            for (Praise praise : praiseList) {
                if (praise.getUser().getUsername().equals(user.getUsername())) {
                    currentPraise = praise;
                }
                break;
            }
        }

        model.addAttribute("currentPraise", currentPraise);
        model.addAttribute("isArticleOwner", isArticleOwner);
        model.addAttribute("articleModel", article);
        return "userspace/article";
    }

    @DeleteMapping("/{username}/articles/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseBean> deleteArticle(@PathVariable("username") String username, @PathVariable("id") int id) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        try {
            articleService.removeArticle(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + user.getPinyinUsername() + "/articles";
        return ResponseEntity.ok().body(ResponseBean.of(true, "删除文章成功！", redirectUrl));
    }

    @GetMapping("/{username}/articles/edit")
    public ModelAndView createArticle(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogList = catalogService.listCatalogs(user);

        model.addAttribute("catalogs", catalogList);
        model.addAttribute("article", new Article());
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("userspace/article_edit", "articleModel", model);
    }

    @GetMapping("/{username}/articles/edit/{id}")
    public ModelAndView editArticle(@PathVariable("username") String username, @PathVariable("id") int id, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogList = catalogService.listCatalogs(user);
        Article article = articleService.getArticleById(id);

        model.addAttribute("catalogs", catalogList);
        model.addAttribute("article", article);
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/article_edit", "articleModel", model);
    }

    @PostMapping("/{username}/articles/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseBean> saveArticle(@PathVariable("username") String username, @RequestBody Article article) {
        if (article.getCatalog().getId() <= 0) {
            return ResponseEntity.ok().body(ResponseBean.of(false, "未选择分类"));
        }
        User user = (User) userDetailsService.loadUserByUsername(username);
        try {
            if (article.getId() > 0) {
                Article originArticle = articleService.getArticleById(article.getId());
                articleService.saveArticle(updateArticle(originArticle, article));
            } else {
                article.setUser(user);
                articleService.saveArticle(article);
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        String redirectUrl = "/u/" + user.getPinyinUsername() + "/articles/" + article.getId();
        return ResponseEntity.ok().body(ResponseBean.of(true, "处理成功", redirectUrl));
    }


    private Article updateArticle(Article originArticle, Article article)  {
        if (StringUtils.isNotEmpty(article.getTitle()) && !article.getTitle().equals(originArticle.getTitle())) {
            originArticle.setTitle(article.getTitle());
        }

        if (StringUtils.isNotEmpty(article.getContent()) && !article.getContent().equals(originArticle.getContent())) {
            originArticle.setContent(article.getContent());
        }

        if (StringUtils.isNotEmpty(article.getSummary()) && !article.getSummary().equals(originArticle.getSummary())) {
            originArticle.setSummary(article.getSummary());
        }

        if (Objects.nonNull(article.getCatalog()) && !article.getCatalog().equals(originArticle.getCatalog())) {
            originArticle.setCatalog(article.getCatalog());
        }

        if (StringUtils.isNotEmpty(article.getTags()) && !article.getTags().equals(originArticle.getTags())) {
            originArticle.setTags(article.getTags());
        }

        if (StringUtils.isNotEmpty(article.getCanteen()) && !article.getCanteen().equals(originArticle.getCanteen())) {
            originArticle.setContent(article.getCanteen());
        }

        if (StringUtils.isNotEmpty(article.getAddress()) && !article.getAddress().equals(originArticle.getAddress())) {
            originArticle.setPrice(article.getAddress());
        }

        if (StringUtils.isNotEmpty(article.getPrice()) && !article.getPrice().equals(originArticle.getPrice())) {
            originArticle.setPrice(article.getPrice());
        }
        return originArticle;
    }
}
