package com.example.shop_sphere.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop_sphere.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	Optional<Category> findByCategoryName(String categoryName);
}
