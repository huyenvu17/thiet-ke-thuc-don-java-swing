# Hệ Thống Thiết Kế Thực Đơn Cho Trẻ Mầm Non

## Giới Thiệu
Ứng dụng này được thiết kế để giúp tạo và quản lý thực đơn cho trẻ mầm non. Cho phép người dùng định nghĩa nguyên liệu, tạo các món ăn và tạo thực đơn cân bằng dinh dưỡng trong giới hạn ngân sách. Hệ thống đặc biệt tập trung vào việc thiết kế các bữa ăn bổ dưỡng và phù hợp với chi phí cho các cơ sở giáo dục mầm non.

## Tính Năng Chính
- **Đăng nhập/Đăng Ký**: Đăng nhập và đăng ký tài khoản sử dụng
- **Quản Lý Hồ Sơ**: Quản lý thông tin tài khoản
- **Quản Lý Nguyên Liệu**: Thêm, chỉnh sửa và theo dõi nguyên liệu với thông tin giá cả
- **Quản Lý Nhóm Thực Phẩm**: Quản lý thông tin nhóm thực phẩm
- **Quản Lý Món Ăn**: Tạo và phân loại món ăn cho bữa sáng, bữa trưa và bữa xế
- **Quản Lý Công Thức Món Ăn**: Quản lý thông tin công thức món ắn
- **Thiết Kế Thực Đơn**: Tạo thực đơn cho số ngày được chỉ định với giới hạn ngân sách
- **Danh Mục Thực Đơn**: Xem và quản lý các thực đơn đã lưu

## Cấu Trúc Cơ Sở Dữ Liệu
Cấu trúc cơ sở dữ liệu chính của ứng dụng bao gồm các bảng sau:

1. **users** - Quản lý thông tin người dùng hệ thống
   - id (PK): int
   - username: varchar(50)
   - password: varchar(100) 
   - full_name: varchar(100)
   - email: varchar(100)
   - phone: varchar(20)
   - role: varchar(20)

2. **nhom_thuc_pham** - Nhóm các nguyên liệu
   - id (PK): int
   - ten_nhom: varchar(100)
   - mo_ta: text

3. **nguyen_lieu** - Thông tin nguyên liệu nấu ăn
   - id (PK): int  
   - ten_nguyen_lieu: varchar(100)
   - khoi_luong: double
   - don_gia: double
   - nhom_thuc_pham_id (FK): int

4. **mon_an** - Danh sách các món ăn
   - id (PK): int
   - ten_mon: varchar(100)
   - loai_mon: varchar(50)
   - cach_che_bien: text

5. **thuc_don** - Thực đơn ăn uống
   - id (PK): int
   - ten_thuc_don: varchar(100)
   - so_ngay: int

6. **mon_an_thuc_don** - Bảng liên kết món ăn và thực đơn
   - id (PK): int
   - mon_an_id (FK): int
   - thuc_don_id (FK): int
   - so_luong: int

7. **nguyen_lieu_mon_an** - Bảng liên kết nguyên liệu và món ăn
   - id (PK): int
   - nguyen_lieu_id (FK): int
   - mon_an_id (FK): int
   - khoi_luong: double

Các ràng buộc quan trọng:
- Khóa ngoại từ nguyen_lieu.nhom_thuc_pham_id → nhom_thuc_pham.id
- Khóa ngoại từ mon_an_thuc_don.mon_an_id → mon_an.id
- Khóa ngoại từ mon_an_thuc_don.thuc_don_id → thuc_don.id
- Khóa ngoại từ nguyen_lieu_mon_an.nguyen_lieu_id → nguyen_lieu.id
- Khóa ngoại từ nguyen_lieu_mon_an.mon_an_id → mon_an.id

## Công Cụ và Thư Viện
- **Java 17**
- **Maven**
- **MySQL**
- **MySQL Connector/J**
- **Java Swing**

## Kiến Trúc
Ứng dụng áp dụng kiến trúc phân lớp dựa trên mô hình MVC (Model-View-Controller) với các thành phần được tổ chức rõ ràng:

- **View Layer (Giao diện người dùng)**  
  + Các panel Java Swing: `LoginRegisterPanel`, `MainFrame`, `IngredientPanel`, `DishPanel`, `MenuPanel`...
  + Hiển thị dữ liệu và thu thập tương tác người dùng thông qua các component Swing
  + Chỉ giao tiếp với Controller layer, không truy cập trực tiếp đến Model hoặc DAO
  + Nhận kết quả từ Controller và cập nhật UI tương ứng

- **Controller Layer (Điều khiển)**  
  + Triển khai theo mẫu Singleton (vd: `AuthController.getInstance()`)
  + Định nghĩa interface rõ ràng: `IMonAnController`, `IThucDonController`, `INguyenLieuController`...
  + Xử lý các request từ View, chuyển đổi dữ liệu qua lại giữa DTO và Service
  + Điều phối luồng xử lý giữa View và Service layer

- **Service Layer (Xử lý nghiệp vụ)**  
  + Triển khai các lớp như `AuthService`, `MenuService`, `DishService`, `IngredientService`...
  + Chứa toàn bộ business logic: xác thực người dùng, thiết kế thực đơn, tính toán dinh dưỡng
  + Chuyển đổi giữa các đối tượng domain và entity sử dụng Factory Method pattern
  + Gọi DAO để thực hiện các thao tác CRUD với database

- **Model Layer (Dữ liệu)**  
  + **Entity**: Ánh xạ trực tiếp với cấu trúc database (`UserEntity`, `IngredientEntity`, `DishEntity`...)
  + **Domain Objects**: Chứa business state và behavior (`User`, `Ingredient`, `Dish`, `Menu`...)
  + **DTO**: Đóng gói dữ liệu truyền giữa các layer (`NguyenLieuDTO`, `ThucDonDTO`, `MonAnDTO`...)
  + Áp dụng Factory Method pattern để chuyển đổi giữa các loại đối tượng

- **DAO Layer (Truy cập dữ liệu)**  
  + Định nghĩa interface (`IUserDao`, `IIngredientDao`, `IDishDao`, `IMenuDao`...) và các lớp triển khai
  + Cung cấp các phương thức CRUD cho từng entity
  + Sử dụng JDBC để tương tác với MySQL database
  + Áp dụng Singleton pattern để quản lý kết nối database (`DatabaseConnection`)

**Luồng xử lý điển hình**:
1. Người dùng tương tác với giao diện (vd: tạo thực đơn mới)
2. View (`ThietKeThucDonPanel`) thu thập input và gọi Controller (`ThucDonController.taoThucDon(thucDonDTO)`)
3. Controller xác thực dữ liệu và chuyển yêu cầu đến Service (`ThucDonService.taoThucDon(thucDonModel)`)
4. Service áp dụng business rules, xử lý logic, và gọi DAO (`ThucDonDao.insert(thucDonEntity)`)
5. DAO thực hiện các thao tác với database và trả về kết quả
6. Kết quả được truyền ngược lại qua các layer: DAO → Service → Controller → View
7. View cập nhật UI để phản hồi cho người dùng

Kiến trúc phân lớp này mang lại nhiều lợi ích:
- Mỗi layer có trách nhiệm rõ ràng, giới hạn
- Các thành phần có thể phát triển và kiểm thử độc lập
- Thay đổi ở một layer ít ảnh hưởng đến các layer khác
- Logic nghiệp vụ trong Service layer có thể được kiểm thử mà không cần UI
- Công nghệ database có thể thay đổi mà không ảnh hưởng đến business logic


# Design Pattern
**Design Patterns chính trong dự án**

- **Singleton Pattern**  
  - Sử dụng trong các lớp Controller và Service (MonAnController, ThucDonService,...)  
  - Đảm bảo chỉ có một instance duy nhất trong suốt vòng đời ứng dụng  
  - Triển khai qua phương thức static `getInstance()`  
  - Ví dụ: `MonAnController.getInstance()`  

- **Factory Method Pattern**  
  - Sử dụng để chuyển đổi giữa Entity và DTO  
  - Đóng gói logic tạo đối tượng trong các factory method  
  - Giảm sự phụ thuộc trực tiếp vào lớp cụ thể  
  - Ví dụ: `MonAnDTO.fromEntity(MonAnEntity entity)`  


## Quy Trình Cài Đặt

### Yêu Cầu
- Java Development Kit (JDK) 17 trở lên
- MySQL 8.0 trở lên
- Maven 3.8 trở lên

### Cài Đặt Cơ Sở Dữ Liệu
1. Cài đặt MySQL nếu chưa có
3. Thực thi script SQL `thietkethucdon_db.sql` để tạo database và các bảng cần thiết

### Cài Đặt Ứng Dụng
1. Mở thư mục source code
2. Cập nhật thông số kết nối cơ sở dữ liệu trong `src/main/java/dao/DatabaseConnection.java` nếu cần
   - Cài đặt mặc định:
     - Host: localhost (127.0.0.1)
     - Database: thietkethucdonapp
     - User: root
     - Password: 111111
3. Build thư viện Maven:
4. Chạy ứng dụng: MainApp.java