package com.buuble.blog.repository;

import com.buuble.blog.entity.Admire;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface AdmireRepository extends JpaRepository<Admire, Integer> {
    Integer countByBlogId(Integer blogId);

    @Transactional
    void deleteByBlogIdAndUserId(Integer blogId, Integer userId);

    Admire findByBlogIdAndUserId(Integer blogId, Integer userId);

    Admire save(Admire admire);
}
