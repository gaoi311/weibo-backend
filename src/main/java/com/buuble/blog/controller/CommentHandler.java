package com.buuble.blog.controller;

import com.buuble.blog.entity.Comment;
import com.buuble.blog.entity.User;
import com.buuble.blog.service.BlogService;
import com.buuble.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/comments/{blogId}")
    public Map<String, Object> getCommentsByBlogId(@PathVariable("blogId") Integer blogId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> status = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("data", data);
        result.put("status", status);
        status.put("code", code);
        if (blogService.findBlogById(blogId) == null) {
            msg = "博客不存在";
            status.put("msg", msg);
            return result;
        }
        List<Comment> comments = userService.findCommentsByBlogId(blogId);
        for (Comment comment : comments) {
            Map<String, Object> dataItem = new HashMap<>();
            User user = userService.findUserById(comment.getUserId());
            Map<String, Object> userInfo = new HashMap<>();
            data.add(dataItem);
            dataItem.put("user", userInfo);
            dataItem.put("comment", comment.getContent());
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());
        }
        code = 200;
        msg = "成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @PostMapping("/comment")
    public Map<String, Object> publishComment(@RequestParam("userId") Integer userId,
                                              @RequestParam("blogId") Integer blogId,
                                              @RequestParam("comment") String commentContent) {
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
        if (commentContent == null || commentContent.isEmpty()) {
            msg = "评论为空";
            status.put("msg", msg);
            return result;
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setBlogId(blogId);
        comment.setContent(commentContent);
        comment.setCreatedTime(new Date());
        comment.setIsSaw("0");
        Comment c2 = userService.addComment(comment);
        if (c2 == null) {
            msg = "评论失败";
            status.put("msg", msg);
            return result;
        }
        code = 200;
        msg = "评论成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }
}
