package com.buuble.blog.service.impl;

import com.buuble.blog.entity.*;
import com.buuble.blog.entity.Collection;
import com.buuble.blog.repository.*;
import com.buuble.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    AdmireRepository admireRepository;

    @Autowired
    FollowRepository followRepository;

    @Override
    public Map<String, Object> findBlogById(Integer blogId, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        Blog blog = blogRepository.findBlogById(blogId);
        User user = userRepository.findUserById(blog.getUserId());
        Topic topic = topicRepository.findTopicById(blog.getTopicId());
        Collection collection = collectionRepository.findByBlogIdAndUserId(blogId, userId);
        Admire likeTable = admireRepository.findByBlogIdAndUserId(blogId, userId);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> blogInfo = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("userId", user.getId());
        userInfo.put("userName", user.getUsername());
        userInfo.put("userAvatar", user.getAvatar());

        blogInfo.put("blogId", blog.getId());
        blogInfo.put("blogContent", blog.getContent());
        blogInfo.put("blogTopicId", topic.getId());
        blogInfo.put("blogTopicName", topic.getName());
        Integer likes = admireRepository.countByBlogId(blogId);
        blogInfo.put("blogLikes", likes);
        blogInfo.put("blogCommentsCount", commentRepository.countByBlogId(blogId));
        blogInfo.put("blogCollectionsCount", collectionRepository.countByBlogId(blogId));
        blogInfo.put("blogPictures", blog.getPictures());
        blogInfo.put("blogCreatedTime", blog.getCreatedTime());
        blogInfo.put("blogIsLiked", likeTable == null ? 0 : 1);
        blogInfo.put("blogIsCollected", collection == null ? 0 : 1);

        data.put("user", userInfo);
        data.put("blog", blogInfo);

        map.put("data", data);
        return map;
    }

    @Override
    public Map<String, Object> getFirstPageBlogs(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        List<Object> data = new ArrayList<>();
        int a[] = blogRepository.findAllId();
        Random random = new Random();
        Set<Integer> b = new HashSet<>();
        while (b.size() < 10 && b.size() < a.length) {
            b.add(a[random.nextInt(a.length)]);
        }
        for (Integer blogId : b) {
            Blog blog = blogRepository.findBlogById(blogId);
            User user = userRepository.findUserById(blog.getUserId());
            Topic topic = topicRepository.findTopicById(blog.getTopicId());
            Collection collection = collectionRepository.findByBlogIdAndUserId(blogId, userId);
            Admire likeTable = admireRepository.findByBlogIdAndUserId(blogId, userId);
            Map<String, Object> dataItem = new HashMap<>();
            Map<String, Object> blogInfo = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();

            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());

            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            blogInfo.put("blogTopicId", topic.getId());
            blogInfo.put("blogTopicName", topic.getName());
            Integer likes = admireRepository.countByBlogId(blogId);
            blogInfo.put("blogLikes", likes);
            blogInfo.put("blogCommentsCount", commentRepository.countByBlogId(blogId));
            blogInfo.put("blogCollectionsCount", collectionRepository.countByBlogId(blogId));
            blogInfo.put("blogPictures", blog.getPictures());
            blogInfo.put("blogCreatedTime", blog.getCreatedTime());
            blogInfo.put("blogIsLiked", likeTable == null ? 0 : 1);
            blogInfo.put("blogIsCollected", collection == null ? 0 : 1);

            dataItem.put("user", userInfo);
            dataItem.put("blog", blogInfo);
            data.add(dataItem);
        }
        map.put("data", data);
        return map;
    }

    @Override
    public Blog addBlog(Integer userId, Integer blogTopic, String blogContent, MultipartFile[] blogPictures) {
        User user = userRepository.findUserById(userId);
        if (user == null) return null;
        Topic topic = topicRepository.findTopicById(blogTopic);
        if (topic == null) return null;

        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTopicId(blogTopic);
        blog.setContent(blogContent);
        blog.setCreatedTime(new Date());
        blog.setLikes(0);
        blogRepository.save(blog);
        String picturePaths = "";
        int i = 1;
        for(MultipartFile blogPicture : blogPictures){
            String originalName = blogPicture.getOriginalFilename();
            String[] s = originalName.split("\\.");
            String backName = s[s.length-1];
            File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\static\\blogImages\\"+blog.getId()+"_"+i+"."+backName);
            try {
                blogPicture.transferTo(file);
                if(i == 1) picturePaths += "blogImages/"+blog.getId()+"_"+i+"."+backName;
                else picturePaths += "|blogImages/"+blog.getId()+"_"+i+"."+backName;
                i++;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        if(i<=blogPictures.length) return null;

        blog.setPictures(picturePaths);
        Blog b2 = blogRepository.save(blog);
        return b2;
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public boolean deleteBlog(Integer blogId) {
        blogRepository.deleteById(blogId);
        return blogRepository.findBlogById(blogId) == null;
    }

    @Override
    public Blog findBlogById(Integer blogId) {
        return blogRepository.findBlogById(blogId);
    }

    @Override
    public List<Blog> findBlogsByUserId(Integer userId) {
        return blogRepository.findBlogsByUserIdOrderByCreatedTimeDesc(userId);
    }

    @Override
    public Map<String, Object> getBlogsByUserId(Integer userId, Integer myId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> blogsInfo = new ArrayList<>();
        Map<String, Object> userInfo = new HashMap<>();
        result.put("data", data);
        data.put("user", userInfo);
        data.put("blogs", blogsInfo);

        User user = userRepository.findUserById(userId);
        if (user == null || userRepository.findUserById(myId) == null) return null;
        userInfo.put("userId", user.getId());
        userInfo.put("userName", user.getUsername());
        userInfo.put("userAvatar", user.getAvatar());
        userInfo.put("userIsFollowed", followRepository.findFollowByUserIdAndOtherId(myId, userId) == null ? 0 : 1);
        List<Blog> blogs = blogRepository.findBlogsByUserIdOrderByCreatedTimeDesc(userId);
        for (Blog blog : blogs) {
            Map<String, Object> blogInfo = new HashMap<>();
            blogsInfo.add(blogInfo);
            Topic topic = topicRepository.findTopicById(blog.getTopicId());
            Collection collection = collectionRepository.findByBlogIdAndUserId(blog.getId(), myId);
            Admire likeTable = admireRepository.findByBlogIdAndUserId(blog.getId(), myId);

            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            blogInfo.put("blogTopicId", topic.getId());
            blogInfo.put("blogTopicName", topic.getName());
            Integer likes = admireRepository.countByBlogId(blog.getId());
            blogInfo.put("blogLikes", likes);
            blogInfo.put("blogCommentsCount", commentRepository.countByBlogId(blog.getId()));
            blogInfo.put("blogCollectionsCount", collectionRepository.countByBlogId(blog.getId()));
            blogInfo.put("blogPictures", blog.getPictures());
            blogInfo.put("blogCreatedTime", blog.getCreatedTime());
            blogInfo.put("blogIsLiked", likeTable == null ? 0 : 1);
            blogInfo.put("blogIsCollected", collection == null ? 0 : 1);
        }
        return result;
    }

    @Override
    public Map<String, Object> getBlogsByTopicId(Integer topicId, Integer userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        result.put("data", data);

        if (topicRepository.findTopicById(topicId) == null) return null;
        List<Blog> blogs = blogRepository.findBlogsByTopicId(topicId);
        for (Blog blog : blogs) {
            User user = userRepository.findUserById(blog.getUserId());
            Topic topic = topicRepository.findTopicById(blog.getTopicId());
            Collection collection = collectionRepository.findByBlogIdAndUserId(blog.getId(), userId);
            Admire likeTable = admireRepository.findByBlogIdAndUserId(blog.getId(), userId);
            Map<String, Object> dataItem = new HashMap<>();
            Map<String, Object> blogInfo = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();

            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());

            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            blogInfo.put("blogTopicId", topic.getId());
            blogInfo.put("blogTopicName", topic.getName());
            Integer likes = admireRepository.countByBlogId(blog.getId());
            blogInfo.put("blogLikes", likes);
            blogInfo.put("blogCommentsCount", commentRepository.countByBlogId(blog.getId()));
            blogInfo.put("blogCollectionsCount", collectionRepository.countByBlogId(blog.getId()));
            blogInfo.put("blogPictures", blog.getPictures());
            blogInfo.put("blogCreatedTime", blog.getCreatedTime());
            blogInfo.put("blogIsLiked", likeTable == null ? 0 : 1);
            blogInfo.put("blogIsCollected", collection == null ? 0 : 1);

            dataItem.put("user", userInfo);
            dataItem.put("blog", blogInfo);
            data.add(dataItem);
        }
        return result;
    }

    @Override
    public Map<String, Object> searchBlogs(Integer userId, String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        List<Object> data = new ArrayList<>();
        map.put("data", data);
        int ids[] = blogRepository.findAllId();
        List<Integer> idsByKey = new ArrayList<>();
        for (Integer blogId : ids) {
            Blog blog = blogRepository.findBlogById(blogId);
            Topic topic = topicRepository.findTopicById(blog.getTopicId());
            if (blog.getContent().indexOf(key) != -1 || key.indexOf(blog.getContent()) != -1 || topic.getName().indexOf(key) != -1 || key.indexOf(topic.getName()) != -1) {
                idsByKey.add(blogId);
            }
        }

        for (Integer blogId : idsByKey) {
            Blog blog = blogRepository.findBlogById(blogId);
            User user = userRepository.findUserById(blog.getUserId());
            Topic topic = topicRepository.findTopicById(blog.getTopicId());
            Collection collection = collectionRepository.findByBlogIdAndUserId(blogId, userId);
            Admire likeTable = admireRepository.findByBlogIdAndUserId(blogId, userId);
            Map<String, Object> dataItem = new HashMap<>();
            Map<String, Object> blogInfo = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();

            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("userAvatar", user.getAvatar());

            blogInfo.put("blogId", blog.getId());
            blogInfo.put("blogContent", blog.getContent());
            blogInfo.put("blogTopicId", topic.getId());
            blogInfo.put("blogTopicName", topic.getName());
            Integer likes = admireRepository.countByBlogId(blogId);
            blog.setLikes(likes);
            blogRepository.save(blog);
            blogInfo.put("blogLikes", likes);
            blogInfo.put("blogCommentsCount", commentRepository.countByBlogId(blogId));
            blogInfo.put("blogCollectionsCount", collectionRepository.countByBlogId(blogId));
            blogInfo.put("blogPictures", blog.getPictures());
            blogInfo.put("blogCreatedTime", blog.getCreatedTime());
            blogInfo.put("blogIsLiked", likeTable == null ? 0 : 1);
            blogInfo.put("blogIsCollected", collection == null ? 0 : 1);

            dataItem.put("user", userInfo);
            dataItem.put("blog", blogInfo);
            data.add(dataItem);
        }
        return map;
    }
}
