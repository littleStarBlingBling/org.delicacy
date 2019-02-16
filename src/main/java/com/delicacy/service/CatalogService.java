package com.delicacy.service;

import com.delicacy.domain.Catalog;
import com.delicacy.domain.User;

import java.util.List;

/**
 * @Author: MyDear
 */
public interface CatalogService {
    Catalog saveCatalog(Catalog catalog);

    void removeCatalog(int id );

    Catalog getCatalogById(int id);

    List<Catalog> listCatalogs(User user);
}
