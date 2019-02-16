package com.delicacy.service;

import com.delicacy.domain.Article;
import com.delicacy.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author: MyDear
 */
public interface ArticleService {

    Article saveArticle(Article article);

    void removeArticle(int id);

    Article getArticleById(int id);

    Page<Article> listArticleByTitleLikeAndSort(User user, String title, Pageable pageable);

    void readingIncrease(int id);

    Page<Article> listArticlesByKeyword(String keyword, Pageable pageable);

    Article createComment(int articleId, String commentContent);

    void removeComment(int articleId, int commentId);

    Article createPraise(int articleId);

    void removePraise(int articleId, int praiseId);

    Page<Article> listArticles(Pageable pageable);

    Page<Article> listTop8LatestArticles();

    Page<Article> listTop8HotArticles();

    List<String> listTop8Tags();

    List<User> listTop8Users();
}
