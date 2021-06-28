package com.buuble.blog.service.impl;

import com.buuble.blog.entity.Topic;
import com.buuble.blog.repository.BlogRepository;
import com.buuble.blog.repository.TopicRepository;
import com.buuble.blog.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Map<String, Object> getLikeTopicByKeyString(String key) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        result.put("data", data);
        int[] ids = topicRepository.findAllTopicId();
        for (Integer topicId : ids) {
            Topic topic = topicRepository.findTopicById(topicId);
            if (topic.getName().indexOf(key) != -1 || key.indexOf(topic.getName()) != -1) {
                Map<String, Object> topicInfo = new HashMap<>();
                topicInfo.put("topicId", topic.getId());
                topicInfo.put("topicName", topic.getName());
                data.add(topicInfo);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> getMostTopics() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        result.put("data", data);
        int[] ids = topicRepository.findAllTopicId();
        TopicIdAndNumber[] tns = new TopicIdAndNumber[ids.length];
        int i = 0;
        for (Integer topicId : ids) {
            tns[i] = new TopicIdAndNumber(topicId, blogRepository.countBlogByTopicId(topicId));
            i++;
        }
        Arrays.sort(tns);
        int n = 0;
        while (n < 10 && n < tns.length) {
            Map<String, Object> topicInfo = new HashMap<>();
            data.add(topicInfo);
            Topic topic = topicRepository.findTopicById(tns[n].topicId);
            topicInfo.put("topicId", topic.getId());
            topicInfo.put("topicName", topic.getName());
            n++;
        }
        return result;
    }

    private class TopicIdAndNumber implements Comparable {
        public int topicId;
        public int number; //同一话题ID对应的博客数

        public TopicIdAndNumber(int topicId, int number) {
            this.topicId = topicId;
            this.number = number;
        }

        @Override
        public int compareTo(Object o) {    //按照number从大到小排序
            if (o instanceof TopicIdAndNumber) {
                TopicIdAndNumber tn = (TopicIdAndNumber) o;
                if (this.number > tn.number) return -1;
                else if (this.number == tn.number) return 0;
                else return 1;
            }
            return 0;
        }
    }
}
