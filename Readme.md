Xử lý file trên console
- Chạy chương trình: 
  - Bước 1: Tìm folder target, mở teminal di chuyển con trỏ vào thư mục folder target(ví dụ: cd D:\Project\mock-up-1\target)
  - Bước 2 : chạy lệnh java -jar <tên file jar> <tên chức năng> <tên thư mục> (Ví dụ: java -jar target/mock-up-1-1.0-SNAPSHOT.jar 1 Data)
  * Với Data là nơi chứa các thư mục InputFolder,OutputFolder cho việc đọc ghi
  - Bước 3: Sau khi chương trình hoàn thành, vào InputFolder để đọc kết quả
  - Bước 4: Nếu thức mục chức tài liệu của bạn là Data , vào Data/ErrorFolder/error.output.txt để xem lỗi. Nếu không hệ thống sẽ tự tạo Folder Data cùng cấp với dự án.
