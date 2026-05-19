-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_country_code VARCHAR(10),
    phone_number VARCHAR(20),
    address_province VARCHAR(100),
    address_city VARCHAR(100),
    address_district VARCHAR(100),
    address_detail VARCHAR(500),
    address_zip_code VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    tenant_id BIGINT DEFAULT 0,
    version BIGINT DEFAULT 1,
    deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    shipping_address_province VARCHAR(100),
    shipping_address_city VARCHAR(100),
    shipping_address_district VARCHAR(100),
    shipping_address_detail VARCHAR(500),
    shipping_address_zip_code VARCHAR(20),
    total_amount DECIMAL(18,4) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    tenant_id BIGINT DEFAULT 0,
    version BIGINT DEFAULT 1,
    deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 订单项表
CREATE TABLE IF NOT EXISTS t_order_item (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    unit_price DECIMAL(18,4) NOT NULL,
    quantity INT NOT NULL,
    tenant_id BIGINT DEFAULT 0,
    version BIGINT DEFAULT 1,
    deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 库存表
CREATE TABLE IF NOT EXISTS t_inventory (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL UNIQUE,
    quantity INT DEFAULT 0,
    reserved_quantity INT DEFAULT 0,
    tenant_id BIGINT DEFAULT 0,
    version BIGINT DEFAULT 1,
    deleted BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_username ON t_user(username);
CREATE INDEX IF NOT EXISTS idx_user_email ON t_user(email);
CREATE INDEX IF NOT EXISTS idx_user_deleted ON t_user(deleted);
CREATE INDEX IF NOT EXISTS idx_user_created_by ON t_user(created_by);
CREATE INDEX IF NOT EXISTS idx_order_user_id ON t_order(user_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON t_order(status);
CREATE INDEX IF NOT EXISTS idx_order_deleted ON t_order(deleted);
CREATE INDEX IF NOT EXISTS idx_order_created_by ON t_order(created_by);
CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON t_order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_order_item_deleted ON t_order_item(deleted);
CREATE INDEX IF NOT EXISTS idx_order_item_created_by ON t_order_item(created_by);
CREATE INDEX IF NOT EXISTS idx_inventory_product_id ON t_inventory(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_deleted ON t_inventory(deleted);
CREATE INDEX IF NOT EXISTS idx_inventory_created_by ON t_inventory(created_by);
CREATE INDEX IF NOT EXISTS idx_user_tenant_id ON t_user(tenant_id);
CREATE INDEX IF NOT EXISTS idx_order_tenant_id ON t_order(tenant_id);
CREATE INDEX IF NOT EXISTS idx_order_item_tenant_id ON t_order_item(tenant_id);
CREATE INDEX IF NOT EXISTS idx_inventory_tenant_id ON t_inventory(tenant_id);