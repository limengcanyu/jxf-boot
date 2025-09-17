-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- 存储BCrypt加密后的密码
    email VARCHAR(100),
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 创建角色表
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE, -- 例如: ROLE_USER, ROLE_ADMIN
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 创建用户角色关联表 (简化版RBAC)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
    );

-- 安全地插入默认角色 - ROLE_USER (仅当表为空或角色不存在时)
INSERT INTO roles (name, description)
SELECT 'ROLE_USER', '普通用户'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

-- 安全地插入默认角色 - ROLE_ADMIN (仅当表为空或角色不存在时)
INSERT INTO roles (name, description)
SELECT 'ROLE_ADMIN', '管理员'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

-- 插入一个默认管理员用户示例 (密码为 'admin123'，需要在应用启动后通过BCrypt加密存储)
-- 注意：这个INSERT也需要安全处理，或者最好通过代码或专门的初始化脚本在首次部署时执行一次。
-- INSERT INTO users (username, password, email, full_name)
-- SELECT 'admin', '$2a$10$...', 'admin@example.com', 'Admin User'
-- WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');
-- 密码 '$2a$10$...' 需要替换为 'admin123' 使用BCrypt加密后的实际值。



