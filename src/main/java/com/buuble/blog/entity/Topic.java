package com.buuble.blog.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //表示主键自增
    private Integer id;

    @Column(unique = true)
    private String name;

    @LastModifiedDate
    private Date createdTime;
}
