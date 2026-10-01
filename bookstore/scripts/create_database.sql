-- create_database.sql
-- SQL script to create the bookstore database and a user for the Spring Boot application.
-- Adjust the username, password, and character set as needed.

-- 1) Create database
CREATE DATABASE IF NOT EXISTS books
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 2) Create user (replace 'bookuser'@'localhost' and 'changeme' with secure values)
-- If the user already exists, you can skip the CREATE USER step and run the GRANT instead.

CREATE USER IF NOT EXISTS 'bookuser'@'localhost' IDENTIFIED BY 'changeme';

-- 3) Grant privileges
GRANT ALL PRIVILEGES ON books.* TO 'bookuser'@'localhost';

-- 4) Apply privileges
FLUSH PRIVILEGES;

-- 5) Optional: create example table and insert sample data (books)
USE books;

CREATE TABLE IF NOT EXISTS book (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255),
  description TEXT,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO book (title, author, description, price) VALUES
('The Pragmatic Programmer', 'Andrew Hunt', 'Classic book on pragmatic software development practices.', 39.99),
('Clean Code', 'Robert C. Martin', 'A handbook of agile software craftsmanship.', 34.50),
('Domain-Driven Design', 'Eric Evans', 'Foundational text on DDD.', 49.00);

-- 6) Users table
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  roles VARCHAR(255) DEFAULT 'ROLE_USER',
  enabled BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert an admin user. Replace the password value with a BCrypt hash for production.
-- The example below uses a plaintext password 'admin' which you should change.
INSERT INTO users (username, email, password, roles, enabled) VALUES
('admin', 'admin@example.com', 'admin', 'ROLE_ADMIN', TRUE)
ON DUPLICATE KEY UPDATE username = username;

-- 7) Order and order_items tables
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  status VARCHAR(50) DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8) Optional: cart items table
CREATE TABLE IF NOT EXISTS cart_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  book_id BIGINT,
  quantity INT NOT NULL DEFAULT 1,
  added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
