package com.delicacy.service;

import com.delicacy.domain.Article;
import com.delicacy.domain.Comment;
import com.delicacy.domain.Praise;
import com.delicacy.domain.User;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import com.delicacy.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: MyDear
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    @Transactional
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public void removeArticle(int id) {
        articleRepository.delete(id);
    }

    @Override
    public Article getArticleById(int id) {
        return articleRepository.findOne(id);
    }

    @Override
    public Page<Article> listArticleByTitleLikeAndSort(User user, String title, Pageable pageable) {
        if(StringUtils.isEmpty(title)){
            return articleRepository.findByUser(user, pageable);
        }
        title = "%" + title + "%";
        return articleRepository.findByUserAndTitleLike(user, title, pageable);
    }

    @Override
    public void readingIncrease(int id) {
        Article article = articleRepository.findOne(id);
        article.setReadSize(article.getCommentSize() + 1);
        this.saveArticle(article);
    }

    @Override
    public Page<Article> listArticlesByKeyword(String keyword, Pageable pageable) {
        keyword = "%" + keyword + "%";
        return articleRepository.findByTitleLike(keyword, pageable);
    }

    @Override
    public Article createComment(int articleId, String commentContent) {
        Article originArticle = articleRepository.findOne(articleId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = Comment.of(user, commentContent);
        originArticle.addComment(comment);
        return this.saveArticle(originArticle);
    }

    @Override
    public void removeComment(int articleId, int commentId) {
        Article originArticle = articleRepository.findOne(articleId);
        if (Objects.isNull(originArticle)) {
            return;
        }
        originArticle.removeComment(commentId);
        this.saveArticle(originArticle);
    }

    @Override
    public Article createPraise(int articleId) {
        Article originArticle = articleRepository.findOne(articleId);
        if (Objects.isNull(originArticle)) {
            return null;
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(user)) {
            return null;
        }
        Praise praise = Praise.of(user);
        if (originArticle.addPraise(praise)) {
            throw new IllegalArgumentException("该用户已点赞过");
        }
        return this.saveArticle(originArticle);
    }

    @Override
    public void removePraise(int articleId, int praiseId) {
        Article originArticle = articleRepository.findOne(articleId);
        originArticle.removePraise(praiseId);
        this.saveArticle(originArticle);
    }

    @Override
    public Page<Article> listArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Page<Article> listTop8LatestArticles() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(0, 8, sort);
        return articleRepository.findAll(pageRequest);
    }

    @Override
    public Page<Article> listTop8HotArticles() {
        Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "praiseSize","createTime");
        PageRequest pageRequest = new PageRequest(0, 8, sort);
        return articleRepository.findAll(pageRequest);
    }

    @Override
    public List<String> listTop8Tags() {
        List<Article> list = listTop8HotArticles().getContent();
        List<String> tagList = Lists.newArrayList();
        list.forEach(article -> {
            if(StringUtils.isNotEmpty(article.getTags())){
                tagList.addAll(Lists.newArrayList(article.getTags().split(",")));
            }
        });
        if(CollectionUtils.isEmpty(tagList)){
            return Lists.newArrayList();
        }
        return tagList.stream().limit(8).distinct().collect(Collectors.toList());
    }

    @Override
    public List<User> listTop8Users() {
        List<Article> articleList = listTop8HotArticles().getContent();
        return articleList.stream().map(Article::getUser).limit(8).distinct().collect(Collectors.toList());
    }
}
