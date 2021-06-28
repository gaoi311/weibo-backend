package com.buuble.blog.service;

import com.buuble.blog.entity.Admire;

public interface AdmireService {
    Admire addLike(Admire admire);

    boolean deleteByBlogIdAndUserId(Integer blogId, Integer userId);

    Admire findByBlogIdAndUserId(Integer blogId, Integer userId);
}
