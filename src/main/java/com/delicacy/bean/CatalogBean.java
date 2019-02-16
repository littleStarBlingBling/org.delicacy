package com.delicacy.bean;

import com.delicacy.domain.Catalog;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: MyDear
 */
@Data
public class CatalogBean implements Serializable {
    private static final long serialVersionUID = -3074813086447212412L;

    private String username;
    private Catalog catalog = new Catalog();
}
