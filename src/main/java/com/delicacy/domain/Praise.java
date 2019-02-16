package com.delicacy.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: MyDear
 */
@Data
@Entity
@Table(name = "delicacy_praise")
public class Praise implements Serializable {

    private static final long serialVersionUID = 4284906348614051645L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private User user;
    @CreationTimestamp
    private Date createTime;

    public static Praise of(User user){
        Praise praise = new Praise();
        praise.setUser(user);
        return praise;
    }
}
