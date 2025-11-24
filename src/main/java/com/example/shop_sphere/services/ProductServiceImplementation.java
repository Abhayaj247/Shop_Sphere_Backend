package com.example.shop_sphere.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shop_sphere.entities.Category;
import com.example.shop_sphere.entities.Product;
import com.example.shop_sphere.entities.ProductImage;
import com.example.shop_sphere.repositories.CategoryRepository;
import com.example.shop_sphere.repositories.ProductImageRepository;
import com.example.shop_sphere.repositories.ProductRepositoy;

@Service
public class ProductServiceImplementation implements ProductService{
	
	private ProductRepositoy productRepository;
	private ProductImageRepository productImageRepository;
	private CategoryRepository categoryRepository;	

	public ProductServiceImplementation(ProductRepositoy productRepository,
			ProductImageRepository productImageRepository, CategoryRepository categoryRepository) {
		super();
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return productRepository.findByCategory_CategoryId(category.getCategoryId());
			}else {
				throw new RuntimeException("Category not found");
			}
		}else {
			return productRepository.findAll();
		}
	}

	@Override
	public List<String> getProductImages(Integer productId) {
		List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(productId);
		List<String> imageUrls = new ArrayList<>();
		for(ProductImage image : productImages) {
			imageUrls.add(image.getImageUrl());
		}
		return imageUrls;
	}

}
