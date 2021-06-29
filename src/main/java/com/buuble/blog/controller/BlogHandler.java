package com.buuble.blog.controller;

import com.buuble.blog.entity.Blog;
import com.buuble.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BlogHandler {
    @Autowired
    private BlogService blogService;

    @GetMapping("/blog/{blogId}")
    public Map<String, Object> getBlog(@PathVariable("blogId") Integer blogId, @RequestParam("userId") Integer userId) {
        Map<String, Object> map = blogService.findBlogById(blogId, userId);
        Map<String, Object> status = new HashMap<>();
        if (map != null) {
            status.put("code", 200);
            status.put("msg", "获取成功");
            map.put("status", status);
            return map;
        } else {
            status.put("code", 404);
            status.put("msg", "获取失败");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", status);
            return map1;
        }
    }

    @GetMapping("/indexblogs")
    public Map<String, Object> getFirstPageBlogs(@RequestParam("userId") Integer userId) {
        Map<String, Object> map = blogService.getFirstPageBlogs(userId);
        Map<String, Object> status = new HashMap<>();
        if (map != null) {
            status.put("code", 200);
            status.put("msg", "获取成功");
            map.put("status", status);
            return map;
        } else {
            status.put("code", 404);
            status.put("msg", "获取失败");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", status);
            return map1;
        }
    }

    @PostMapping("/blog")  //添加博客
    public Map<String, Object> addBlog(@RequestParam("userId") Integer userId,
                                       @RequestParam("blogTopic") Integer blogTopic,
                                       @RequestParam("blogContent") String blogContent,
                                       @RequestParam("file") MultipartFile[] file) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("status", status);
        result.put("data", data);
        status.put("code", code);
        if (blogContent.isEmpty()) {
            msg = "微博内容不能为空";
            status.put("msg", msg);
            return result;
        }


        Blog blog = blogService.addBlog(userId, blogTopic, blogContent, file);
        if (blog == null) {
            msg = "不存在该用户或者该话题";
            status.put("msg", msg);
            return result;
        }
        code = 200;
        msg = "发表成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @DeleteMapping("/blog/{blogId}")  //删除博客
    public Map<String, Object> deleteBlog(@PathVariable("blogId") Integer blogId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        result.put("status", status);
        result.put("data", new HashMap<>());
        status.put("code", 404);
        if (blogService.findBlogById(blogId) == null) {
            status.put("msg", "要删除的用户不存在");
            return result;
        }
        boolean flag = blogService.deleteBlog(blogId);
        if (flag) {
            status.put("code", 200);
            status.put("msg", "删除成功");
        } else {
            status.put("msg", "删除失败");
        }
        return result;
    }

    @GetMapping("/blogs/{userId}")
    public Map<String, Object> getBlogsByUserId(@PathVariable("userId") Integer userId, @RequestParam("myId") Integer myId) {
        Map<String, Object> result = blogService.getBlogsByUserId(userId, myId);
        Map<String, Object> status = new HashMap<>();
        if (result == null) {
            result = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            result.put("data", data);
            status.put("code", 404);
            status.put("msg", "用户不存在或者自己ID错误");
        } else {
            status.put("code", 200);
            status.put("msg", "获取成功");
        }
        result.put("status", status);
        return result;
    }

    @GetMapping("/topicblogs/{topicId}")
    public Map<String, Object> getBlogsByTopicId(@PathVariable("topicId") Integer topicId, @RequestParam("userId") Integer userId) {
        Map<String, Object> result = blogService.getBlogsByTopicId(topicId, userId);
        Map<String, Object> status = new HashMap<>();
        if (result == null) {
            result = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            result.put("data", data);
            status.put("code", 404);
            status.put("msg", "话题不存在");
        } else {
            status.put("code", 200);
            status.put("msg", "成功");
        }
        result.put("status", status);
        return result;
    }

    @GetMapping("/search")
    public Map<String, Object> searchBlogs(@RequestParam("userId") Integer userId, @RequestParam("key") String key) {
        Map<String, Object> map = blogService.searchBlogs(userId, key);
        Map<String, Object> status = new HashMap<>();
        if (map != null) {
            status.put("code", 200);
            status.put("msg", "获取成功");
            map.put("status", status);
            return map;
        } else {
            status.put("code", 404);
            status.put("msg", "获取失败");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", status);
            List<Map<String, Object>> data = new ArrayList<>();
            map1.put("data", data);
            return map1;
        }
    }
}
