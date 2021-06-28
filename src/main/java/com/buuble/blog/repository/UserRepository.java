package com.buuble.blog.repository;

import com.buuble.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(Integer id);

    User findUserByUsername(String username);

    User save(User user);
}
