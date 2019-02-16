package com.delicacy.repository;

import com.delicacy.domain.Catalog;
import com.delicacy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: MyDear
 */
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
    List<Catalog> findByUser(User user);

    List<Catalog> findByUserAndName(User user, String name);
}
