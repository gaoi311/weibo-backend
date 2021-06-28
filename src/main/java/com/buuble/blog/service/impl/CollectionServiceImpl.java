package com.buuble.blog.service.impl;

import com.buuble.blog.entity.*;
import com.buuble.blog.repository.*;
import com.buuble.blog.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AdmireRepository admireRepository;

    @Override
    public Collection addCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    @Override
    public boolean deleteCollection(Integer userId, Integer blogId) {
        collectionRepository.deleteByBlogIdAndUserId(blogId, userId);
        return collectionRepository.findByBlogIdAndUserId(blogId, userId) == null;
    }

    @Override
    public Collection findByBlogIdAndUserId(Integer blogId, Integer userId) {
        return collectionRepository.findByBlogIdAndUserId(blogId, userId);
    }

    @Override
    public Map<String, Object> getCollectionsByUserId(Integer userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        result.put("data", data);
        if (userRepository.findUserById(userId) == null) return null;
        List<Collection> collections = collectionRepository.findCollectionsByUserId(userId);
        for (Collection collection : collections) {
            Map<String, Object> dataItem = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();
            Map<String, Object> blogInfo = new HashMap<>();
            data.add(dataItem);
            dataItem.put("user", userInfo);
            dataItem.put("blog", blogInfo);

            Blog blog = blogRepository.findBlogById(collection.getBlogId());
            User user = userRepository.findUserById(blog.getUserId());
            Topic topic = topicRepository.findTopicById(blog.getTopicId());
            Collection collection2 = collectionRepository.findByBlogIdAndUserId(blog.getId(), userId);
            Admire likeTable = admireRepository.findByBlogIdAndUserId(blog.getId(), userId);
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());

            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            blogInfo.put("blogTopicId", topic.getId());
            blogInfo.put("blogTopicName", topic.getName());
            Integer likes = admireRepository.countByBlogId(blog.getId());
            blog.setLikes(likes);
            blogRepository.save(blog);
            blogInfo.put("blogLikes", likes);
            blogInfo.put("blogCommentsCount", commentRepository.countByBlogId(blog.getId()));
            blogInfo.put("blogCollectionsCount", collectionRepository.countByBlogId(blog.getId()));
            blogInfo.put("blogPictures", blog.getPictures());
            blogInfo.put("blogCreatedTime", blog.getCreatedTime());
            blogInfo.put("blogIsLiked", likeTable == null ? 0 : 1);
            blogInfo.put("blogIsCollected", collection2 == null ? 0 : 1);
        }
        return result;
    }

    @Override
    public Integer countByBlogIdAndIsSaw(Integer blogId, String isSaw) {
        return collectionRepository.countByBlogIdAndIsSaw(blogId, isSaw);
    }

    @Override
    public List<Map<String, Object>> getCollectionsByBlogIdAndIsSaw(Integer blogId, String isSaw) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Collection> collections = collectionRepository.findCollectionsByBlogIdAndIsSaw(blogId, isSaw);
        for (Collection collection : collections) {
            Map<String, Object> collectionInfo = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();
            Map<String, Object> blogInfo = new HashMap<>();
            result.add(collectionInfo);
            collectionInfo.put("user", userInfo);
            collectionInfo.put("blog", blogInfo);
            Blog blog = blogRepository.findBlogById(collection.getBlogId());
            User user = userRepository.findUserById(collection.getUserId());
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());
            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            collection.setIsSaw("1");
            collectionRepository.save(collection);
        }
        return result;
    }
}
