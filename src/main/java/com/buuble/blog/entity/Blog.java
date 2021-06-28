package com.buuble.blog.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //表示主键自增
    private Integer id;
    private Integer userId;
    private Integer topicId;
    private String content;

    @LastModifiedDate
    private Date createdTime;
    private Integer likes;
    private String pictures;
}
