package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.onlinebookstore.bookstore.model.Book;

@Controller
public class BookController {
	
	@Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "user/books";
    }
    
    @GetMapping("/books/search")
    public String searchBooks(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Book> books;
        if (query != null && !query.trim().isEmpty()) {
            books = bookService.searchBooks(query);
        } else {
            books = bookService.findAll();
        }
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        return "user/books"; // Thymeleaf view
    }

    @GetMapping("/books/id/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "user/books";
    }
}
