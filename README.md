# Hệ Thống Thiết Kế Thực Đơn Cho Trẻ Mầm Non

## Giới Thiệu
Ứng dụng này được thiết kế để giúp tạo và quản lý thực đơn cho trẻ mầm non. Cho phép người dùng định nghĩa nguyên liệu, tạo các món ăn và tạo thực đơn cân bằng dinh dưỡng trong giới hạn ngân sách. Hệ thống đặc biệt tập trung vào việc thiết kế các bữa ăn bổ dưỡng và phù hợp với chi phí cho các cơ sở giáo dục mầm non.

## Tính Năng Chính
- **Quản Lý Nguyên Liệu**: Thêm, chỉnh sửa và theo dõi nguyên liệu với thông tin giá cả
- **Tạo Món Ăn**: Tạo và phân loại món ăn cho bữa sáng, bữa trưa và bữa xế
- **Thiết Kế Thực Đơn**: Tạo thực đơn cho số ngày được chỉ định với giới hạn ngân sách
- **Danh Mục Thực Đơn**: Xem và quản lý các thực đơn đã lưu
- **Tính Toán Chi Phí**: Tự động tính toán chi phí bữa ăn dựa trên nguyên liệu

## Cấu Trúc Cơ Sở Dữ Liệu
Ứng dụng sử dụng cơ sở dữ liệu MySQL với các bảng chính sau:
- **nguyenlieu**: Lưu trữ thông tin về nguyên liệu thực phẩm, đơn vị và giá cả
- **monan**: Chứa chi tiết món ăn bao gồm tên, loại và hướng dẫn chế biến
- **congthucmonan**: Ánh xạ nguyên liệu với món ăn và số lượng cụ thể
- **thucdon**: Lưu trữ các thực đơn đã tạo với tên và thời gian
- **chitietthucdon**: Chứa các món ăn cụ thể được gán cho mỗi bữa ăn của mỗi ngày

## Công Cụ và Thư Viện
- **Java 17**: Ngôn ngữ lập trình cốt lõi
- **Maven**: Công cụ quản lý và xây dựng dự án
- **MySQL**: Hệ thống quản lý cơ sở dữ liệu
- **MySQL Connector/J**: Trình điều khiển JDBC cho MySQL (phiên bản 9.2.0)
- **Swing**: Bộ công cụ giao diện người dùng Java

## Kiến Trúc
Ứng dụng tuân theo kiến trúc phân lớp:
- **Lớp Giao Diện**: Các thành phần giao diện người dùng dựa trên Swing
- **Lớp Điều Khiển**: Xử lý đầu vào người dùng và điều phối các hành động
- **Lớp Dịch Vụ**: Chứa logic nghiệp vụ cho việc tạo thực đơn
- **Lớp DAO**: Các đối tượng truy cập dữ liệu cho các thao tác cơ sở dữ liệu
- **Lớp Thực Thể**: Các đối tượng mô hình dữ liệu đại diện cho các bảng cơ sở dữ liệu

## Quy Trình Cài Đặt

### Yêu Cầu
- Java Development Kit (JDK) 17 trở lên
- MySQL 8.0 trở lên
- Maven 3.8 trở lên

### Cài Đặt Cơ Sở Dữ Liệu
1. Cài đặt MySQL nếu chưa có
2. Tạo cơ sở dữ liệu có tên `thietkethucdonapp`
3. Thực thi script SQL `thietkethucdon_db.sql` để tạo các bảng cần thiết

### Cài Đặt Ứng Dụng
1. Sao chép repository
2. Cập nhật thông số kết nối cơ sở dữ liệu trong `src/main/java/dao/DatabaseConnection.java` nếu cần
   - Cài đặt mặc định:
     - Host: localhost (127.0.0.1)
     - Database: thietkethucdonapp
     - User: root
     - Password: 111111
3. Xây dựng dự án bằng Maven:
   ```
   mvn clean package
   ```
4. Chạy ứng dụng:
   ```
   java -jar target/DoAnThietKeThucDon-1.0-SNAPSHOT.jar
   ```
   Hoặc sử dụng plugin Maven exec:
   ```
   mvn exec:java
   ```

## Cách Sử Dụng
1. Bắt đầu bằng cách thêm nguyên liệu trong phần Quản lý
2. Tạo các món ăn sử dụng nguyên liệu có sẵn
3. Sử dụng tính năng "Thiết kế thực đơn" để tạo thực đơn
4. Xem và quản lý các thực đơn đã tạo trong phần "Danh sách thực đơn"
