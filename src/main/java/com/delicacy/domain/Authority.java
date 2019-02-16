package com.delicacy.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @Author: MyDear
 */
@Entity
@Table(name = "delicacy_authority")
@Data
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = -8098322350896471759L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int type;

    @Override
    public String getAuthority() {
        return name;
    }

    public static Authority of(int type){
        Authority authority = new Authority();
        authority.setType(type);
        return authority;
    }
}
