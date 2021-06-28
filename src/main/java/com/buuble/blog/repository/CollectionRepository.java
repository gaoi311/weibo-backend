package com.buuble.blog.repository;

import com.buuble.blog.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    Integer countByBlogId(Integer blogId);

    Integer countByBlogIdAndIsSaw(Integer blogId, String isSaw);

    @Transactional
    void deleteByBlogIdAndUserId(Integer blogId, Integer userId);

    Collection findByBlogIdAndUserId(Integer blogId, Integer userId);

    Collection save(Collection collection);

    List<Collection> findCollectionsByUserId(Integer userId);

    List<Collection> findCollectionsByBlogIdAndIsSaw(Integer blogId, String isSaw);
}
