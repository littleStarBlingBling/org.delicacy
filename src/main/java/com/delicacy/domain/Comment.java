package com.delicacy.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: MyDear
 */
@Entity
@Table(name = "delicacy_comment")
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = -4470964200321818741L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    private Date createTime;

    public static Comment of(User user, String content){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        return comment;
    }
}
