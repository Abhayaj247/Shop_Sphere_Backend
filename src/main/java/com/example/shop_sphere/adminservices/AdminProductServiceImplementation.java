package com.example.shop_sphere.adminservices;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shop_sphere.entities.Category;
import com.example.shop_sphere.entities.Product;
import com.example.shop_sphere.entities.ProductImage;
import com.example.shop_sphere.repositories.CategoryRepository;
import com.example.shop_sphere.repositories.ProductImageRepository;
import com.example.shop_sphere.repositories.ProductRepositoy;

@Service
public class AdminProductServiceImplementation implements AdminProductService {
	
	private final ProductRepositoy productRepository;
	
    private final ProductImageRepository productImageRepository;
    
    private final CategoryRepository categoryRepository;
    
    
	public AdminProductServiceImplementation(ProductRepositoy productRepository,
			ProductImageRepository productImageRepository, CategoryRepository categoryRepository) {
		super();
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId,
			String imageUrl) {
		// Validate the category
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new IllegalArgumentException("Invalid category ID");
		}

		// Create and save the product
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(BigDecimal.valueOf(price));
		product.setStock(stock);
		product.setCategory(category.get());
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());

		Product savedProduct = productRepository.save(product);

		// Create and save the product image
		if (imageUrl != null && !imageUrl.isEmpty()) {
			ProductImage productImage = new ProductImage();
			productImage.setProduct(savedProduct);
			productImage.setImageUrl(imageUrl);
			productImageRepository.save(productImage);
		} else {
			throw new IllegalArgumentException("Product image URL cannot be empty");
		}

		return savedProduct;
	}

	@Override
	public void deleteProduct(Integer productId) {
		// Check if the product exists
		if (!productRepository.existsById(productId)) {
			throw new IllegalArgumentException("Product not found");
		}

		// Delete associated product images
		productImageRepository.deleteByProductId(productId);

		// Delete the product
		productRepository.deleteById(productId);
	}
}
