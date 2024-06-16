package com.example.demo.repository;


import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.user_email = :email")
    boolean existsByEmail(@Param("email") String userEmail);
    //boolean existsByEmail(String userEmail);
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.user_email = :email")
    void deleteByEmail(@Param("email") String email);
    //void deleteByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.user_email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    //Optional<User> findByEmail(String email);
}
