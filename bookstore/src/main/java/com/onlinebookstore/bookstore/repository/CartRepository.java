package com.onlinebookstore.bookstore.repository;

import com.onlinebookstore.bookstore.model.Book;
import com.onlinebookstore.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByBookAndOrderIsNull(Book book);

    List<CartItem> findByOrderIsNull();
}
