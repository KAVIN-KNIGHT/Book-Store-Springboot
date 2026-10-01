package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.User;
import com.onlinebookstore.bookstore.repository.UserRepository;
import com.onlinebookstore.bookstore.repository.BookRepository;
import com.onlinebookstore.bookstore.model.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, BookRepository bookRepository) {
        return args -> {
            // create a default admin if none exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                String raw = "Admin@123"; // sensible default; you can change it after first login
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode(raw));
                admin.setEmail("admin@bookstore.local");
                admin.setAdmin(true);
                userRepository.save(admin);
                System.out.println("[DataInitializer] seeded admin user 'admin' with password 'Admin@123'");
            }

            // seed some sample books if none exist
            if (bookRepository.count() == 0) {
                Book b1 = new Book();
                b1.setTitle("The Great Gatsby");
                b1.setAuthor("F. Scott Fitzgerald");
                b1.setDescription("A classic novel of the Jazz Age.");
                b1.setPrice(12.99);
                b1.setImageUrl("/images/gatsby.jpg");

                Book b2 = new Book();
                b2.setTitle("1984");
                b2.setAuthor("George Orwell");
                b2.setDescription("A dystopian novel about totalitarianism.");
                b2.setPrice(9.99);
                b2.setImageUrl("/images/1984.jpg");

                bookRepository.save(b1);
                bookRepository.save(b2);
                System.out.println("[DataInitializer] seeded sample books");
            }
        };
    }
}
