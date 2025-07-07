package com.learn.Identity.Service.repository;

import com.learn.Identity.Service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String userName);

    // Stored procedure login
    @Query(value = "CALL validateUser(:p_username, :p_password)", nativeQuery = true)
    User validateUser(
        @Param("p_username") String username,
        @Param("p_password") String password
    );

    // Backup plain JPA query (unused for now)
    User findByUsernameAndPassword(String username, String password);

}
