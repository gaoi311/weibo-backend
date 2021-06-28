package com.buuble.blog.repository;

import com.buuble.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Integer countByBlogId(Integer blogId);

    Integer countByBlogIdAndIsSaw(Integer blogId, String isSaw);

    List<Comment> findAllByBlogId(Integer blogId);

    List<Comment> findCommentsByBlogIdAndIsSaw(Integer blogId, String isSaw);

    Comment save(Comment comment);
}
