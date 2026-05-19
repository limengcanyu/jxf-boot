-- 清空现有数据
DELETE FROM t_order_item;
DELETE FROM t_order;
DELETE FROM t_inventory;
DELETE FROM t_user;

-- 初始化用户数据
INSERT INTO t_user (id, username, email, phone_country_code, phone_number, address_province, address_city, address_district, address_detail, address_zip_code, enabled, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('u001', 'zhangsan', 'zhangsan@example.com', '+86', '13800138001', '广东省', '深圳市', '南山区', '科技园路88号', '518000', TRUE, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_user (id, username, email, phone_country_code, phone_number, address_province, address_city, address_district, address_detail, address_zip_code, enabled, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('u002', 'lisi', 'lisi@example.com', '+86', '13800138002', '北京市', '北京市', '朝阳区', '建国路99号', '100000', TRUE, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_user (id, username, email, phone_country_code, phone_number, address_province, address_city, address_district, address_detail, address_zip_code, enabled, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('u003', 'wangwu', 'wangwu@example.com', '+86', '13800138003', '上海市', '上海市', '浦东新区', '陆家嘴环路100号', '200000', TRUE, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化订单数据
INSERT INTO t_order (id, user_id, shipping_address_province, shipping_address_city, shipping_address_district, shipping_address_detail, shipping_address_zip_code, total_amount, status, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('o001', 'u001', '广东省', '深圳市', '南山区', '科技园路88号', '518000', 2999.0000, 'COMPLETED', 0, 1, FALSE, 'u001', 'u001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_order (id, user_id, shipping_address_province, shipping_address_city, shipping_address_district, shipping_address_detail, shipping_address_zip_code, total_amount, status, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('o002', 'u002', '北京市', '北京市', '朝阳区', '建国路99号', '100000', 599.0000, 'PAID', 0, 1, FALSE, 'u002', 'u002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_order (id, user_id, shipping_address_province, shipping_address_city, shipping_address_district, shipping_address_detail, shipping_address_zip_code, total_amount, status, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('o003', 'u003', '上海市', '上海市', '浦东新区', '陆家嘴环路100号', '200000', 1299.0000, 'CONFIRMED', 0, 1, FALSE, 'u003', 'u003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化订单项数据
INSERT INTO t_order_item (id, order_id, product_id, product_name, unit_price, quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('oi001', 'o001', 'p001', '笔记本电脑', 2999.0000, 1, 0, 1, FALSE, 'u001', 'u001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_order_item (id, order_id, product_id, product_name, unit_price, quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('oi002', 'o002', 'p002', '无线鼠标', 199.0000, 2, 0, 1, FALSE, 'u002', 'u002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_order_item (id, order_id, product_id, product_name, unit_price, quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('oi003', 'o002', 'p003', '机械键盘', 200.0000, 1, 0, 1, FALSE, 'u002', 'u002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_order_item (id, order_id, product_id, product_name, unit_price, quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('oi004', 'o003', 'p004', '显示器', 1299.0000, 1, 0, 1, FALSE, 'u003', 'u003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化库存数据
INSERT INTO t_inventory (id, product_id, quantity, reserved_quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('inv001', 'p001', 100, 5, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_inventory (id, product_id, quantity, reserved_quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('inv002', 'p002', 500, 20, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_inventory (id, product_id, quantity, reserved_quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('inv003', 'p003', 300, 10, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_inventory (id, product_id, quantity, reserved_quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('inv004', 'p004', 200, 8, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO t_inventory (id, product_id, quantity, reserved_quantity, tenant_id, version, deleted, created_by, updated_by, created_at, updated_at) VALUES ('inv005', 'p005', 1000, 50, 0, 1, FALSE, 'system', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);