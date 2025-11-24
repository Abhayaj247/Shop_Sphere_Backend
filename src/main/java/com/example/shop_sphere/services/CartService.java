package com.example.shop_sphere.services;

import java.util.Map;

public interface CartService {
	public int getCartItemCount(int userId);
	public void addToCart(int userId, int productId, int quantity);
	public Map<String, Object> getCartItems(int userId);
	public void updateCartItemQuantity(int userId, int productId, int quantity);
	public void deleteCartItem(int userId, int productId);
}
