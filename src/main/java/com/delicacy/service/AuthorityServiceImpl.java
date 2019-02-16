package com.delicacy.service;

;
import com.delicacy.domain.Authority;
import com.delicacy.repository.AuthorityRepository;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: MyDear
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(int id) {
        return authorityRepository.findOne(id);
    }

}
