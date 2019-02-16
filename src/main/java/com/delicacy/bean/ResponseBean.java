package com.delicacy.bean;

import lombok.Data;

/**
 * @Author: MyDear
 */
@Data
public class ResponseBean {
    private boolean success;
    private String message;
    private Object body;

    public static ResponseBean of(boolean success, String message){
        ResponseBean responseBean = new ResponseBean();
        responseBean.setSuccess(success);
        responseBean.setMessage(message);
        return responseBean;
    }

    public static ResponseBean of(boolean success, String message, Object body){
        ResponseBean responseBean = of(success, message);
        responseBean.setBody(body);
        return responseBean;
    }
}
