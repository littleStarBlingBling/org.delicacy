package com.delicacy.repository;

import com.delicacy.domain.Article;
import com.delicacy.domain.Catalog;
import com.delicacy.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @Author: MyDear
 */
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Page<Article> findByUserAndTitleLike(User user, String title, Pageable pageable);
    
    Page<Article> findByTitleLike(String keyword, Pageable pageable);

    Page<Article> findByUser(User user, Pageable pageable);
}
