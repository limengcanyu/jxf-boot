INSERT INTO users (username, password, enabled) VALUES 
('admin', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', true),
('user', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', true);

INSERT INTO authorities (username, authority) VALUES 
('admin', 'ROLE_USER'),
('admin', 'ROLE_ADMIN'),
('user', 'ROLE_USER');
