package com.buuble.blog.service;

import com.buuble.blog.entity.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Map<String, Object> getFirstPageBlogs(Integer userId);

    Blog addBlog(Integer userId, Integer blogTopic, String blogContent, MultipartFile[] blogPictures);

    Blog updateBlog(Blog blog);

    boolean deleteBlog(Integer blogId);

    Blog findBlogById(Integer blogId);

    List<Blog> findBlogsByUserId(Integer userId);

    Map<String, Object> getBlogsByUserId(Integer userId, Integer myId);

    Map<String, Object> getBlogsByTopicId(Integer topicId, Integer userId);

    Map<String, Object> findBlogById(Integer blogId, Integer userId);

    Map<String, Object> searchBlogs(Integer userId, String key);
}
