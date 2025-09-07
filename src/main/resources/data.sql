-- Sample legacy data

-- Insert sample users
INSERT INTO users (username, email, full_name, status) VALUES 
('john_doe', 'john.doe@legacy.com', 'John Doe', 'ACTIVE'),
('jane_smith', 'jane.smith@legacy.com', 'Jane Smith', 'ACTIVE'),
('bob_wilson', 'bob.wilson@legacy.com', 'Bob Wilson', 'INACTIVE'),
('alice_brown', 'alice.brown@legacy.com', 'Alice Brown', 'ACTIVE');

-- Insert sample orders
INSERT INTO orders (order_number, user_id, product_name, quantity, price, order_status, notes) VALUES 
('ORD-2024-001', 1, 'Legacy Widget', 5, 99.99, 'COMPLETED', 'Rush delivery requested'),
('ORD-2024-002', 1, 'Vintage Component', 2, 45.50, 'PENDING', NULL),
('ORD-2024-003', 2, 'Classic Module', 1, 250.00, 'PROCESSING', 'Handle with care'),
('ORD-2024-004', 2, 'Retro Device', 3, 75.25, 'COMPLETED', 'Customer satisfied'),
('ORD-2024-005', 4, 'Old-School Gadget', 10, 15.99, 'PENDING', 'Bulk order discount applied');