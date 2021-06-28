package com.buuble.blog.repository;


import com.buuble.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface BlogRepository extends JpaRepository<Blog, Integer> {

    Blog findBlogById(Integer id);

    List<Blog> findBlogsByUserIdOrderByCreatedTimeDesc(Integer userId);

    List<Blog> findBlogsByTopicId(Integer topicId);

    int countBlogByTopicId(Integer topicId);

    @Transactional
    void deleteById(Integer id);

    //从Blog2表中查询所有已发布的博客Id
    @Query(value = "select id from Blog")
    int[] findAllId();

    Blog save(Blog blog);
}
