DROP TABLE IF EXISTS order_info;

CREATE TABLE order_info (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            order_id VARCHAR(64) NOT NULL UNIQUE,
                            amount DECIMAL(10,2) NOT NULL,
                            status VARCHAR(32) NOT NULL,
                            reason VARCHAR(255),
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                            update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_id ON order_info(order_id);
CREATE INDEX idx_status ON order_info(status);
