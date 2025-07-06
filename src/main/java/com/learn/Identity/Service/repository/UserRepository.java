package com.learn.Identity.Service.repository;

import com.learn.Identity.Service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String userName);
}
