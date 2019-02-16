package com.delicacy.service;

import com.delicacy.domain.Catalog;
import com.delicacy.domain.User;
import com.delicacy.enums.ErrorCodeEnum;
import com.delicacy.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: MyDear
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
        if (!CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ErrorCodeEnum.CATALOG_EXIST.getMessage());
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(int id) {
        catalogRepository.delete(id);
    }

    @Override
    public Catalog getCatalogById(int id) {
        return catalogRepository.findOne(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
