package com.learn.Identity.Service.repository;

import com.learn.Identity.Service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String userName);
    Optional<User> findByUsername(String username);

    // CORRECT WAY: Using @Procedure annotation for stored procedures
    @Procedure(procedureName = "validateUser")
    User validateUserStoredProcedure(
        @Param("p_username") String username,
        @Param("p_password") String password
    );


}
