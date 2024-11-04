Xử lý file trên console
- Chạy chương trình: 
  - Bước 1: Tìm folder target, mở terminal di chuyển con trỏ vào thư mục folder target(ví dụ: cd D:\Project\mock-up-1\target)
  - Bước 2 : chạy lệnh java -jar <tên file jar> <tên chức năng> <tên thư mục> (Ví dụ: java -jar target/mock-up-1-1.0-SNAPSHOT.jar 1 Data)
  * Với Data là nơi chứa các thư mục InputFolder,OutputFolder cho việc đọc ghi
  - Bước 3: Sau khi chương trình hoàn thành, vào InputFolder để đọc kết quả
  - Bước 4: Nếu thư mục chứa tài liệu của bạn là Data, vào Data/ErrorFolder/error.output.txt để xem lỗi. Nếu không hệ thống sẽ tự tạo Folder Data cùng cấp với dự án.


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

-Vui lòng format dữ liệu theo mẫu này, mọi thắc mắc xin gửi về hòm thư: 