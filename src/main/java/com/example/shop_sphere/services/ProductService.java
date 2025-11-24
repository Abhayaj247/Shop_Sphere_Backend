package com.example.shop_sphere.services;

import java.util.List;

import com.example.shop_sphere.entities.Product;

public interface ProductService {
	public List<Product> getProductsByCategory(String categoryName);
	public List<String> getProductImages(Integer productId);
}
