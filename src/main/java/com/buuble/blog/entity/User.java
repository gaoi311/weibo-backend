package com.buuble.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data //lombok中的注解,自动生成getter、setter方法
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id //主键前加此注解
    @GeneratedValue(strategy = GenerationType.IDENTITY) //表示主键自增
    private Integer id;
    private String password;
    private String sex;
    private Date birth;
    private String avatar;
    private String isBlocked;

    @Column(unique = true)
    private String username;
}
