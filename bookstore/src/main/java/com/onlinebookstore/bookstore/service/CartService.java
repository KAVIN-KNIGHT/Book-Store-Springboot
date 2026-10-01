package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.Book;
import com.onlinebookstore.bookstore.model.CartItem;
import com.onlinebookstore.bookstore.model.Order;
import com.onlinebookstore.bookstore.repository.BookRepository;
import com.onlinebookstore.bookstore.repository.CartRepository;
import com.onlinebookstore.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public CartService(CartRepository cartRepository, BookRepository bookRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    // List all cart items (preview)
    public List<CartItem> listAll() {
        return cartRepository.findByOrderIsNull(); // only items not yet in an order
    }

    // Add a book to cart
    @Transactional
    public void addToCart(Long bookId, int qty) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        CartItem item = cartRepository.findByBookAndOrderIsNull(book).orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() + qty);
            cartRepository.save(item);
        } else {
            cartRepository.save(new CartItem(book, qty));
        }
    }

    // Update quantity
    @Transactional
    public void updateQuantity(Long bookId, int qty) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        CartItem item = cartRepository.findByBookAndOrderIsNull(book)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        item.setQuantity(qty);
        cartRepository.save(item);
    }

    // Remove from cart
    @Transactional
    public void removeFromCart(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        CartItem item = cartRepository.findByBookAndOrderIsNull(book)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartRepository.delete(item);
    }

    // Clear cart
    @Transactional
    public void clear() {
        cartRepository.deleteAll(cartRepository.findByOrderIsNull());
    }

    // Checkout: create order and assign to cart items
    @Transactional
    public Order checkout() {
        List<CartItem> items = cartRepository.findByOrderIsNull();
        if (items.isEmpty())
            throw new RuntimeException("Cart is empty");

        // Create order first
        Order order = new Order();
        orderRepository.save(order);

        // Assign order to items
        for (CartItem item : items) {
            item.setOrder(order);
            cartRepository.save(item);
        }

        return order;
    }

    // Calculate cart total (subtotal before shipping, tax, discounts)
    public double getCartTotal() {
        return listAll().stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
    }

    // Calculate shipping cost (flat rate or free shipping over threshold)
    public double getShippingCost() {
        double cartTotal = getCartTotal();
        // Free shipping over $50, otherwise $5.99
        return cartTotal >= 50.0 ? 0.0 : 5.99;
    }

    // Get current discount (placeholder - can be enhanced with promo codes)
    public double getDiscount() {
        // TODO: Implement promo code logic
        return 0.0;
    }

    // Calculate tax amount (8% tax rate)
    public double getTaxAmount() {
        double cartTotal = getCartTotal();
        double taxRate = 0.08; // 8% tax
        return cartTotal * taxRate;
    }

    // Calculate final order total
    public double getOrderTotal() {
        return getCartTotal() + getShippingCost() + getTaxAmount() - getDiscount();
    }
}
