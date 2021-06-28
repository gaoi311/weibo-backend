package com.buuble.blog.repository;

import com.buuble.blog.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Follow save(Follow follow);

    Integer countByOtherIdAndIsSaw(Integer otherId, String isSaw);

    @Transactional
    void deleteFollowByUserIdAndOtherId(Integer userId, Integer otherId);

    List<Follow> findFollowsByUserId(Integer userId);

    List<Follow> findFollowsByOtherIdAndIsSaw(Integer otherId, String isSaw);

    Follow findFollowByUserIdAndOtherId(Integer userId, Integer otherId);
}
