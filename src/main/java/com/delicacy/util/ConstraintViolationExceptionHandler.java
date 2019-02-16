package com.delicacy.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: MyDear
 */
public class ConstraintViolationExceptionHandler {
    public static String getMessage(ConstraintViolationException e){
        List<String> msgList =  e.getConstraintViolations().stream().map(constraintViolation -> constraintViolation.getMessage()).collect(Collectors.toList());
        return StringUtils.join(msgList.toArray(), ";");
    }
}
