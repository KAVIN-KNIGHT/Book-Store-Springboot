package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CartService cartService;

    public OrderService(CartService cartService) {
        this.cartService = cartService;
    }

    // Place order: delegate to CartService.checkout()
    public Order placeOrder() {
        return cartService.checkout();
    }
}
