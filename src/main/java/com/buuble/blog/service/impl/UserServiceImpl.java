package com.buuble.blog.service.impl;

import com.buuble.blog.entity.*;
import com.buuble.blog.repository.*;
import com.buuble.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public User findUserById(Integer userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public List<Comment> findCommentsByBlogId(Integer blogId) {
        return commentRepository.findAllByBlogId(blogId);
    }

    @Override
    public Integer commentCountByBlogIdAndIsSaw(Integer blogId, String isSaw) {
        return commentRepository.countByBlogIdAndIsSaw(blogId, isSaw);
    }

    @Override
    public List<Map<String, Object>> getCommentsByBlogIdAndIsSaw(Integer blogId, String isSaw) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentsByBlogIdAndIsSaw(blogId, isSaw);
        for (Comment comment : comments) {
            Map<String, Object> commentInfo = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();
            Map<String, Object> blogInfo = new HashMap<>();
            result.add(commentInfo);
            commentInfo.put("user", userInfo);
            commentInfo.put("blog", blogInfo);
            commentInfo.put("comment", comment.getContent());   // 与接口不符合，只需要内容即可
            Blog blog = blogRepository.findBlogById(comment.getBlogId());
            User user = userRepository.findUserById(comment.getUserId());
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());
            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            comment.setIsSaw("1");
            commentRepository.save(comment);
        }
        return result;
    }

    @Override
    public Follow addFollow(Follow follow) {
        return followRepository.save(follow);
    }

    @Override
    public boolean deleteFollowByUserIdAndOtherId(Integer userId, Integer otherId) {
        followRepository.deleteFollowByUserIdAndOtherId(userId, otherId);
        return followRepository.findFollowByUserIdAndOtherId(userId, otherId) == null;
    }

    @Override
    public Follow findFollowByUserIdAndOtherId(Integer userId, Integer otherId) {
        return followRepository.findFollowByUserIdAndOtherId(userId, otherId);
    }

    @Override
    public Map<String, Object> getFollowsOtherByUserId(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        result.put("data", data);
        if (userRepository.findUserById(userId) == null) return null;
        List<Follow> follows = followRepository.findFollowsByUserId(userId);
        for (Follow follow : follows) {
            Map<String, Object> userInfo = new HashMap<>();
            data.add(userInfo);

            User user = userRepository.findUserById(follow.getOtherId());
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());
            userInfo.put("userIsFollowed", 1);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getFollowsByOtherIdAndIsSaw(Integer otherId, String isSaw) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Follow> follows = followRepository.findFollowsByOtherIdAndIsSaw(otherId, isSaw);
        for (Follow follow : follows) {
            Map<String, Object> followInfo = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();
            result.add(followInfo);
            followInfo.put("user", userInfo);
            User user = userRepository.findUserById(follow.getUserId());
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());
            follow.setIsSaw("1");
            followRepository.save(follow);
        }
        return result;
    }

    @Override
    public Integer followCountByOtherIdAndIsSaw(Integer otherId, String isSaw) {
        return followRepository.countByOtherIdAndIsSaw(otherId, isSaw);
    }
}
