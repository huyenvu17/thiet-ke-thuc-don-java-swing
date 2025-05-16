-- Tạo database
CREATE DATABASE IF NOT EXISTS thietkethucdonapp
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE thietkethucdonapp;

-- Xóa các bảng nếu đã tồn tại (theo thứ tự ngược của quan hệ khóa ngoại)
DROP TABLE IF EXISTS chitietthucdon;
DROP TABLE IF EXISTS thucdon;
DROP TABLE IF EXISTS congthucmonan;
DROP TABLE IF EXISTS monan;
DROP TABLE IF EXISTS nguyenlieu;
DROP TABLE IF EXISTS nhomthucpham;
DROP TABLE IF EXISTS user;

-- Bảng nhóm thực phẩm
CREATE TABLE nhomthucpham (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_nhom VARCHAR(100) NOT NULL,
    mo_ta TEXT
);

-- Bảng nguyên liệu
CREATE TABLE nguyenlieu (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten_nguyen_lieu VARCHAR(100) NOT NULL,
    khoi_luong DOUBLE(20, 2) NOT NULL,
    don_gia INT NOT NULL,
    nhom_thuc_pham_id INT NOT NULL,
    FOREIGN KEY (nhom_thuc_pham_id) REFERENCES nhomthucpham(id)
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

-- Bảng user cho chức năng đăng nhập/đăng ký và quản lý hồ sơ
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    role ENUM('admin', 'user') NOT NULL DEFAULT 'user'
);

-- Thêm dữ liệu vào nhóm thực phẩm
INSERT INTO nhomthucpham (ten_nhom, mo_ta) VALUES
('Chất bột đường', 'Nhóm thực phẩm chứa nhiều carbohydrate'),
('Chất đạm', 'Nhóm thực phẩm giàu protein'),
('Chất béo', 'Nhóm thực phẩm chứa nhiều chất béo'),
('Vitamin và khoáng chất', 'Nhóm thực phẩm giàu vitamin');

-- Thêm dữ liệu mẫu vào bảng nguyên liệu 
INSERT INTO nguyenlieu (ten_nguyen_lieu, nhom_thuc_pham_id, khoi_luong, don_gia) VALUES
-- Nhóm chất bột đường
('Gạo', 1, 1.00, 15000.00),
('Đậu xanh', 1, 1.00, 20000.00),
('Gạo Jasmine', 1, 1.00, 30927.00),
('Gạo nếp cái hoa vàng', 1, 1.00, 36615.00),
('Gạo ST24', 1, 1.00, 9836.00),
('Bí đỏ giống Nhật', 1, 1.00, 8341.00),
('Đậu đen', 1, 1.00, 19516.00),
('Khoai lang tím', 1, 1.00, 5290.00),
('Bắp non', 1, 1.00, 4915.00),
('Ngô nếp', 1, 1.00, 9897.00),

-- Nhóm chất đạm
('Thịt gà', 2, 1.00, 35000.00),
('Cá hồi', 2, 1.00, 50000.00),
('Thịt bò Úc', 2, 1.00, 371560.00),
('Cá hồi Chile', 2, 1.00, 494600.00),
('Thịt bò Mỹ', 2, 1.00, 670700.00),
('Mực nang', 2, 1.00, 812860.00),
('Thịt heo nạc', 2, 1.00, 64445.00),
('Thịt gà ta', 2, 1.00, 37532.00),
('Cá hồi Na Uy', 2, 1.00, 56890.00),
('Tôm sú', 2, 1.00, 648770.00),
('Mực ống', 2, 1.00, 127570.00),
('Thịt heo ba chỉ', 2, 1.00, 721540.00),

-- Nhóm chất béo
('Dầu Tường An', 3, 1.00, 71331.00),

-- Nhóm vitamin và khoáng chất
('Cà rốt', 4, 1.00, 10000.00),
('Rau dền', 4, 1.00, 8790.00),
('Táo Envy', 4, 1.00, 67686.00),
('Sữa tươi thanh trùng', 4, 1.00, 8616.00),
('Nấm rơm', 4, 1.00, 36953.00),
('Chuối cau', 4, 1.00, 74414.00),
('Cà chua beef', 4, 1.00, 7475.00),
('Khoai tây Đà Lạt', 4, 1.00, 80421.00),
('Đậu phụ trắng', 4, 1.00, 77129.00),
('Đậu nành', 4, 1.00, 19173.00),
('Ngô Mỹ', 4, 1.00, 16667.00),
('Sữa chua', 4, 1.00, 41380.00),
('Bắp cải tím', 4, 1.00, 46874.00),
('Nấm hương', 4, 1.00, 39635.00),
('Nấm đùi gà', 4, 1.00, 17753.00),
('Bí đỏ hồ lô', 4, 1.00, 76761.00),
('Nấm kim châm', 4, 1.00, 13092.00),
('Cam sành', 4, 1.00, 32208.00),
('Cam Cara ruột đỏ', 4, 1.00, 30484.00),
('Sữa tươi tiệt trùng', 4, 1.00, 34715.00),
('Táo Fuji', 4, 1.00, 22352.00),
('Tôm thẻ', 4, 1.00, 55447.00),
('Rau muống', 4, 1.00, 13060.00),
('Chuối già Nam Mỹ', 4, 1.00, 50933.00),
('Bắp cải trắng', 4, 1.00, 94227.00),
('Phô mai', 4, 1.00, 71331.00);

-- Thêm dữ liệu mẫu cho bảng món ăn
INSERT INTO monan (ten_mon, loai_mon, cach_che_bien) VALUES
-- Món ăn buổi sáng
('Phở gà', 'sang', 'Nấu nước dùng từ xương gà, thêm gia vị, chan nước dùng vào bánh phở, ăn kèm với thịt gà xé, hành, rau mùi'),
('Bánh mì trứng', 'sang', 'Chiên trứng, kẹp vào bánh mì cùng với rau, dưa chuột'),
('Cháo gà', 'sang', 'Nấu gạo với nước dùng gà đến khi nhừ, thêm thịt gà xé, hành phi, tiêu'),
('Bún riêu cua', 'sang', 'Nấu riêu cua với cà chua, thêm đậu hũ, mắm tôm, rau sống'),
('Xôi gà', 'sang', 'Hấp xôi, ăn kèm với thịt gà xé, hành phi'),

-- Món ăn buổi trưa
('Cơm gà', 'trua', 'Nấu cơm từ gạo thơm, ăn với thịt gà luộc, nước mắm pha, dưa chuột'),
('Bún bò', 'trua', 'Nấu nước dùng từ xương bò, thêm sả, gừng, chan vào bún, ăn kèm với thịt bò'),
('Cơm sườn', 'trua', 'Nấu cơm, ăn kèm với sườn nướng, canh rau'),
('Bánh canh cá lóc', 'trua', 'Nấu bánh canh với nước dùng cá, thêm thịt cá lóc'),
('Hủ tiếu nam vang', 'trua', 'Nấu nước dùng xương, thêm tôm, thịt, gan heo, chan vào hủ tiếu'),

-- Món ăn buổi xế
('Chè đậu xanh', 'xe', 'Nấu đậu xanh với đường, thêm nước cốt dừa'),
('Sinh tố bơ', 'xe', 'Xay bơ với sữa và đường'),
('Bánh flan', 'xe', 'Làm caramel, hấp hỗn hợp trứng sữa đường'),
('Chè bắp', 'xe', 'Nấu bắp với đường, thêm nước cốt dừa'),
('Sữa chua nếp cẩm', 'xe', 'Hầm nếp cẩm với đường, ăn kèm sữa chua');

-- Thêm dữ liệu mẫu cho công thức món ăn
INSERT INTO congthucmonan (mon_an_id, nguyen_lieu_id, khoi_luong) VALUES
-- Công thức cho Phở gà
(1, 11, 150.00), -- Thịt gà 150g
(1, 1, 100.00),  -- Gạo (để làm bánh phở) 100g

-- Công thức cho Bánh mì trứng
(2, 21, 50.00),  -- Phô mai 50g

-- Công thức cho Cháo gà
(3, 1, 50.00),   -- Gạo 50g
(3, 11, 100.00), -- Thịt gà 100g

-- Công thức cho Bún riêu cua
(4, 28, 100.00), -- Cà chua beef 100g
(4, 30, 200.00), -- Đậu phụ trắng 200g

-- Công thức cho Xôi gà
(5, 4, 200.00),  -- Gạo nếp cái hoa vàng 200g
(5, 11, 150.00), -- Thịt gà 150g

-- Công thức cho Cơm gà
(6, 1, 200.00),  -- Gạo 200g
(6, 11, 200.00), -- Thịt gà 200g

-- Công thức cho Bún bò
(7, 13, 200.00), -- Thịt bò Úc 200g

-- Công thức cho Cơm sườn
(8, 1, 200.00),  -- Gạo 200g
(8, 21, 100.00), -- Thịt heo (giả định là phô mai để đơn giản) 100g

-- Công thức cho Bánh canh cá lóc
(9, 14, 200.00), -- Cá hồi (thay cho cá lóc) 200g

-- Công thức cho Hủ tiếu nam vang
(10, 1, 150.00), -- Gạo 150g
(10, 19, 100.00), -- Tôm sú 100g

-- Công thức cho Chè đậu xanh
(11, 2, 100.00), -- Đậu xanh 100g

-- Công thức cho Sinh tố bơ
(12, 27, 100.00), -- Sữa tươi tiệt trùng 100g

-- Công thức cho Bánh flan
(13, 27, 100.00), -- Sữa tươi tiệt trùng 100g

-- Công thức cho Chè bắp
(14, 9, 150.00), -- Bắp non 150g

-- Công thức cho Sữa chua nếp cẩm
(15, 4, 100.00), -- Gạo nếp cái hoa vàng 100g
(15, 26, 150.00); -- Sữa chua 150g

-- Thêm dữ liệu mẫu cho thực đơn
INSERT INTO thucdon (ten_thuc_don, so_ngay) VALUES
('Thực đơn mùa hè', 7),
('Thực đơn tiết kiệm', 5),
('Thực đơn cuối tuần', 2);

-- Thêm dữ liệu mẫu cho chi tiết thực đơn
INSERT INTO chitietthucdon (thuc_don_id, ngay, buoi, mon_an_id) VALUES
-- Thực đơn mùa hè - Ngày 1
(1, 1, 'sang', 1), -- Phở gà
(1, 1, 'trua', 6), -- Cơm gà
(1, 1, 'xe', 11),  -- Chè đậu xanh

-- Thực đơn mùa hè - Ngày 2
(1, 2, 'sang', 2), -- Bánh mì trứng
(1, 2, 'trua', 7), -- Bún bò
(1, 2, 'xe', 12),  -- Sinh tố bơ

-- Thực đơn tiết kiệm - Ngày 1
(2, 1, 'sang', 3), -- Cháo gà
(2, 1, 'trua', 8), -- Cơm sườn
(2, 1, 'xe', 13),  -- Bánh flan

-- Thực đơn cuối tuần - Ngày 1
(3, 1, 'sang', 4), -- Bún riêu cua
(3, 1, 'trua', 9), -- Bánh canh cá lóc
(3, 1, 'xe', 14);  -- Chè bắp

-- Tạo các VIEW
-- Xóa VIEW nếu đã tồn tại
DROP VIEW IF EXISTS vw_nhomthucpham;
DROP VIEW IF EXISTS vw_nguyenlieu_theotennhom;
DROP VIEW IF EXISTS vw_thongke_nguyenlieu_theonhom;

-- View hiển thị nhóm thực phẩm
CREATE VIEW vw_nhomthucpham AS
SELECT id, ten_nhom, mo_ta FROM nhomthucpham;

-- View hiển thị nguyên liệu kèm theo tên nhóm thực phẩm
CREATE VIEW vw_nguyenlieu_theotennhom AS
SELECT n.id, n.ten_nguyen_lieu, n.khoi_luong, n.don_gia, n.nhom_thuc_pham_id, nt.ten_nhom
FROM nguyenlieu n
JOIN nhomthucpham nt ON n.nhom_thuc_pham_id = nt.id;

-- View thống kê số lượng nguyên liệu theo từng nhóm
CREATE VIEW vw_thongke_nguyenlieu_theonhom AS
SELECT nt.id, nt.ten_nhom, COUNT(n.id) AS so_luong_nguyen_lieu,
       AVG(n.don_gia) AS don_gia_trung_binh
FROM nhomthucpham nt
LEFT JOIN nguyenlieu n ON nt.id = n.nhom_thuc_pham_id
GROUP BY nt.id, nt.ten_nhom;

-- Tạo tài khoản admin mặc định
INSERT INTO user (username, password, full_name, email, phone, role)
VALUES ('admin', 'admin', 'Quản trị viên', 'admin@example.com', '0123456789', 'admin')
ON DUPLICATE KEY UPDATE username=username;