-- Tạo database
CREATE DATABASE IF NOT EXISTS thietkethucdonapp
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE thietkethucdonapp;

-- Bảng nguyên liệu
CREATE TABLE nguyenlieu (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_nguyen_lieu VARCHAR(100) NOT NULL,
    don_vi_tinh VARCHAR(20) DEFAULT 'g',
    don_gia DECIMAL(10, 2) NOT NULL
);

-- Bảng món ăn
CREATE TABLE monan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_mon VARCHAR(100) NOT NULL,
    loai_mon ENUM('sang', 'trua', 'xe') NOT NULL,
    cach_che_bien TEXT
);

-- Bảng công thức món ăn (liên kết món ăn với nguyên liệu)
CREATE TABLE congthucmonan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    mon_an_id INT NOT NULL,
    nguyen_lieu_id INT NOT NULL,
    khoi_luong DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (mon_an_id) REFERENCES monan(id),
    FOREIGN KEY (nguyen_lieu_id) REFERENCES nguyenlieu(id)
);

-- Bảng thực đơn
CREATE TABLE thucdon (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_thuc_don VARCHAR(100) NOT NULL,
    so_ngay INT NOT NULL
);

-- Bảng chi tiết thực đơn (món ăn cho từng bữa của mỗi ngày)
CREATE TABLE chitietthucdon (
    id INT PRIMARY KEY AUTO_INCREMENT,
    thuc_don_id INT NOT NULL,
    ngay INT NOT NULL,
    buoi ENUM('sang', 'trua', 'xe') NOT NULL,
    mon_an_id INT NOT NULL,
    FOREIGN KEY (thuc_don_id) REFERENCES thucdon(id),
    FOREIGN KEY (mon_an_id) REFERENCES monan(id)
);