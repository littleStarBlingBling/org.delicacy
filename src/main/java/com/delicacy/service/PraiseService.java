package com.delicacy.service;

import com.delicacy.domain.Praise;

/**
 * @Author: MyDear
 */
public interface PraiseService {
    Praise getPraiseById(int id);

    void removePraise(int id);
}
