package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BookRepository bookRepository;

    public HomeController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping({ "/", "/home" })
    public String home(Model model) {
        // Get all books or featured books (limit to 8 for home page)
        model.addAttribute("featuredBooks", bookRepository.findAll().stream().limit(8).toList());
        model.addAttribute("categories", java.util.Collections.emptyList()); // Can be expanded with category support
        return "user/home";
    }

    @GetMapping("/about")
    public String about() {
        return "user/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("/orders")
    public String orders() {
        return "user/orders";
    }
}