package com.delicacy.domain;

import com.delicacy.bean.CatalogBean;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: MyDear
 */
@Entity
@Table(name = "delicacy_catalog")
@Data
public class Catalog implements Serializable {

    private static final long serialVersionUID = -1357242042326590523L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Catalog of(CatalogBean catalogBean){
        Catalog catalog = new Catalog();
        if(catalogBean.getCatalog() == null){
            System.out.println("分类实体为空！");
        }else {
            catalog.setName(catalogBean.getCatalog().name);
        }

        return catalog;
    }
}
