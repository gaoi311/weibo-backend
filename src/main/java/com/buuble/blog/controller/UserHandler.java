package com.buuble.blog.controller;

import com.buuble.blog.entity.Blog;
import com.buuble.blog.entity.User;
import com.buuble.blog.service.BlogService;
import com.buuble.blog.service.CollectionService;
import com.buuble.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/myinfo/{userId}")
    public Map<String, Object> getMyInfoByUserId(@PathVariable("userId") Integer userId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("status", status);
        result.put("data", data);
        status.put("code", code);
        User user = userService.findUserById(userId);
        if (user == null) {
            msg = "用户不存在";
            status.put("msg", msg);
            return result;
        }
        code = 200;
        msg = "用户信息获取成功";
        status.put("code", code);
        status.put("msg", msg);
        data.put("userId", user.getId());
        data.put("userName", user.getUsername());
        data.put("userGender", user.getSex());
        data.put("userBirth", user.getBirth());
        data.put("userAvatar", user.getAvatar());
        return result;
    }

    @PutMapping("/myinfo/{userId}")
    public Map<String, Object> updateMyInfo(@PathVariable("userId") Integer userId,
                                            @RequestParam("userName") String userName,
                                            @RequestParam("userGender") String userGender,
                                            @RequestParam("userBirth") String userBirth) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("status", status);
        result.put("data", data);
        status.put("code", code);

        User user = userService.findUserById(userId);
        if (user == null) {
            msg = "用户不存在";
            status.put("msg", msg);
            return result;
        }
        if (userName.length() < 6) {
            msg = "用户名长度至少为6个字符";
            status.put("msg", msg);
            return result;
        }
        if (!userName.matches("[a-zA-Z][0-9a-zA-Z]+")) {
            msg = "用户名只能由字母开头，后跟字母或数字5个以上";
            status.put("msg", msg);
            return result;
        }
        if (!(userGender.equals("男") || userGender.equals("女") || userGender.equals("1") || userGender.equals("2"))) {
            msg = "性别只能为男或女";
            status.put("msg", msg);
            return result;
        }
        Date date;
        if (userBirth.isEmpty()) date = null;
        else date = Date.valueOf(userBirth);
        user.setId(userId);
        user.setUsername(userName);
        user.setSex(userGender);
        user.setBirth(date);
        User u2 = userService.addUser(user);
        if (u2 == null) {
            msg = "用户信息修改失败";
            status.put("msg", msg);
            return result;
        }
        code = 200;
        msg = "修改成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

//    @PutMapping("/avatar/{userId}")
//    public Map<String, Object> updateAvatar(@PathVariable("userId") Integer userId, @RequestParam("userAvatar") MultipartFile userAvatar) {
//        Map<String, Object> status = new HashMap<>();
//        String originalName = userAvatar.getOriginalFilename();
//        String[] s = originalName.split("\\.");
//        System.out.println(s.length);
//        String backName = s[s.length-1];
//        File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\static\\avatar\\"+userId+"."+backName);
//        try {
//            userAvatar.transferTo(file);
//            User user = userService.findUserById(userId);
//            user.setAvatar("avatar/"+userId+"."+backName);
//            userService.addUser(user);
//            status.put("code",200);
//            status.put("msg","上传成功");
//        } catch (IOException e) {
//            status.put("code",404);
//            status.put("msg","上传失败");
//            e.printStackTrace();
//        }
//        Map<String, Object> result = new HashMap<>();
//        result.put("status",status);
//        return result;
//    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("data", data);
        result.put("status", status);
        status.put("code", code);
        if (userName.length() < 6) {
            msg = "用户名长度至少为6个字符";
            status.put("msg", msg);
            return result;
        }
        if (!userName.matches("[a-zA-Z][0-9a-zA-Z]+")) {
            msg = "用户名只能由字母开头，后跟字母或数字5个以上";
            status.put("msg", msg);
            return result;
        }
        if (userPassword.length() < 6) {
            msg = "密码长度至少为6个字符";
            status.put("msg", msg);
            return result;
        }
        if (userPassword.matches("[^a-zA-Z]*")) {
            msg = "密码必须包含至少一个字母";
            status.put("msg", msg);
            return result;
        }
        User user2 = userService.findUserByUsername(userName);
        if (user2 != null) {
            msg = "该用户名已存在";
            status.put("msg", msg);
            return result;
        }

        User user = new User();
        user.setUsername(userName);
        user.setPassword(userPassword);
        user.setAvatar("avatar/default.jpg");
        user.setIsBlocked("0");
        user.setBirth(new Date(System.currentTimeMillis()));
        user.setSex("男");
        user2 = userService.addUser(user);
        if (user2 == null) {
            msg = "用户添加失败";
            status.put("msg", msg);
            return result;
        }
        code = 200;
        msg = "注册成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam("userName") String userName,
                                     @RequestParam("userPassword") String userPassword) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        Integer code = 404;
        String msg;
        result.put("status", status);
        result.put("data", data);
        status.put("code", code);
        data.put("userToken", "123.244.453400");
        data.put("user", userInfo);

        User user = userService.findUserByUsername(userName);
        if (user == null) {
            msg = "用户不存在";
            status.put("msg", msg);
            return result;
        }
        if (!user.getPassword().equals(userPassword)) {
            msg = "密码错误";
            status.put("msg", msg);
            return result;
        }
        if (Integer.parseInt(user.getIsBlocked()) == 1) {
            msg = "您已被封禁！";
            status.put("msg", msg);
            return result;
        }
        code = 200;
        msg = "登录成功";
        status.put("code", code);
        status.put("msg", msg);
        userInfo.put("userId", user.getId());
        userInfo.put("userName", user.getUsername());
        userInfo.put("userAvatar", user.getAvatar());
        return result;
    }

    @GetMapping("/noticescount/{userId}")
    public Map<String, Object> getNoticesCountByUserId(@PathVariable("userId") Integer userId) {
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
        Integer noticesCount = 0;
        noticesCount += userService.followCountByOtherIdAndIsSaw(userId, "0");
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        for (Blog blog : blogs) {
            noticesCount += userService.commentCountByBlogIdAndIsSaw(blog.getId(), "0");
            noticesCount += collectionService.countByBlogIdAndIsSaw(blog.getId(), "0");
        }
        data.put("noticesCount", noticesCount);
        code = 200;
        msg = "获取成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }

    @GetMapping("/unsawnotices/{userId}")
    public Map<String, Object> getUnSawNoticesByUserId(@PathVariable("userId") Integer userId) {
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
        List<Map<String, Object>> blogFollowNotices = userService.getFollowsByOtherIdAndIsSaw(userId, "0");
        data.put("blogFollowNotices", blogFollowNotices);
        List<Map<String, Object>> blogCollectionNotices = new ArrayList<>();
        List<Map<String, Object>> blogCommentNotices = new ArrayList<>();
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        for (Blog blog : blogs) {
            Integer blogId = blog.getId();
            List<Map<String, Object>> tempBlogCollectionNotices = collectionService.getCollectionsByBlogIdAndIsSaw(blogId, "0");
            for (Map<String, Object> notice : tempBlogCollectionNotices) {
                blogCollectionNotices.add(notice);
            }
            List<Map<String, Object>> tempBlogCommentNotices = userService.getCommentsByBlogIdAndIsSaw(blogId, "0");
            for (Map<String, Object> notice : tempBlogCommentNotices) {
                blogCommentNotices.add(notice);
            }
        }
        data.put("blogCollectionNotices", blogCollectionNotices);
        data.put("blogCommentNotices", blogCommentNotices);
        code = 200;
        msg = "获取成功";
        status.put("code", code);
        status.put("msg", msg);
        return result;
    }
}
