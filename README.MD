# WebApp Kinh Doanh Laptop

## Giới thiệu
Dự án **WebApp Kinh Doanh Laptop** là một hệ thống thương mại điện tử giúp người dùng dễ dàng mua sắm laptop trực tuyến. Ứng dụng được xây dựng bằng **Spring Boot** và sử dụng **MySQL** làm cơ sở dữ liệu, hỗ trợ đầy đủ các chức năng quản lý sản phẩm, giỏ hàng, đơn hàng và thanh toán.

## Công nghệ sử dụng
- **Ngôn ngữ lập trình**: Java
- **Framework**: Spring Boot 3.2.2
- **Mô hình kiến trúc**: MVC (Model-View-Controller)
- **Cơ sở dữ liệu**: MySQL
- **Giao diện**: JSP (Sử dụng template từ [ThemeWagon](https://themewagon.com/))
- **Bảo mật**: Spring Security (Authorization, Session, Remember Me)
- **ORM**: Hibernate với JPA
- **Cấu trúc dự án**: Maven
- **Kiểm tra dữ liệu đầu vào**: Validate form input

## Chức năng
### Dành cho Quản Trị Viên (Admin)
- **Quản lý người dùng**
   + Thêm người dùng: Admin có thể tạo tài khoản mới cho người dùng với thông tin như họ tên, email, số điện thoại, vai trò (Admin, User), username, password.
   + Cập nhật người dùng: Cho phép chỉnh sửa thông tin cá nhân của người dùng, thay đổi vai trò nếu cần.
   + Xóa người dùng: Admin có thể xóa tài khoản người dùng không còn hoạt động.
   + Quy trình:
      Admin vào trang Quản lý người dùng.
      Chọn thao tác Thêm/Cập nhật/Xóa tùy vào nhu cầu.
      Nếu thêm/cập nhật, nhập thông tin cần thiết và lưu lại.
      Nếu xóa, hệ thống yêu cầu xác nhận trước khi thực hiện

- **Quản lý sản phẩm**
   + Thêm sản phẩm: Admin nhập thông tin sản phẩm mới gồm tên, mô tả, giá, số lượng,...
   + Cập nhật sản phẩm: Cho phép chỉnh sửa thông tin sản phẩm, thay đổi giá, số lượng hoặc mô tả.
   + Xóa sản phẩm: Loại bỏ sản phẩm không còn kinh doanh.
   + Tải lên hình ảnh sản phẩm: Admin có thể chọn và tải lên nhiều hình ảnh cho sản phẩm
   + Quy trình
      Admin vào trang Quản lý sản phẩm.
      Chọn thao tác Thêm/Cập nhật/Xóa sản phẩm.
      Nếu thêm/cập nhật, nhập đầy đủ thông tin sản phẩm và tải lên hình ảnh nếu cần.
      Nếu xóa, hệ thống sẽ yêu cầu xác nhận trước khi thực hiện.

- **Quản lý đơn hàng** 
   + Xem danh sách đơn hàng: Hiển thị tất cả đơn hàng với trạng thái (peding, shipping, complete, cancel).
   + Thay đổi trạng thái đơn hàng: Admin có thể cập nhật trạng thái đơn hàng.
   + Quy trình
      Admin vào trang Quản lý đơn hàng.
      Chọn một đơn hàng để xem chi tiết.
      Thực hiện cập nhật trạng thái nếu cần

### Dành cho Người Dùng
- **Xem danh sách sản phẩm**
   + Người dùng có thể xem danh sách tất cả sản phẩm hiện có trong hệ thống.
   + Hiển thị các thông tin sản phẩm như: tên, giá, hình ảnh, mô tả ngắn, số lượng tồn kho.
   + Quy trình
      Người dùng vào trang Sản phẩm.
      Hệ thống hiển thị danh sách sản phẩm có sẵn.
      Người dùng có thể chọn xem chi tiết từng sản phẩm.

- **Lọc sản phẩm theo khoảng giá, hãng sản xuất,...**
   + Cho phép người dùng lọc sản phẩm theo:
      Hãng sản xuất (ví dụ: Dell, Asus, v.v.).
      Mức giá (ví dụ: dưới 1 triệu, 1-5 triệu, trên 5 triệu).
      Mục đích sử dụng (ví dụ: Gaming, Doanh nhân,...).
   + Quy trình
      Người dùng truy cập trang Sản phẩm.
      Sử dụng bộ lọc để tìm sản phẩm theo nhu cầu.
      Hệ thống cập nhật danh sách sản phẩm theo tiêu chí đã chọn.

- **Thêm sản phẩm vào giỏ hàng**
   + Người dùng có thể chọn sản phẩm và thêm vào giỏ hàng.
   + Quy trình
      Người dùng chọn sản phẩm cần mua.
      Nhấn nút "Thêm vào giỏ hàng".

- **Tăng/giảm số lượng sản phẩm trong giỏ**

- **Đặt hàng và thanh toán**
   + Người dùng vào Giỏ hàng và chọn Thanh toán.
   + Nhập địa chỉ giao hàng, tên, số điện thoại.
   + Xác nhận thanh toán
- **Xem lịch sử mua hàng (Chọn vào phần Avatar góc phải)**


## Hướng dẫn cài đặt
### 1️⃣ Yêu cầu hệ thống
- **Java**: 17
- **Spring Boot**: 3.2.2
- **Cài đặt đầy đủ dependencies trong `pom.xml`**

### 2️⃣ Cài đặt và chạy dự án
#### Clone repository
```bash
git clone https://github.com/NgnTienDat/springmvc-hoidanit-course.git
cd springmvc-hoidanit-course
```
#### Cấu hình cơ sở dữ liệu
- Tạo một database MySQL mới
- Cập nhật thông tin kết nối trong `application.properties`
```properties

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/laptopshop
spring.datasource.username=<your MySQL username account>
spring.datasource.password=<your password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql: true


spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

logging.level.org.springframework.security=DEBUG

spring.session.store-type=jdbc
spring.session.timeout=30m

spring.session.jdbc.initialize-schema=always
```
#### Chạy dự án
Bạn có thể chạy dự án trên **IntelliJ IDEA** hoặc **Visual Studio Code**.

** Khuyến khích sử dụng Visual Studio Code với các bước sau:**
1. Mở thư mục dự án trong **Visual Studio Code**.
2. Cài đặt **Extension Pack for Java** (nếu chưa có).
3. Mở terminal và chạy lệnh:
   ```bash
   mvn spring-boot:run
   ```
4. Hoặc chạy trực tiếp bằng file `LaptopshopApplication.java` trong thư mục:
   ```
   src/main/java/vn/hoidanit/laptopshop/
   ```

### 3️⃣ Truy cập ứng dụng
- **Trang Admin**: [http://localhost:8080/admin](http://localhost:8080/admin)
- **Trang Người Dùng**: [http://localhost:8080](http://localhost:8080)

### 4️⃣ Cơ sở dữ liệu mẫu
Dự án đi kèm với một file **SQL** chứa dữ liệu mẫu để bạn dễ dàng thiết lập cơ sở dữ liệu.

#### File cơ sở dữ liệu
- **Vị trí**: https://drive.google.com/file/d/16QCC-Hb55sxiecNv7I8i6TjCscviYPmN/view?usp=sharing
- **Cách import vào MySQL**:
  1️⃣ Mở **MySQL Workbench**.
  2️⃣ Tạo một database mới với lệnh:
     ```sql
     CREATE DATABASE laptopshop;
     ```
  3️⃣ Trong MySQL Workbench, vào **Server** → **Data Import** -> Chọn Import from self-contained file → Chọn file `laptopshop.sql` -> Tại mục Default Target Schema, chọn laptopshop và nhấn **Start Import**.


Đặc tả database:

1. Bảng roles (Vai trò)
- Mục đích: Quản lý vai trò người dùng trong hệ thống (Admin, Khách hàng, Nhân viên, v.v.).
- Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã vai trò.
   + name (VARCHAR(255)): Tên vai trò.
   + description (VARCHAR(255)): Mô tả vai trò.
2. Bảng users (Người dùng)
- Mục đích: Lưu trữ thông tin người dùng trong hệ thống.
- Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã người dùng.
   + address (VARCHAR(255)): Địa chỉ của người dùng.
   + avatar (VARCHAR(255)): Đường dẫn ảnh đại diện.
   + email (VARCHAR(255)): Email đăng nhập.
   + full_name (VARCHAR(255)): Họ và tên.
   + password (VARCHAR(255)): Mật khẩu (được mã hóa).
   + phone (VARCHAR(255)): Số điện thoại.
   + role_id (BIGINT, FOREIGN KEY → roles.id): Vai trò của người dùng.
3. Bảng products (Sản phẩm)
- Mục đích: Lưu trữ thông tin về các sản phẩm.
- Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã sản phẩm.
   + detail_desc (MEDIUMTEXT): Mô tả chi tiết.
   + factory (VARCHAR(255)): Nhà sản xuất.
   + image (VARCHAR(255)): Đường dẫn ảnh sản phẩm.
   + name (VARCHAR(255)): Tên sản phẩm.
   + price (DOUBLE): Giá sản phẩm.
   + quantity (BIGINT): Số lượng tồn kho.
   + short_desc (VARCHAR(255)): Mô tả ngắn.
   + sold (BIGINT): Số lượng đã bán.
   + target (VARCHAR(255)): Mục đích sử dụng
4. Bảng carts (Giỏ hàng)
- Mục đích: Lưu trữ thông tin giỏ hàng của người dùng.
- Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã giỏ hàng.
   + sum (INT): Tổng số sản phẩm trong giỏ.
   + user_id (BIGINT, FOREIGN KEY → users.id): Mã người dùng sở hữu giỏ hàng.
5. Bảng cart_detail (Chi tiết giỏ hàng)
- Mục đích: Lưu trữ chi tiết các sản phẩm trong giỏ hàng.
Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã chi tiết giỏ hàng.
   + price (DOUBLE): Giá sản phẩm tại thời điểm thêm vào giỏ.
   + quantity (BIGINT): Số lượng sản phẩm trong giỏ.
   + cart_id (BIGINT, FOREIGN KEY → carts.id): Mã giỏ hàng.
   + product_id (BIGINT, FOREIGN KEY → products.id): Mã sản phẩm.
6. Bảng orders (Đơn hàng)
- Mục đích: Lưu trữ thông tin các đơn hàng của người dùng.
- Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã đơn hàng.
   + total_price (DOUBLE): Tổng giá trị đơn hàng.
   + user_id (BIGINT, FOREIGN KEY → users.id): Mã người dùng đặt hàng.
   + receiver_address (VARCHAR(255)): Địa chỉ nhận hàng.
   + receiver_name (VARCHAR(255)): Tên người nhận.
   + receiver_phone (VARCHAR(255)): Số điện thoại người nhận.
   + status (VARCHAR(255)): Trạng thái đơn hàng 
7. Bảng order_detail (Chi tiết đơn hàng)
- Mục đích: Lưu trữ thông tin chi tiết từng đơn hàng.
- Các cột dữ liệu:
   + id (BIGINT, PRIMARY KEY): Mã chi tiết đơn hàng.
   + price (DOUBLE): Giá sản phẩm tại thời điểm đặt hàng.
   + quantity (BIGINT): Số lượng sản phẩm trong đơn.
   + order_id (BIGINT, FOREIGN KEY → orders.id): Mã đơn hàng.
   + product_id (BIGINT, FOREIGN KEY → products.id): Mã sản phẩm.

Mối quan hệ
- users - roles: Một người dùng có một vai trò (users.role_id → roles.id).
- users - carts: Một người dùng có một giỏ hàng (carts.user_id → users.id).
- carts - cart_detail: Một giỏ hàng chứa nhiều sản phẩm (cart_detail.cart_id → carts.id).
- cart_detail - products: Một sản phẩm có thể thuộc nhiều giỏ hàng (cart_detail.product_id → products.id).
- users - orders: Một người dùng có thể có nhiều đơn hàng (orders.user_id → users.id).
- orders - order_detail: Một đơn hàng có thể chứa nhiều sản phẩm (order_detail.order_id → orders.id).
- order_detail - products: Một sản phẩm có thể thuộc nhiều đơn hàng (order_detail.product_id → products.id).




