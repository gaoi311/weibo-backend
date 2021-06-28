package com.buuble.blog.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //表示主键自增
    private Integer id;
    private Integer userId;
    private Integer otherId;
    private String isSaw;
}
