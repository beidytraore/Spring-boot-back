package com.example.myBackend.Backend.dao;

import com.example.myBackend.Backend.model.Role;
import com.example.myBackend.Backend.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(Roles role);
}