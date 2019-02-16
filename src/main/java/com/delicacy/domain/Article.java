package com.delicacy.domain;

import com.github.rjeschke.txtmark.Processor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @Author: MyDear
 */
@Entity
@Table(name = "delicacy_article")
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 7584319957513085527L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "标题不能为空")
    private String title;
    @NotEmpty(message = "餐厅不能为空")
    private String canteen;
    @NotEmpty(message = "地址不能为空")
    private String address;
    @NotEmpty(message = "价格不能为空")
    private String price;
    @NotEmpty(message = "食物不能为空")
    private String food;
    @NotEmpty(message = "摘要不能为空")
    private String summary;
    @NotEmpty(message = "正文不能为空")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;
    @NotEmpty(message = "正文不能为空，或者只有图片")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String htmlContent;
    @CreationTimestamp
    private Date createTime;
    private int readSize;
    private int commentSize;
    private int praiseSize;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "article_comment",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> commentList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "article_praise",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "praise_id", referencedColumnName = "id"))
    private List<Praise> praiseList;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;
    private String tags;

    public void setContent(String content){
        this.content = content;
        this.htmlContent = Processor.process(content);
    }

    public void setComments(List<Comment> commentList) {
        this.commentList = commentList;
        this.commentSize = this.commentList.size();
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        this.commentSize = this.commentList.size();
    }

    public void removeComment(int commentId) {
        for (int i = 0; i < this.commentList.size(); i++) {
            if (commentList.get(i).getId() == commentId) {
                this.commentList.remove(i);
                break;
            }
        }
        this.commentSize = this.commentList.size();
    }

    public boolean addPraise(Praise praise) {
        boolean isExist = false;
        for (int i = 0; i < this.praiseList.size(); i++) {
            if (this.praiseList.get(i).getUser().getId() == praise.getUser().getId()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            this.praiseList.add(praise);
            this.praiseSize = this.praiseList.size();
        }
        return isExist;
    }

    public void removePraise(int praiseId) {
        for (int i = 0; i < this.praiseList.size(); i++) {
            if (this.praiseList.get(i).getId() == praiseId) {
                this.praiseList.remove(i);
                break;
            }
        }
        this.praiseSize = this.praiseList.size();
    }

    public void setPraiseList(List<Praise> praiseList) {
        this.praiseSize = praiseList.size();
        this.praiseList = praiseList;
    }
}
