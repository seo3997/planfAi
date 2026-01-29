-- User Table
CREATE TABLE IF NOT EXISTS tb_user (
    user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Profit Reports Table
CREATE TABLE IF NOT EXISTS profit_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_no BIGINT NOT NULL,
    report_data JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_no FOREIGN KEY (user_no) REFERENCES tb_user(user_no) ON DELETE CASCADE
);
