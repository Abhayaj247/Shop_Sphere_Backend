package com.example.shop_sphere.adminservices;

import com.example.shop_sphere.entities.Product;

public interface AdminProductService {
	public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageUrl);
	 public void deleteProduct(Integer productId);
}
