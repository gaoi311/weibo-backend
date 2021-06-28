package com.buuble.blog.service;

import com.buuble.blog.entity.*;

import java.util.List;
import java.util.Map;


public interface UserService {

    User addUser(User user);

    User findUserById(Integer userId);

    User findUserByUsername(String username);

    Comment addComment(Comment comment);

    List<Comment> findCommentsByBlogId(Integer blogId);

    Integer commentCountByBlogIdAndIsSaw(Integer blogId, String isSaw);

    List<Map<String, Object>> getCommentsByBlogIdAndIsSaw(Integer blogId, String isSaw);

    Follow addFollow(Follow follow);

    boolean deleteFollowByUserIdAndOtherId(Integer userId, Integer otherId);

    Follow findFollowByUserIdAndOtherId(Integer userId, Integer otherId);

    Map<String, Object> getFollowsOtherByUserId(Integer userId);

    List<Map<String, Object>> getFollowsByOtherIdAndIsSaw(Integer otherId, String isSaw);

    Integer followCountByOtherIdAndIsSaw(Integer otherId, String isSaw);

}
