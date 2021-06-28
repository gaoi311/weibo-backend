package com.buuble.blog.controller;

import com.buuble.blog.entity.Collection;
import com.buuble.blog.service.BlogService;
import com.buuble.blog.service.CollectionService;
import com.buuble.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CollectionHandler {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @PostMapping("/collection")
    public Map<String, Object> addCollection(@RequestParam("userId") Integer userId,
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
        if (collectionService.findByBlogIdAndUserId(blogId, userId) != null) {
            msg = "已经收藏该博客";
            status.put("msg", msg);
            return result;
        }
        Collection collection = new Collection();
        collection.setUserId(userId);
        collection.setBlogId(blogId);
        collection.setIsSaw("0");
        Collection c2 = collectionService.addCollection(collection);
        if (c2 == null) {
            msg = "收藏失败";
            status.put("msg", msg);
            return result;
        }

        code = 200;
        msg = "收藏成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @DeleteMapping("/collection")
    public Map<String, Object> cancelCollection(@RequestParam("userId") Integer userId,
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
            msg = "被收藏的博客不存在";
            status.put("msg", msg);
            return result;
        }
        if (collectionService.findByBlogIdAndUserId(blogId, userId) == null) {
            msg = "用户还未收藏该博客";
            status.put("msg", msg);
            return result;
        }
        boolean flag = collectionService.deleteCollection(userId, blogId);
        if (!flag) {
            msg = "取消收藏失败";
            status.put("msg", msg);
            return result;
        }

        code = 200;
        msg = "取消成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @GetMapping("/collections/{userId}")
    Map<String, Object> getCollectionsByUserId(@PathVariable("userId") Integer userId) {
        Map<String, Object> result = collectionService.getCollectionsByUserId(userId);
        Map<String, Object> status = new HashMap<>();
        if (result == null) {
            result = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            result.put("data", data);
            status.put("code", 404);
            status.put("msg", "用户不存在");
        } else {
            status.put("code", 200);
            status.put("msg", "获取成功");
        }
        result.put("status", status);
        return result;
    }
}
