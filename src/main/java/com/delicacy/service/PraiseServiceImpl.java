package com.delicacy.service;

import com.delicacy.domain.Praise;
import com.delicacy.repository.PraiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Author: MyDear
 */
@Service
public class PraiseServiceImpl implements PraiseService {
    @Autowired
    private PraiseRepository praiseRepository;

    @Override
    public Praise getPraiseById(int id) {
        return praiseRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removePraise(int id) {
        praiseRepository.delete(id);
    }
}
