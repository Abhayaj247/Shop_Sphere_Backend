package com.example.shop_sphere.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_sphere.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
}
