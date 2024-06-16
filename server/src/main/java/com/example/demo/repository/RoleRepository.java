package com.example.demo.repository;

import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

   //Optional<Role> findByName(String name);
   @Query("SELECT r FROM Role r WHERE r.role_name = :name")
    Optional<Role> findByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE r.role_name = :name")
    boolean existsByName(@Param("name") String name);
//    boolean existsByName(String role);
}
