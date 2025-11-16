USE mini_market;

-- =========================
-- CATEGORY
-- =========================
INSERT INTO category (category_id, name) VALUES
(UUID(), 'Beverages'),
(UUID(), 'Snacks'),
(UUID(), 'Personal Care'),
(UUID(), 'Cleaning Supplies');

-- =========================
-- BRANCH
-- =========================
INSERT INTO branch (address, is_active, name, open_date, phone) VALUES
('12 Lê Văn Việt, Q9, TP.HCM', 1, 'Branch 1 - District 9', CURDATE(), '0909000001'),
('45 Võ Văn Ngân, Thủ Đức', 1, 'Branch 2 - Thủ Đức', CURDATE(), '0909000002');

-- =========================
-- EMPLOYEE
-- =========================
INSERT INTO employee (employee_id, username, password, name, phone, role, start_at, status)
VALUES
(UUID(), 'admin', 'admin123', 'Nguyễn Văn Admin', '0909000000', 'ADMIN', CURDATE(), 1),
(UUID(), 'staff01', 'staff123', 'Trần Thị Staff', '0909111222', 'STAFF', CURDATE(), 1);

-- =========================
-- CUSTOMER
-- =========================
INSERT INTO customer (customer_id, name, phone, created_at)
VALUES
(UUID(), 'Nguyễn Minh Khôi', '0905123456', NOW()),
(UUID(), 'Trần Lan Anh', '0912233445', NOW()),
(UUID(), 'Phạm Gia Huy', '0909555666', NOW());

-- =========================
-- PRODUCT
-- =========================
-- Chúng ta cần chọn category_id có thật
INSERT INTO product (product_id, name, sell_price, cost_price, is_active, created_at, category_id)
SELECT UUID(), 'Coca-Cola 330ml', 10000, 8000, 1, NOW(), category_id FROM category WHERE name = 'Beverages' LIMIT 1;

INSERT INTO product (product_id, name, sell_price, cost_price, is_active, created_at, category_id)
SELECT UUID(), 'Snack Oishi', 8000, 5000, 1, NOW(), category_id FROM category WHERE name = 'Snacks' LIMIT 1;

INSERT INTO product (product_id, name, sell_price, cost_price, is_active, created_at, category_id)
SELECT UUID(), 'Dầu gội Clear 200ml', 55000, 40000, 1, NOW(), category_id FROM category WHERE name = 'Personal Care' LIMIT 1;

INSERT INTO product (product_id, name, sell_price, cost_price, is_active, created_at, category_id)
SELECT UUID(), 'Nước lau sàn Sunlight 1L', 35000, 20000, 1, NOW(), category_id FROM category WHERE name = 'Cleaning Supplies' LIMIT 1;

-- =========================
-- SHIFT
-- =========================
INSERT INTO shift (name, start_time, end_time)
VALUES
('Morning', '07:00:00', '15:00:00'),
('Evening', '15:00:00', '23:00:00'),
('Night', '23:00:00', '07:00:00');

-- =========================
-- SHIFT_ASSIGNMENT
-- =========================
INSERT INTO shift_assignment (branch_id, employee_id, shift_id)
SELECT b.branch_id, e.employee_id, s.shift_id
FROM branch b, employee e, shift s
WHERE s.name = 'Morning'
LIMIT 1;

INSERT INTO shift_assignment (branch_id, employee_id, shift_id)
SELECT b.branch_id, e.employee_id, s.shift_id
FROM branch b, employee e, shift s
WHERE s.name = 'Evening'
LIMIT 1 OFFSET 1;

-- =========================
-- INVENTORY
-- =========================
INSERT INTO inventory (created_at, quantity, updated_at, branch_id, product_id)
SELECT NOW(), 200, NOW(), b.branch_id, p.product_id
FROM branch b, product p
LIMIT 4;

-- =========================
-- INVOICE
-- =========================
-- Giả lập 2 hóa đơn: 1 cho Khôi, 1 cho Lan Anh
INSERT INTO invoice (invoice_id, created_at, discount, note, total, branch_id, customer_id, employee_id)
SELECT UUID(), NOW(), 0, 'Invoice 1', 26000, b.branch_id, c.customer_id, e.employee_id
FROM branch b, customer c, employee e
WHERE c.name='Nguyễn Minh Khôi'
LIMIT 1;

INSERT INTO invoice (invoice_id, created_at, discount, note, total, branch_id, customer_id, employee_id)
SELECT UUID(), NOW(), 5000, 'Invoice 2', 88000, b.branch_id, c.customer_id, e.employee_id
FROM branch b, customer c, employee e
WHERE c.name='Trần Lan Anh'
LIMIT 1;

-- =========================
-- INVOICE_DETAIL
-- =========================
-- Liên kết invoice & product
INSERT INTO invoice_detail (invoice_id, product_id, quantity, total, unit_price)
SELECT i.invoice_id, p.product_id, 2, 20000, 10000
FROM invoice i, product p
WHERE p.name='Coca-Cola 330ml'
LIMIT 1;

INSERT INTO invoice_detail (invoice_id, product_id, quantity, total, unit_price)
SELECT i.invoice_id, p.product_id, 1, 8000, 8000
FROM invoice i, product p
WHERE p.name='Snack Oishi'
LIMIT 1 OFFSET 1;

-- =========================
-- LOYALTY
-- =========================
INSERT INTO loyalty (loyalty_id, last_update, total_points, customer_id)
SELECT UUID(), NOW(), FLOOR(RAND()*100), customer_id FROM customer;


COMMIT;
