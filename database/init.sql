CREATE DATABASE IF NOT EXISTS mini_market CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mini_market;
CREATE TABLE category (
    category_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;
CREATE TABLE supplier (
    supplier_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255) NOT NULL,
    phone CHAR(10),
    address VARCHAR(255)
) ENGINE=InnoDB;
CREATE TABLE product (
    product_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255) NOT NULL,
    category_id CHAR(36),
    cost_price DECIMAL(12,2) NOT NULL,
    sell_price DECIMAL(12,2) NOT NULL,
    expiry_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id)
        REFERENCES category(category_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT chk_product_price CHECK (cost_price >= 0 AND sell_price >= cost_price)
) ENGINE=InnoDB;
CREATE TABLE branch (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone CHAR(10),
    address VARCHAR(255),
    open_date DATE DEFAULT (CURDATE()),
    is_active BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;
CREATE TABLE employee (
    employee_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    branch_id INT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone CHAR(10),
    role ENUM('ADMIN', 'STAFF') DEFAULT 'STAFF',
    start_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_at TIMESTAMP NULL,
    status VARCHAR(50),
    CONSTRAINT fk_employee_branch FOREIGN KEY (branch_id)
        REFERENCES branch(branch_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;
CREATE TABLE shift (
    shift_id INT AUTO_INCREMENT PRIMARY KEY,
    name CHAR(10),
    start_time TIME,
    end_time TIME,
    CONSTRAINT chk_shift_time CHECK (start_time < end_time)
) ENGINE=InnoDB;
CREATE TABLE shift_assignment (
    shift_id INT,
    employee_id CHAR(36),
    branch_id INT,
    PRIMARY KEY (shift_id, employee_id, branch_id),
    CONSTRAINT fk_shiftassign_shift FOREIGN KEY (shift_id)
        REFERENCES shift(shift_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_shiftassign_employee FOREIGN KEY (employee_id)
        REFERENCES employee(employee_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_shiftassign_branch FOREIGN KEY (branch_id)
        REFERENCES branch(branch_id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;
CREATE TABLE customer (
    customer_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(255) NOT NULL,
    phone CHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
CREATE TABLE loyalty (
    loyalty_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    customer_id CHAR(36) UNIQUE,
    total_points INT DEFAULT 0,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_loyalty_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_loyalty_points CHECK (total_points >= 0)
) ENGINE=InnoDB;
CREATE TABLE invoice (
    invoice_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    employee_id CHAR(36),
    branch_id INT,
    customer_id CHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12,2) NOT NULL DEFAULT 0,
    discount DECIMAL(12,2) DEFAULT 0,
    note TEXT,
    CONSTRAINT fk_invoice_employee FOREIGN KEY (employee_id)
        REFERENCES employee(employee_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_invoice_branch FOREIGN KEY (branch_id)
        REFERENCES branch(branch_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id)
        REFERENCES customer(customer_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT chk_invoice_total CHECK (total >= 0 AND discount >= 0 AND total >= discount)
) ENGINE=InnoDB;
CREATE TABLE invoice_detail (
    product_id CHAR(36),
    invoice_id CHAR(36),
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    total DECIMAL(12,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    PRIMARY KEY (product_id, invoice_id),
    CONSTRAINT fk_invoicedetail_product FOREIGN KEY (product_id)
        REFERENCES product(product_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_invoicedetail_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoice(invoice_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_invoicedetail_values CHECK (quantity > 0 AND unit_price > 0)
) ENGINE=InnoDB;
CREATE TABLE import_receipt (
    import_id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    supplier_id CHAR(36),
    branch_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    note TEXT,
    CONSTRAINT fk_import_supplier FOREIGN KEY (supplier_id)
        REFERENCES supplier(supplier_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_import_branch FOREIGN KEY (branch_id)
        REFERENCES branch(branch_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;
CREATE TABLE import_detail (
    product_id CHAR(36),
    import_id CHAR(36),
    quantity INT NOT NULL,
    import_price DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (product_id, import_id),
    CONSTRAINT fk_importdetail_product FOREIGN KEY (product_id)
        REFERENCES product(product_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_importdetail_import FOREIGN KEY (import_id)
        REFERENCES import_receipt(import_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_importdetail_values CHECK (quantity > 0 AND import_price > 0)
) ENGINE=InnoDB;
CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT,
    product_id CHAR(36),
    quantity INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_branch FOREIGN KEY (branch_id)
        REFERENCES branch(branch_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id)
        REFERENCES product(product_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT chk_inventory_quantity CHECK (quantity >= 0)
) ENGINE=InnoDB;