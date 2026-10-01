package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.dto.UserRegistrationDto;
import com.onlinebookstore.bookstore.model.User;
import com.onlinebookstore.bookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(UserRegistrationDto dto, RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Username is required");
                return "redirect:/register?error";
            }

            if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Email is required");
                return "redirect:/register?error";
            }

            if (dto.getPassword() == null || dto.getPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters");
                return "redirect:/register?error";
            }

            // Check if username already exists
            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Username already exists");
                return "redirect:/register?error";
            }

            // Create new user
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            User user = new User(dto.getUsername(), encodedPassword, dto.getEmail());
            user.setAdmin(false); // Regular users are not admins by default
            userRepository.save(user);

            // Success message
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login?success";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register?error";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
