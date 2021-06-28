package com.buuble.blog.controller;

import com.buuble.blog.entity.Follow;
import com.buuble.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FollowHandler {
    @Autowired
    UserService userService;

    @PostMapping("/follow")
    public Map<String, Object> followOther(@RequestParam("userId") Integer userId,
                                           @RequestParam("otherId") Integer otherId) {
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
        if (userService.findUserById(otherId) == null) {
            msg = "要关注的用户不存在";
            status.put("msg", msg);
            return result;
        }
        if (userService.findFollowByUserIdAndOtherId(userId, otherId) != null) {
            msg = "已经关注该用户";
            status.put("msg", msg);
            return result;
        }
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setOtherId(otherId);
        follow.setIsSaw("0");
        Follow f2 = userService.addFollow(follow);
        if (f2 == null) {
            msg = "关注失败";
            status.put("msg", msg);
            return result;
        }

        code = 200;
        msg = "关注成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @DeleteMapping("/follow")
    public Map<String, Object> cancelFollow(@RequestParam("userId") Integer userId,
                                            @RequestParam("otherId") Integer otherId) {
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
        if (userService.findUserById(otherId) == null) {
            msg = "被关注的用户不存在";
            status.put("msg", msg);
            return result;
        }
        if (userService.findFollowByUserIdAndOtherId(userId, otherId) == null) {
            msg = "不存在该关注";
            status.put("msg", msg);
            return result;
        }
        boolean flag = userService.deleteFollowByUserIdAndOtherId(userId, otherId);
        if (!flag) {
            msg = "取消关注失败";
            status.put("msg", msg);
            return result;
        }

        code = 200;
        msg = "取消成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @GetMapping("follows/{userId}")
    Map<String, Object> getFollowsOtherByUserId(@PathVariable("userId") Integer userId) {
        Map<String, Object> result = userService.getFollowsOtherByUserId(userId);
        Map<String, Object> status = new HashMap<>();
        if (result == null) {
            result = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            result.put("data", data);
            status.put("code", 404);
            status.put("msg", "用户不存在");
        } else {
            status.put("code", 200);
            status.put("msg", "关注获取成功");
        }
        result.put("status", status);
        return result;
    }
}
