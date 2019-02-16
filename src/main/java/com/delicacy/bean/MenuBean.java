package com.delicacy.bean;

import lombok.Data;

/**
 * @Author: MyDear
 */
@Data
public class MenuBean {
    private String name;
    private String url;
    public static MenuBean of(String name, String url){
        MenuBean menuBean = new MenuBean();
        menuBean.setName(name);
        menuBean.setUrl(url);
        return menuBean;
    }
}
