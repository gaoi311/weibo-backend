package com.buuble.blog.repository;

import com.buuble.blog.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Topic findTopicById(Integer id);

//    @Query(value = "select name from Topic2 where id in(?1) ")
//    List<String> findNameById(List<Integer> ids);

    @Query(value = "select id from Topic")
    int[] findAllTopicId();

    Topic save(Topic topic);
}
