package com.buuble.blog.controller;

import com.buuble.blog.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TopicHandler {

    @Autowired
    private TopicService topicService;

    @GetMapping("/topics")
    Map<String, Object> getLikeTopicByKeyString(@RequestParam("key") String Key) {
        Map<String, Object> result = topicService.getLikeTopicByKeyString(Key);
        Map<String, Object> status = new HashMap<>();
        if (result == null) {
            result = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            result.put("data", data);
            status.put("code", 404);
            status.put("msg", "获取失败");
        } else {
            if (Key == null || Key.isEmpty()) {
                List<Map<String, Object>> data = new ArrayList<>();
                result.put("data", data);
                status.put("code", 404);
                status.put("msg", "关键字不能为空");
                result.put("status", status);
                return result;
            }
            status.put("code", 200);
            status.put("msg", "成功");
        }
        result.put("status", status);
        return result;
    }

    @GetMapping("/mosttopics")
    Map<String, Object> getMostTopics() {
        Map<String, Object> result = topicService.getMostTopics();
        Map<String, Object> status = new HashMap<>();
        if (result == null) {
            result = new HashMap<>();
            List<Map<String, Object>> data = new ArrayList<>();
            result.put("data", data);
            status.put("code", 404);
            status.put("msg", "获取失败");
        } else {
            status.put("code", 200);
            status.put("msg", "成功");
        }
        result.put("status", status);
        return result;
    }
}
