-- Tạo database
CREATE DATABASE IF NOT EXISTS thietkethucdonapp
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE thietkethucdonapp;

-- Xóa các bảng nếu đã tồn tại
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

-- Bảng user
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
('Bột mì', 1, 1.00, 12000.00),
('Bún tươi', 1, 1.00, 25000.00),
('Miến dong', 1, 1.00, 30000.00),
('Mì trứng', 1, 1.00, 15000.00),

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
('Trứng gà', 2, 1.00, 30000.00),
('Cá ngừ', 2, 1.00, 120000.00),
('Cá chép', 2, 1.00, 70000.00),
('Cá diêu hồng', 2, 1.00, 90000.00),
('Thịt vịt', 2, 1.00, 65000.00),
('Thịt cừu', 2, 1.00, 300000.00),
('Tôm thẻ', 2, 1.00, 55447.00),

-- Nhóm chất béo
('Dầu Tường An', 3, 1.00, 71331.00),
('Bơ thực vật', 3, 1.00, 80000.00),
('Dầu olive', 3, 1.00, 120000.00),
('Dầu mè', 3, 1.00, 90000.00),
('Dầu đậu nành', 3, 1.00, 70000.00),

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
('Rau muống', 4, 1.00, 13060.00),
('Chuối già Nam Mỹ', 4, 1.00, 50933.00),
('Bắp cải trắng', 4, 1.00, 94227.00),
('Phô mai', 4, 1.00, 71331.00),
('Rau cải xanh', 4, 1.00, 12000.00),
('Rau mùi', 4, 1.00, 8000.00),
('Hành lá', 4, 1.00, 7000.00),
('Dứa', 4, 1.00, 15000.00),
('Xoài', 4, 1.00, 25000.00),
('Ổi', 4, 1.00, 18000.00),
('Bánh phở', 1, 1.00, 20000.00),
('Bánh mì', 1, 1.00, 8000.00),
('Dưa chuột', 4, 1.00, 12000.00),
('Hành tây', 4, 1.00, 15000.00),
('Gừng', 4, 1.00, 40000.00),
('Sả', 4, 1.00, 25000.00),
('Bơ', 4, 1.00, 80000.00),
('Nước cốt dừa', 4, 1.00, 35000.00),
('Đường', 1, 1.00, 18000.00),
('Muối', 4, 1.00, 5000.00),
('Nước mắm', 4, 1.00, 30000.00),
('Tương ớt', 4, 1.00, 25000.00),
('Hạt sen', 4, 1.00, 120000.00),
('Thạch rau câu', 1, 1.00, 15000.00),
('Đậu Hà Lan', 4, 1.00, 35000.00),
('Xá xíu', 2, 1.00, 80000.00),
('Gan heo', 2, 1.00, 45000.00),
('Cua', 2, 1.00, 150000.00),
('Nấm mèo', 4, 1.00, 25000.00),
('Tiêu', 4, 1.00, 200000.00),
('Mắm tôm', 4, 1.00, 40000.00);

-- Thêm dữ liệu mẫu cho bảng món ăn
INSERT INTO monan (ten_mon, loai_mon, cach_che_bien) VALUES
-- Món ăn buổi sáng
('Phở gà', 'sang', 'Nấu nước dùng từ xương gà, thêm gia vị, chan nước dùng vào bánh phở, ăn kèm với thịt gà xé, hành, rau mùi'),
('Bánh mì trứng', 'sang', 'Chiên trứng, kẹp vào bánh mì cùng với rau, dưa chuột'),
('Cháo gà', 'sang', 'Nấu gạo với nước dùng gà đến khi nhừ, thêm thịt gà xé, hành phi, tiêu'),
('Bún riêu cua', 'sang', 'Nấu riêu cua với cà chua, thêm đậu hũ, mắm tôm, rau sống'),
('Xôi gà', 'sang', 'Hấp xôi, ăn kèm với thịt gà xé, hành phi'),
('Bánh cuốn thịt', 'sang', 'Làm bánh cuốn từ bột gạo, nhân thịt heo xay và nấm mèo'),
('Bún bò Huế', 'sang', 'Nấu nước dùng từ xương bò và sả, ăn kèm với thịt bò, bún'),
('Bánh canh tôm', 'sang', 'Nấu bánh canh với nước dùng tôm, thêm tôm tươi'),
('Miến gà', 'sang', 'Nấu miến với nước dùng gà, thêm thịt gà xé, nấm hương'),
('Bánh bao nhân thịt', 'sang', 'Làm vỏ bánh bao từ bột mì, nhân thịt heo'),

-- Món ăn buổi trưa
('Cơm gà', 'trua', 'Nấu cơm từ gạo thơm, ăn với thịt gà luộc, nước mắm pha, dưa chuột'),
('Bún bò', 'trua', 'Nấu nước dùng từ xương bò, thêm sả, gừng, chan vào bún, ăn kèm với thịt bò'),
('Cơm sườn', 'trua', 'Nấu cơm, ăn kèm với sườn nướng, canh rau'),
('Bánh canh cá lóc', 'trua', 'Nấu bánh canh với nước dùng cá, thêm thịt cá lóc'),
('Hủ tiếu nam vang', 'trua', 'Nấu nước dùng xương, thêm tôm, thịt, gan heo, chan vào hủ tiếu'),
('Cơm tấm sườn', 'trua', 'Nấu cơm tấm, ăn kèm với sườn nướng, bì, chả, trứng'),
('Bún chả', 'trua', 'Bún ăn kèm chả thịt heo nướng, nước mắm pha'),
('Cơm chiên dương châu', 'trua', 'Cơm chiên với trứng, thịt xá xíu, đậu Hà Lan, cà rốt'),
('Mì xào hải sản', 'trua', 'Mì trứng xào với hải sản, rau cải'),
('Cơm gà xối mỡ', 'trua', 'Cơm ăn kèm với gà chiên vàng, dưa chuột'),

-- Món ăn buổi xế
('Chè đậu xanh', 'xe', 'Nấu đậu xanh với đường, thêm nước cốt dừa'),
('Sinh tố bơ', 'xe', 'Xay bơ với sữa và đường'),
('Bánh flan', 'xe', 'Làm caramel, hấp hỗn hợp trứng sữa đường'),
('Chè bắp', 'xe', 'Nấu bắp với đường, thêm nước cốt dừa'),
('Sữa chua nếp cẩm', 'xe', 'Hầm nếp cẩm với đường, ăn kèm sữa chua'),
('Bánh đúc lá dứa', 'xe', 'Làm bánh đúc từ bột gạo và lá dứa, ăn kèm nước đường'),
('Xôi vò', 'xe', 'Làm xôi từ gạo nếp và đậu xanh'),
('Chè thái', 'xe', 'Nấu chè với các loại trái cây, thạch, đậu đỏ'),
('Bánh chuối nướng', 'xe', 'Làm bánh từ chuối, bột mì, nướng trong lò'),
('Chè hạt sen', 'xe', 'Nấu hạt sen với đường phèn'),
('Tôm thẻ xào rau muống', 'trua', 'Tôm thẻ xào với rau muống, tỏi, hành');

-- Thêm dữ liệu mẫu cho công thức món ăn
INSERT INTO congthucmonan (mon_an_id, nguyen_lieu_id, khoi_luong) VALUES
-- Công thức cho Phở gà (ID 1) - "Nấu nước dùng từ xương gà, thêm gia vị, chan nước dùng vào bánh phở, ăn kèm với thịt gà xé, hành, rau mùi"
(1, 63, 200.00), -- Bánh phở 200g
(1, 15, 150.00), -- Thịt gà 150g  
(1, 58, 10.00),  -- Hành lá 10g
(1, 57, 10.00),  -- Rau mùi 10g
(1, 73, 5.00),   -- Nước mắm 5ml
(1, 72, 3.00),   -- Muối 3g

-- Công thức cho Bánh mì trứng (ID 2) - "Chiên trứng, kẹp vào bánh mì cùng với rau, dưa chuột"
(2, 64, 100.00), -- Bánh mì 100g
(2, 28, 100.00), -- Trứng gà 100g
(2, 65, 30.00),  -- Dưa chuột 30g
(2, 56, 20.00),  -- Rau cải xanh 20g
(2, 34, 10.00),  -- Dầu Tường An 10ml

-- Công thức cho Cháo gà (ID 3) - "Nấu gạo với nước dùng gà đến khi nhừ, thêm thịt gà xé, hành phi, tiêu"
(3, 1, 100.00),  -- Gạo 100g
(3, 15, 150.00), -- Thịt gà 150g
(3, 58, 10.00),  -- Hành lá 10g
(3, 84, 2.00),   -- Tiêu 2g
(3, 73, 5.00),   -- Nước mắm 5ml

-- Công thức cho Bún riêu cua (ID 4) - "Nấu riêu cua với cà chua, thêm đậu hũ, mắm tôm, rau sống"
(4, 12, 200.00), -- Bún tươi 200g
(4, 82, 150.00), -- Cua 150g
(4, 46, 100.00), -- Cà chua beef 100g
(4, 48, 100.00), -- Đậu phụ trắng 100g
(4, 85, 10.00),  -- Mắm tôm 10ml
(4, 56, 50.00),  -- Rau cải xanh 50g

-- Công thức cho Xôi gà (ID 5) - "Hấp xôi, ăn kèm với thịt gà xé, hành phi"
(5, 4, 250.00),  -- Gạo nếp cái hoa vàng 250g
(5, 15, 150.00), -- Thịt gà 150g
(5, 58, 10.00),  -- Hành lá 10g
(5, 34, 20.00),  -- Dầu Tường An 20ml

-- Công thức cho Bánh cuốn thịt (ID 6) - "Làm bánh cuốn từ bột gạo, nhân thịt heo xay và nấm mèo"
(6, 1, 200.00),  -- Gạo (bột) 200g
(6, 21, 150.00), -- Thịt heo nạc 150g
(6, 83, 50.00),  -- Nấm mèo 50g
(6, 58, 10.00),  -- Hành lá 10g
(6, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Bún bò Huế (ID 7) - "Nấu nước dùng từ xương bò và sả, ăn kèm với thịt bò, bún"
(7, 12, 200.00), -- Bún tươi 200g
(7, 17, 200.00), -- Thịt bò Úc 200g
(7, 68, 20.00),  -- Sả 20g
(7, 67, 10.00),  -- Gừng 10g
(7, 74, 10.00),  -- Tương ớt 10ml
(7, 56, 50.00),  -- Rau cải xanh 50g

-- Công thức cho Bánh canh tôm (ID 8) - "Nấu bánh canh với nước dùng tôm, thêm tôm tươi"
(8, 1, 150.00),  -- Gạo (bột bánh canh) 150g
(8, 24, 200.00), -- Tôm sú 200g
(8, 58, 10.00),  -- Hành lá 10g
(8, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Miến gà (ID 9) - "Nấu miến với nước dùng gà, thêm thịt gà xé, nấm hương"
(9, 13, 150.00), -- Miến dong 150g
(9, 15, 150.00), -- Thịt gà 150g
(9, 53, 50.00),  -- Nấm hương 50g
(9, 58, 10.00),  -- Hành lá 10g
(9, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Bánh bao nhân thịt (ID 10) - "Làm vỏ bánh bao từ bột mì, nhân thịt heo"
(10, 11, 200.00), -- Bột mì 200g
(10, 21, 150.00), -- Thịt heo nạc 150g
(10, 66, 10.00),  -- Hành tây 10g
(10, 72, 5.00),   -- Muối 5g

-- Công thức cho Cơm gà (ID 11) - "Nấu cơm từ gạo thơm, ăn với thịt gà luộc, nước mắm pha, dưa chuột"
(11, 3, 200.00),  -- Gạo Jasmine 200g
(11, 15, 200.00), -- Thịt gà 200g
(11, 65, 50.00),  -- Dưa chuột 50g
(11, 73, 15.00),  -- Nước mắm 15ml

-- Công thức cho Bún bò (ID 12) - "Nấu nước dùng từ xương bò, thêm sả, gừng, chan vào bún, ăn kèm với thịt bò"
(12, 12, 200.00), -- Bún tươi 200g
(12, 17, 200.00), -- Thịt bò Úc 200g
(12, 68, 20.00),  -- Sả 20g
(12, 67, 10.00),  -- Gừng 10g
(12, 46, 50.00),  -- Cà chua beef 50g

-- Công thức cho Cơm sườn (ID 13) - "Nấu cơm, ăn kèm với sườn nướng, canh rau"
(13, 1, 200.00),  -- Gạo 200g
(13, 26, 250.00), -- Thịt heo ba chỉ (sườn) 250g
(13, 56, 100.00), -- Rau cải xanh 100g
(13, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Bánh canh cá lóc (ID 14) - "Nấu bánh canh với nước dùng cá, thêm thịt cá lóc"
(14, 1, 150.00),  -- Gạo (bột bánh canh) 150g
(14, 30, 200.00), -- Cá diêu hồng (thay cá lóc) 200g
(14, 58, 10.00),  -- Hành lá 10g
(14, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Hủ tiếu nam vang (ID 15) - "Nấu nước dùng xương, thêm tôm, thịt, gan heo, chan vào hủ tiếu"
(15, 1, 200.00),  -- Gạo (hủ tiếu) 200g
(15, 24, 100.00), -- Tôm sú 100g
(15, 21, 100.00), -- Thịt heo nạc 100g
(15, 81, 50.00),  -- Gan heo 50g
(15, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Cơm tấm sườn (ID 16) - "Nấu cơm tấm, ăn kèm với sườn nướng, bì, chả, trứng"
(16, 5, 200.00),  -- Gạo ST24 (cơm tấm) 200g
(16, 26, 200.00), -- Thịt heo ba chỉ (sườn) 200g
(16, 28, 50.00),  -- Trứng gà 50g
(16, 73, 10.00),  -- Nước mắm 10ml

-- Công thức cho Bún chả (ID 17) - "Bún ăn kèm chả thịt heo nướng, nước mắm pha"
(17, 12, 200.00), -- Bún tươi 200g
(17, 21, 200.00), -- Thịt heo nạc 200g
(17, 73, 20.00),  -- Nước mắm 20ml
(17, 71, 10.00),  -- Đường 10g
(17, 65, 30.00),  -- Dưa chuột 30g

-- Công thức cho Cơm chiên dương châu (ID 18) - "Cơm chiên với trứng, thịt xá xíu, đậu Hà Lan, cà rốt"
(18, 3, 250.00),  -- Gạo Jasmine 250g
(18, 28, 100.00), -- Trứng gà 100g
(18, 80, 100.00), -- Xá xíu 100g
(18, 78, 50.00),  -- Đậu Hà Lan 50g
(18, 39, 50.00),  -- Cà rốt 50g
(18, 34, 30.00),  -- Dầu Tường An 30ml

-- Công thức cho Mì xào hải sản (ID 19) - "Mì trứng xào với hải sản, rau cải"
(19, 14, 200.00), -- Mì trứng 200g
(19, 24, 100.00), -- Tôm sú 100g
(19, 25, 100.00), -- Mực ống 100g
(19, 56, 100.00), -- Rau cải xanh 100g
(19, 34, 30.00),  -- Dầu Tường An 30ml

-- Công thức cho Cơm gà xối mỡ (ID 20) - "Cơm ăn kèm với gà chiên vàng, dưa chuột"
(20, 3, 200.00),  -- Gạo Jasmine 200g
(20, 15, 250.00), -- Thịt gà 250g
(20, 65, 50.00),  -- Dưa chuột 50g
(20, 34, 50.00),  -- Dầu Tường An 50ml

-- Công thức cho Chè đậu xanh (ID 21) - "Nấu đậu xanh với đường, thêm nước cốt dừa"
(21, 2, 150.00),  -- Đậu xanh 150g
(21, 71, 50.00),  -- Đường 50g
(21, 70, 100.00), -- Nước cốt dừa 100ml

-- Công thức cho Sinh tố bơ (ID 22) - "Xay bơ với sữa và đường"
(22, 69, 200.00), -- Bơ 200g
(22, 50, 200.00), -- Sữa tươi tiệt trùng 200ml
(22, 71, 30.00),  -- Đường 30g

-- Công thức cho Bánh flan (ID 23) - "Làm caramel, hấp hỗn hợp trứng sữa đường"
(23, 28, 150.00), -- Trứng gà 150g
(23, 50, 200.00), -- Sữa tươi tiệt trùng 200ml
(23, 71, 80.00),  -- Đường 80g

-- Công thức cho Chè bắp (ID 24) - "Nấu bắp với đường, thêm nước cốt dừa"
(24, 9, 200.00),  -- Bắp non 200g
(24, 71, 50.00),  -- Đường 50g
(24, 70, 100.00), -- Nước cốt dừa 100ml

-- Công thức cho Sữa chua nếp cẩm (ID 25) - "Hầm nếp cẩm với đường, ăn kèm sữa chua"
(25, 4, 150.00),  -- Gạo nếp cái hoa vàng 150g
(25, 51, 200.00), -- Sữa chua 200g
(25, 71, 50.00),  -- Đường 50g

-- Công thức cho Bánh đúc lá dứa (ID 26) - "Làm bánh đúc từ bột gạo và lá dứa, ăn kèm nước đường"
(26, 1, 200.00),  -- Gạo (bột) 200g
(26, 59, 50.00),  -- Dứa (thay lá dứa) 50g
(26, 71, 100.00), -- Đường 100g
(26, 70, 50.00),  -- Nước cốt dừa 50ml

-- Công thức cho Xôi vò (ID 27) - "Làm xôi từ gạo nếp và đậu xanh"
(27, 4, 200.00),  -- Gạo nếp cái hoa vàng 200g
(27, 2, 100.00),  -- Đậu xanh 100g
(27, 70, 50.00),  -- Nước cốt dừa 50ml

-- Công thức cho Chè thái (ID 28) - "Nấu chè với các loại trái cây, thạch, đậu đỏ"
(28, 59, 100.00), -- Dứa 100g
(28, 60, 100.00), -- Xoài 100g
(28, 77, 100.00), -- Thạch rau câu 100g
(28, 2, 50.00),   -- Đậu xanh (thay đậu đỏ) 50g
(28, 71, 50.00),  -- Đường 50g
(28, 70, 100.00), -- Nước cốt dừa 100ml

-- Công thức cho Bánh chuối nướng (ID 29) - "Làm bánh từ chuối, bột mì, nướng trong lò"
(29, 45, 300.00), -- Chuối cau 300g
(29, 11, 150.00), -- Bột mì 150g
(29, 28, 100.00), -- Trứng gà 100g
(29, 71, 50.00),  -- Đường 50g
(29, 35, 50.00),  -- Bơ thực vật 50g

-- Công thức cho Chè hạt sen (ID 30) - "Nấu hạt sen với đường phèn"
(30, 75, 150.00), -- Hạt sen 150g
(30, 71, 80.00),  -- Đường 80g
(30, 70, 100.00), -- Nước cốt dừa 100ml

-- Công thức cho Tôm thẻ xào rau muống (ID 31) - "Tôm thẻ xào với rau muống, tỏi, hành"
(31, 33, 200.00), -- Tôm thẻ 200g
(31, 52, 150.00), -- Rau muống 150g
(31, 58, 10.00),  -- Hành lá 10g
(31, 34, 20.00),  -- Dầu Tường An 20ml
(31, 73, 10.00);  -- Nước mắm 10ml

-- Thêm dữ liệu mẫu cho thực đơn
INSERT INTO thucdon (ten_thuc_don, so_ngay) VALUES
('Thực đơn mùa hè', 7),
('Thực đơn tiết kiệm', 5),
('Thực đơn cuối tuần', 2);

-- Thêm dữ liệu mẫu cho chi tiết thực đơn
INSERT INTO chitietthucdon (thuc_don_id, ngay, buoi, mon_an_id) VALUES
-- Thực đơn mùa hè - Ngày 1
(1, 1, 'sang', 1), -- Phở gà
(1, 1, 'trua', 11), -- Cơm gà
(1, 1, 'xe', 21),  -- Chè đậu xanh

-- Thực đơn mùa hè - Ngày 2
(1, 2, 'sang', 2), -- Bánh mì trứng
(1, 2, 'trua', 12), -- Bún bò
(1, 2, 'xe', 22),  -- Sinh tố bơ

-- Thực đơn tiết kiệm - Ngày 1
(2, 1, 'sang', 3), -- Cháo gà
(2, 1, 'trua', 13), -- Cơm sườn
(2, 1, 'xe', 23),  -- Bánh flan

-- Thực đơn cuối tuần - Ngày 1
(3, 1, 'sang', 4), -- Bún riêu cua
(3, 1, 'trua', 14), -- Bánh canh cá lóc
(3, 1, 'xe', 24);  -- Chè bắp

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
VALUES ('admin', 'admin', 'Quản trị viên', 'admin@gmail.com', '0123456789', 'admin')
ON DUPLICATE KEY UPDATE username=username;