-- Legacy Database Schema
-- Using old-style DDL without modern constraints

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS users;

-- Users table with legacy design
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- Orders table with legacy foreign key
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    user_id BIGINT,
    product_name VARCHAR(200),
    quantity INT DEFAULT 1,
    price DECIMAL(10,2),
    order_status VARCHAR(20) DEFAULT 'PENDING',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Legacy indexes (not optimized)
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_order_user ON orders(user_id);
CREATE INDEX idx_order_status ON orders(order_status);