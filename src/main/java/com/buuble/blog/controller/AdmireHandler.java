package com.buuble.blog.controller;

import com.buuble.blog.entity.Blog;
import com.buuble.blog.entity.Admire;
import com.buuble.blog.service.BlogService;
import com.buuble.blog.service.AdmireService;
import com.buuble.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AdmireHandler {
    @Autowired
    private AdmireService admireService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @PostMapping("/like")
    public Map<String, Object> addLike(@RequestParam("userId") Integer userId,
                                       @RequestParam("blogId") Integer blogId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("data", data);
        result.put("status", status);
        status.put("code", code);

        if (userService.findUserById(userId) == null) {
            msg = "用户不存在";
            status.put("msg", msg);
            return result;
        }
        if (blogService.findBlogById(blogId) == null) {
            msg = "博客不存在";
            status.put("msg", msg);
            return result;
        }
        if (admireService.findByBlogIdAndUserId(blogId, userId) != null) {
            msg = "已经点赞";
            status.put("msg", msg);
            return result;
        }
        Admire admire = new Admire();
        admire.setUserId(userId);
        admire.setBlogId(blogId);
        admire.setIsSaw("0");
        Admire l2 = admireService.addLike(admire);
        if (l2 == null) {
            msg = "点赞失败";
            status.put("msg", msg);
            return result;
        }
        Blog blog = blogService.findBlogById(blogId);
        blog.setLikes(blog.getLikes() + 1);
        blogService.updateBlog(blog);
        code = 200;
        msg = "点赞成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @DeleteMapping("/like")
    public Map<String, Object> cancelLike(@RequestParam("userId") Integer userId,
                                          @RequestParam("blogId") Integer blogId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("data", data);
        result.put("status", status);
        status.put("code", code);

        if (userService.findUserById(userId) == null) {
            msg = "用户不存在";
            status.put("msg", msg);
            return result;
        }
        if (blogService.findBlogById(blogId) == null) {
            msg = "被点赞的博客不存在";
            status.put("msg", msg);
            return result;
        }
        if (admireService.findByBlogIdAndUserId(blogId, userId) == null) {
            msg = "用户还未点赞该博客";
            status.put("msg", msg);
            return result;
        }
        boolean flag = admireService.deleteByBlogIdAndUserId(blogId, userId);
        if (!flag) {
            msg = "取消点赞失败";
            status.put("msg", msg);
            return result;
        }
        Blog blog = blogService.findBlogById(blogId);
        blog.setLikes(blog.getLikes() - 1);
        blogService.updateBlog(blog);
        code = 200;
        msg = "取消成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }
}
