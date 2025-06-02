# HR Management System

## Overview
HR Management System is a comprehensive solution for managing human resources in an organization. This system automates and streamlines various HR processes including employee management, contracts, leave applications, salary management, performance feedback, and notifications.

## Getting Started
Pull repo về và start là đủ.

### Tài khoản test
- **Username**: admin
- **Password**: admin

## Phân Tích Công Nghệ

### Back-end
- **Framework**: Spring Boot 3.4.3
- **Programming Language**: Java 23
- **Build Tool**: Maven
- **Database**: MySQL
- **Security**: Spring Security, OAuth2, JWT Authentication
- **Cache**: Redis
- **Documentation**: SpringDoc OpenAPI (Swagger)

### Tích Hợp
- **Email Service**: Spring Mail (Gmail SMTP)
- **File Storage**: Cloudinary, Google Drive API
- **Document Processing**: Docx4j
- **Task Scheduling**: Quartz Scheduler
- **Real-time Communication**: WebSockets

### Kiến Trúc
- **Architecture Pattern**: Model-View-Controller (MVC)
- **Data Access Layer**: Spring Data JPA
- **Object Mapping**: MapStruct
- **API Style**: RESTful API
- **Code Generation**: Lombok
- **Validation**: Spring Validation

## Chức Năng Chính

### 1. Quản Lý Nhân Sự
- Hồ sơ nhân viên
- Phân quyền người dùng (Role-based Access Control)
- Quản lý phòng ban và vị trí

### 2. Quản Lý Hợp Đồng
- Tạo và theo dõi hợp đồng
- Quản lý các loại hợp đồng
- Lịch sử làm việc

### 3. Quản Lý Nghỉ Phép
- Đơn xin nghỉ phép
- Số dư nghỉ phép
- Các loại nghỉ phép
- Ngày nghỉ

### 4. Quản Lý Lương và Phúc Lợi
- Tính lương
- Tăng lương
- Phụ cấp thâm niên
- Bậc lương nghề (Job Grade)

### 5. Quản Lý Khen Thưởng & Kỷ Luật
- Quyết định thưởng
- Phân bổ thưởng
- Quyết định kỷ luật
- Phân công kỷ luật

### 6. Thông Báo và Phản Hồi
- Hệ thống thông báo
- Phản hồi nhân viên
- Email tự động

### 7. Báo Cáo và Tài Liệu
- Xuất báo cáo
- Quản lý tài liệu
- Tạo quyết định

## Điểm Nổi Bật Của Dự Án

1. **Kiến trúc module hóa**: Dự án được chia thành các module rõ ràng như controller, service, repository, model giúp dễ dàng bảo trì và mở rộng.

2. **Bảo mật đa lớp**: Sử dụng Spring Security kết hợp JWT để xác thực và phân quyền.

3. **Lưu trữ đám mây**: Tích hợp Cloudinary và Google Drive API để lưu trữ tài liệu.

4. **Hỗ trợ đa ngôn ngữ**: Chuẩn bị cho quốc tế hóa.

5. **Hiệu suất cao**: Sử dụng Redis cache để tăng tốc độ xử lý.

6. **Giao tiếp thời gian thực**: WebSockets cho thông báo và cập nhật tức thì.

7. **Xử lý tài liệu linh hoạt**: Sử dụng Docx4j để tạo và thao tác với tài liệu văn phòng.

8. **API được tài liệu hóa tốt**: OpenAPI/Swagger cho tài liệu API.

9. **Tự động hóa quy trình**: Sử dụng Quartz Scheduler cho các tác vụ định kỳ.

10. **Thiết kế có thể mở rộng**: Sẵn sàng cho việc mở rộng quy mô doanh nghiệp.
