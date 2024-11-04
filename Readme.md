Xử lý file trên console
- Chạy chương trình: 
  - Bước 1: Tìm folder target, mở terminal di chuyển con trỏ vào thư mục folder target(ví dụ: cd D:\Project\mock-up-1\target).
  - Bước 2: Chạy lệnh mvn clean package để build file jar.
  - Bước 3 : chạy lệnh java -jar <tên file jar> <tên chức năng> <tên thư mục> (Ví dụ: java -jar target/mock-up-1-1.0-SNAPSHOT.jar 1 Data).
  * Với Data là nơi chứa các thư mục InputFolder,OutputFolder cho việc đọc ghi.
  - Bước 4: Sau khi chương trình hoàn thành, vào InputFolder để đọc kết quả.
  - Bước 5: Nếu thư mục chứa tài liệu của bạn là Data, vào Data/ErrorFolder/error.output.txt để xem lỗi. Nếu không hệ thống sẽ tự tạo Folder Data cùng cấp với dự án.


Input data format:
- product.origin.csv:
  Id,Name,Price,StockAvailable
  P0001,Nồi cơm điện,96.92,83
- customer.origin.csv
  id,name,email,phonenumber
  CUS0001,Nguyễn Đức Hoàng,9gmufwa3@gmail.com,03836429663
- order.origin.csv
  Id,CustomerID,ProductQuantities,OrderDate
  ORD0000001,CUS0374,P0945:47;P0145:27;P0159:18,2024-05-13T10:17:08.055107+07:07

Cấu trúc dự án:
```
src
├── main
│   ├── java
│   │   └── org.example
│   │       ├── DTO               # Các đối tượng truyền dữ liệu
│   │       ├── enums             # Các enum dùng chung
│   │       ├── factory           # Các factory cho việc xử lý file và xác thực
│   │       ├── model             # Các model đại diện cho thực thể dữ liệu (Sản phẩm, Khách hàng, Đơn hàng)
│   │       ├── repository        # Repository để lưu trữ dữ liệu
│   │       ├── service           # Các dịch vụ xử lý nghiệp vụ
│   │       ├── utils             # Các tiện ích dùng chung
│   │       └── Main              # Lớp chính để chạy ứng dụng
│   └── resources                 # Thư mục chứa các file tài nguyên (như file dữ liệu đầu vào, cấu hình)
└── test                          # Thư mục chứa các lớp kiểm thử
```
Danh sách chức năng:
- 1: Load data.
- 2.1: Thêm mới product.
- 2.2: Chỉnh sửa product.
- 2.3: Xóa product.
- 3.1: Xóa customer.
- 3.2: Thêm mới customer.
- 3.3: Chỉnh sửa customer.
- 4.1: Thêm mới order.
- 4.2: Chỉnh sửa order.
- 4.3: Xóa order.
- 5.1: Tìm kiếm top 3 product có số lượng order lớn nhất.
- 5.2: Tìm kiếm order theo Product ID.
-Vui lòng format dữ liệu theo mẫu này, mọi thắc mắc xin gửi về hòm thư: 