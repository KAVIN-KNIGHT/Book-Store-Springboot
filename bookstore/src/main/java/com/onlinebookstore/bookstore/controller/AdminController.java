package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.model.Book;
import com.onlinebookstore.bookstore.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private final BookRepository bookRepository;

    public AdminController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping({ "/admin", "/admin/dashboard" })
    public String dashboard(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "admin/dashboard";
    }

    @GetMapping("/admin/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "admin/manageBooks";
    }

    @PostMapping("/admin/books/add")
    public String addBook(@RequestParam String title,
            @RequestParam String author,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double price) {
        Book book = new Book(title, author, description, price != null ? price : 0.0);
        bookRepository.save(book);
        return "redirect:/admin/books";
    }
}
