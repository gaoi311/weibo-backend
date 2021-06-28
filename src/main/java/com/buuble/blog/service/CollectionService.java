package com.buuble.blog.service;

import com.buuble.blog.entity.Collection;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface CollectionService {

    Collection addCollection(Collection collection);

    @Transactional
    boolean deleteCollection(Integer userId, Integer blogId);

    Collection findByBlogIdAndUserId(Integer blogId, Integer userId);

    Map<String, Object> getCollectionsByUserId(Integer userId);

    Integer countByBlogIdAndIsSaw(Integer blogId, String isSaw);

    List<Map<String, Object>> getCollectionsByBlogIdAndIsSaw(Integer blogId, String isSaw);

}
