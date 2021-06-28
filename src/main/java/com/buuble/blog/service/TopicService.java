package com.buuble.blog.service;

import java.util.Map;

public interface TopicService {
    Map<String, Object> getLikeTopicByKeyString(String key);
    Map<String, Object> getMostTopics();
}
