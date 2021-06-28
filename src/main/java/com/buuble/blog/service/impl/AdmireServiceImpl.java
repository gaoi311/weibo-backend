package com.buuble.blog.service.impl;

import com.buuble.blog.entity.Admire;
import com.buuble.blog.repository.AdmireRepository;
import com.buuble.blog.service.AdmireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdmireServiceImpl implements AdmireService {
    @Autowired
    AdmireRepository admireRepository;

    @Override
    public Admire addLike(Admire admire) {
        return admireRepository.save(admire);
    }

    @Override
    public boolean deleteByBlogIdAndUserId(Integer blogId, Integer userId) {
        admireRepository.deleteByBlogIdAndUserId(blogId, userId);
        return admireRepository.findByBlogIdAndUserId(blogId, userId) == null;
    }

    @Override
    public Admire findByBlogIdAndUserId(Integer blogId, Integer userId) {
        return admireRepository.findByBlogIdAndUserId(blogId, userId);
    }
}
