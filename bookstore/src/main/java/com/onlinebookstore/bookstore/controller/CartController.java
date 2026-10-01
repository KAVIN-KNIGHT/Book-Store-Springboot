package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.repository.BookRepository;
import com.onlinebookstore.bookstore.service.CartService;
import com.onlinebookstore.bookstore.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final BookRepository bookRepository;

    public CartController(CartService cartService, OrderService orderService, BookRepository bookRepository) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.bookRepository = bookRepository;
    }

    private void addCartSummary(Model model) {
        model.addAttribute("items", cartService.listAll());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        model.addAttribute("shippingCost", cartService.getShippingCost());
        model.addAttribute("discount", cartService.getDiscount());
        model.addAttribute("taxAmount", cartService.getTaxAmount());
        model.addAttribute("orderTotal", cartService.getOrderTotal());
        // Add recommended books (limit to 4)
        model.addAttribute("recommendedBooks", bookRepository.findAll().stream().limit(4).toList());
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        addCartSummary(model);
        return "user/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId, @RequestParam(defaultValue = "1") int qty) {
        cartService.addToCart(bookId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long bookId, @RequestParam int qty) {
        if (qty <= 0) {
            cartService.removeFromCart(bookId);
        } else {
            cartService.updateQuantity(bookId, qty);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(Model model) {
        model.addAttribute("order", orderService.placeOrder());
        return "user/checkout";
    }

    @PostMapping("/cart/clear")
    public String clearAll() {
        cartService.clear();
        return "redirect:/cart";
    }

    @PostMapping("/cart/apply-promo")
    public String applyPromo(@RequestParam String promoCode, Model model) {
        // TODO: Implement promo code logic. For now, just add a message.
        // You can integrate with a PromoService if you have one.
        model.addAttribute("promoMessage", "Promo code applied: " + promoCode);
        // Optionally, update cartService with discount logic here
        addCartSummary(model);
        return "user/cart";
    }
}
