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

## Phân Tích Mẫu Thiết Kế (Design Patterns)

Dự án HR Management áp dụng nhiều mẫu thiết kế phổ biến để đảm bảo tính mô-đun, khả năng mở rộng và bảo trì. Dưới đây là phân tích các mẫu thiết kế chính được sử dụng:

### 1. Repository Pattern
Repository Pattern tách biệt logic truy cập dữ liệu khỏi logic kinh doanh, giúp code dễ đọc, dễ kiểm thử và bảo trì.

**Ví dụ trong dự án:**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.status = org.ptithcm2021.hr_management.enums.UserStatusEnum.PENDING")
    List<User> findAllActiveUsers();
    
    Page<User> findAllByStatus(UserStatusEnum status, Pageable pageable);
    
    // Các phương thức truy vấn khác
}
```

### 2. Service Pattern
Service Pattern đóng gói logic nghiệp vụ của ứng dụng, tạo lớp trung gian giữa controller và repository.

**Ví dụ trong dự án:**
```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    
    @Override
    public UserResponse createUser(UserRequest userRequest) throws MessagingException {
        User user = userMapper.toUser(userRequest);
        // Logic tạo người dùng
        return userMapper.toUserResponse(userRepository.save(user));
    }
    
    // Các phương thức dịch vụ khác
}
```

### 3. Dependency Injection
Spring Boot sử dụng DI để cung cấp các dependency cho các lớp, làm giảm sự phụ thuộc và tăng tính module hóa.

**Ví dụ trong dự án:**
```java
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final UserService userService;
    private final PositionRepository positionRepository;
    private final ContractTypeRepository contractTypeRepository;
    // Các dependency khác được inject tự động thông qua constructor
}
```

### 4. Builder Pattern
Sử dụng để xây dựng các đối tượng phức tạp thông qua Lombok.

**Ví dụ trong dự án:**
```java
@Entity
@Table(name = "contracts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private LocalDate startDate;
    private LocalDate endDate;
    private double basicSalary;
    
    @Builder.Default
    private ContractStatusEnum contractStatusEnum = ContractStatusEnum.PENDING;
    
    // Các thuộc tính khác
}

// Sử dụng:
Contract contract = Contract.builder()
                     .startDate(startDate)
                     .endDate(endDate)
                     .basicSalary(salary)
                     .build();
```

### 5. Strategy Pattern
Cho phép chọn thuật toán hoặc xử lý tại thời điểm chạy.

**Ví dụ trong dự án:**
```java
@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {
    // Logic xử lý quyết định dựa trên loại quyết định
    @Override
    public DecisionResponse createDecision(DecisionRequest decisionRequest) {
        // Logic chung
        Decision decision = decisionMapper.toDecision(decisionRequest);
        
        // Chiến lược xử lý khác nhau dựa trên loại quyết định
        if (decisionRequest.getPositionId() != null) {
            // Chiến lược xử lý quyết định vị trí
            Position position = positionRepository.findById(decisionRequest.getPositionId())
                    .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));
            decision.setPosition(position);
            
        } else if (decision.getSeniorityAllowanceRule() != null) {
            // Chiến lược xử lý quyết định phụ cấp thâm niên
            SeniorityAllowanceRule rule = seniorityAllowanceRuleRepository.findById(decisionRequest.getSeniorityAllowanceRuleId())
                    .orElseThrow(() -> new AppException(ErrorCode.SENIORITY_ALLOWANCE_RULE_NOT_FOUND));
            decision.setSeniorityAllowanceRule(rule);
            
        } else if (decision.getSalaryPromotion() != null) {
            // Chiến lược xử lý quyết định tăng lương
            SalaryPromotion salaryPromotion = salaryPromotionRepository.findById(decisionRequest.getSalaryPromotionId())
                    .orElseThrow(() -> new AppException(ErrorCode.SALARY_PROMOTION_NOT_FOUND));
            decision.setSalaryPromotion(salaryPromotion);
        }
        
        return decisionMapper.toDecisionResponse(decisionRepository.save(decision));
    }
}
```

### 6. Adapter Pattern
MapStruct được sử dụng để chuyển đổi giữa các đối tượng DTO và Entity.

**Ví dụ trong dự án:**
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
    
    // Các phương thức ánh xạ khác
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
```

### 7. Singleton Pattern 
Spring tự động quản lý các bean singleton thông qua IoC container.

**Ví dụ trong dự án:**
```java
@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
                "redis-18620.c1.ap-southeast-1-1.ec2.redns.redis-cloud.com", 18620);
        configuration.setUsername("default");
        configuration.setPassword("iJA8gzweshOAnISRVc4GmX5CoXClxoRZ");
        return new LettuceConnectionFactory(configuration);
    }
    
    // Các bean khác được quản lý như singleton
}
```

### 8. Factory Method Pattern
Sử dụng để tạo đối tượng từ các interface mà không cần biết chi tiết thực hiện.

**Ví dụ trong dự án:**
```java
@Configuration
public class CloudStorageConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Bean
    public Drive driveService() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        InputStream in = new ClassPathResource("credentials.json").getInputStream();
        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Spring Boot Google Drive Upload")
                .build();
    }
}
```

### 9. Facade Pattern
Cung cấp giao diện đơn giản cho một hệ thống con phức tạp.

**Ví dụ trong dự án:**
```java
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;
    private final Drive driveService;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // Logic phức tạp được che giấu sau phương thức đơn giản
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "HR_management"));
        return uploadResult.get("secure_url").toString();
    }

    @Override
    public String uploadFileFromByteArrayOutputStream(ByteArrayOutputStream output, String fileName) throws Exception {
        // Che giấu logic phức tạp của việc tải lên Google Drive
        java.io.File tempFile = createTempFile(output, fileName);
        FileContent mediaContent = new FileContent("application/pdf", tempFile);
        
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(fileName);
        
        com.google.api.services.drive.model.File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
                
        makeFilePublic(driveService, uploadedFile.getId());
        
        String fileUrl = "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
        tempFile.delete();
        
        return fileUrl;
    }
}
```

### 10. Observer Pattern
Được triển khai thông qua hệ thống thông báo và WebSocket.

**Ví dụ trong dự án:**
```java
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRecipientRepository recipientRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {
        User sender = null;
        if (notificationRequest.getUserId() != null) {
            sender = userService.getUserToUser(notificationRequest.getUserId());
        }
        Notification notification = notificationMapper.toNotification(notificationRequest);

        List<NotificationRecipient> notificationRecipients = new ArrayList<>();
        List<User> receivers = new ArrayList<>();

        // Xử lý các đối tượng quan sát khác nhau (users, departments, positions)
        if (notificationRequest.getReceiverIds() != null) {
            notificationRequest.getReceiverIds().forEach(aLong -> {
                User user = userService.getUserToUser(aLong);
                receivers.add(user);
            });
            notification.setNotificationEnum(NotificationEnum.SINGLE);
        }
        
        if (notificationRequest.getDepartmentIds() != null) {
            notificationRequest.getDepartmentIds().forEach(s -> {
                List<User> users = userRepository.findAllByDepartmentId(s, UserStatusEnum.ACTIVE);
                receivers.addAll(users);
            });
            notification.setNotificationEnum(NotificationEnum.DEPARTMENT);
        }
        
        // Gửi thông báo cho tất cả người nhận đã đăng ký (observers)
        for (User receiver: receivers.stream().distinct().toList()) {
            NotificationRecipient recipient = NotificationRecipient.builder()
                    .notification(notification)
                    .user(receiver)
                    .build();
            notificationRecipients.add(recipient);
        }

        notification.setSender(sender);
        Notification noti = notificationRepository.save(notification);
        recipientRepository.saveAll(notificationRecipients);
        
        return notificationMapper.toNotificationResponse(noti);
    }
}
```

## Các Biểu Đồ UML Chi Tiết

### 1. Biểu Đồ Lớp Chi Tiết (Class Diagram)

```
+------------------+     +---------------+     +-----------------+
|      User        |<--->| Department    |<--->|    Position     |
+------------------+     +---------------+     +-----------------+
| - id: long       |     | - id: String  |     | - id: String    |
| - email: String  |     | - name: String|     | - name: String  |
| - fullName: String     | - description |     | - description   |
| - numberCCCD     |     +---------------+     | - department    |
| - phoneNumber    |           ^               | - role          |
| - dob: LocalDate |           |               +-----------------+
| - nationality    |     +-----+------+              ^
| - gender         |     |   Account   |              |
| - address        |     +------------+               |
| - status: enum   |<--->| - username  |<------------>+
| - avatar         |     | - password  |         +----------------+
| - account        |     | - status    |-------->|     Role       |
| - position       |     | - role      |         +----------------+
| - hireDate       |     +------------+          | - id: RoleEnum |
| - serviceDuration|           |                 | - description  |
| - salaryBasic    |           |                 +----------------+
+------------------+      +----+-------+
       |  ^                |  Contract  |
       |  |                +------------+                +-----------------+
       |  +--------------->| - id: int  |<-------------->| ContractType    |
       |                   | - startDate|                +-----------------+
       |                   | - endDate  |                | - id: String    |
       |                   | - basicSalary               | - name: String  |
       |                   | - clause   |                | - duration      |
       |                   | - status   |                | - isPolicy      |
       |                   | - user     |                +-----------------+
       |                   | - signer   |
       v                   | - position |                +-----------------+
+-----------------+        | - jobGrade |<-------------->| JobGrade        |
| LeaveApplication|        +------------+                +-----------------+
+-----------------+              |                       | - id: String    |
| - id: long      |<-------------+                       | - name: String  |
| - startDate     |              |                       | - coefficient   |
| - endDate       |       +------+------+                +-----------------+
| - reason        |       | Decision    |
| - status: enum  |       +-------------+                +-----------------+
| - signer: User  |<----->| - id: String|<-------------->| LeaveBalance    |
| - user: User    |       | - attachment|                +-----------------+
| - leaveType     |       | - content   |                | - id: int       |
+-----------------+       | - value     |                | - year: int     |
      ^                   | - date      |                | - month: int    |
      |                   | - status    |                | - totalLeaveDay |
+-----+---------+         | - type: enum|                | - carriedOverDay|
| LeaveType      |        | - user      |                | - usedLeaveDay  |
+---------------+         | - position  |                | - remainingLeave|
| - id: int      |        +-------------+                | - user          |
| - name: String |              |                        +-----------------+
| - description  |              v
| - affectBalance|       +-----+--------+                +-----------------+
+---------------+        |WorkingHistory |<-------------->| Salary          |
                         +--------------+                 +-----------------+
                         | - id: long   |                 | - id: int       |
                         | - type: enum |                 | - startDate     |
                         | - user       |                 | - endDate       |
                         | - decision   |                 | - baseSalary    |
                         | - contract   |                 | - allowance     |
                         +--------------+                 | - workingDays   |
                                                         | - leaveDays     |
                                                         | - paymentDate   |
                                                         | - user          |
                                                         | - contract      |
                                                         +-----------------+
```

### 2. Biểu Đồ Use Case (Use Case Diagram)

```
                                   +-------------------+
                                   |   HR Management   |
                                   +-------------------+
                                           ^
                                          / \
                           +-------------+   +-------------+
                           |                               |
                    +------+------+                 +------+------+
                    |     HR      |                 |    Staff    |
                    +-------------+                 +-------------+
                    | - Login     |                 | - Login     |
                    | - Logout    |                 | - Logout    |
                    +------+------+                 +------+------+
                           |                               |
         +-----------------+-----------------+    +--------+--------+
         |                 |                 |    |                 |
+--------+-----+  +--------+-----+  +--------+----+  +--------+----+  +--------+----+
| Quản lý NV   |  | Quản lý HĐ   |  | Quản lý QĐ   |  | Xin nghỉ   |  | Xem thông tin|
+-------------+  +-------------+  +-------------+  +-------------+  +-------------+
| - Thêm NV    |  | - Tạo HĐ    |  | - Tạo QĐ    |  | - Tạo đơn  |  | - Xem hồ sơ |
| - Sửa NV     |  | - Ký HĐ     |  | - Phê duyệt |  | - Hủy đơn  |  | - Xem lương |
| - Xóa NV     |  | - Gia hạn HĐ|  | - Khen thưởng|  | - Xem lịch |  | - Xem QĐ    |
| - Tìm NV     |  | - Chấm dứt  |  | - Kỷ luật   |  | - Xem phép |  | - Xem TB    |
+-------------+  +-------------+  +-------------+  +-------------+  +-------------+
```

### 3. Biểu Đồ Trạng Thái (State Diagram) - Quy Trình Hợp Đồng

```
    +-------------+        +------------------+        +--------------------+
    |   PENDING   +------->| SIGNED_PENDING   +------->|      ACTIVE        |
    | (Chờ ký kết)|        | (Đã ký, chờ hiệu |        | (Hợp đồng có hiệu  |
    +-------------+        |    lực)          |        |       lực)         |
          ^                +------------------+        +--------------------+
          |                                                      |
          |                                                      |
          |                                                      v
    +-----+------+        +------------------+        +--------------------+
    | TERMINATED |<-------| RENEWED          |<-------| EXPIRED            |
    | (Chấm dứt) |        | (Đã gia hạn)     |        | (Hết hạn)          |
    +------------+        +------------------+        +--------------------+
```

### 4. Biểu Đồ Tuần Tự (Sequence Diagram) - Quy Trình Xét Duyệt Nghỉ Phép

```
+--------+     +-------------------+     +------------------+     +-------------+     +---------------+
| User   |     | LeaveApplication  |     | LeaveBalance     |     | Notification |     | Email Service |
+--------+     +-------------------+     +------------------+     +-------------+     +---------------+
    |                   |                        |                       |                   |
    | 1. Tạo đơn nghỉ   |                        |                       |                   |
    |------------------>|                        |                       |                   |
    |                   | 2. Kiểm tra số dư      |                       |                   |
    |                   |----------------------->|                       |                   |
    |                   |                        |                       |                   |
    |                   | 3. Xác nhận số dư      |                       |                   |
    |                   |<-----------------------|                       |                   |
    |                   |                        |                       |                   |
    |                   | 4. Lưu đơn             |                       |                   |
    |                   |-----------------------+|                       |                   |
    |                   |                      | |                       |                   |
    |                   |<---------------------+ |                       |                   |
    |                   | 5. Gửi thông báo       |                       |                   |
    |                   |---------------------------------------------->|                   |
    |                   |                        |                       | 6. Gửi email      |
    |                   |                        |                       |------------------>|
    |                   |                        |                       |                   |
    | 7. Kết quả        |                        |                       |                   |
    |<------------------|                        |                       |                   |
    |                   |                        |                       |                   |
```

### 5. Biểu Đồ Hoạt Động (Activity Diagram) - Quy Trình Tuyển Dụng và Quản Lý Nhân Sự

```
    +------------------+
    | Bắt đầu tuyển NV |
    +------------------+
             |
             v
    +------------------+
    | Tạo vị trí tuyển |
    +------------------+
             |
             v
    +------------------+
    |  Phỏng vấn ứng   |
    |     viên         |
    +------------------+
             |
             v
    +------------------+      Không     +------------------+
    |  Ứng viên đạt    +-------------->|  Kết thúc quy     |
    |   yêu cầu?       |               |     trình         |
    +------------------+               +------------------+
             |
             | Có
             v
    +------------------+
    | Tạo hồ sơ nhân   |
    |     viên         |
    +------------------+
             |
             v
    +------------------+
    |   Tạo tài khoản  |
    |   hệ thống       |
    +------------------+
             |
             v
    +------------------+
    |    Soạn thảo     |
    |    hợp đồng      |
    +------------------+
             |
             v
    +------------------+
    |   Ký kết hợp     |
    |     đồng         |
    +------------------+
             |
             v
    +------------------+
    | Cập nhật trạng   |
    | thái nhân viên   |
    +------------------+
             |
             v
    +------------------+
    |  Phân quyền và   |
    |  phân công công  |
    |      việc        |
    +------------------+
             |
             v
    +------------------+
    |  Theo dõi hiệu   |
    |  suất làm việc   |
    +------------------+
```

### 6. Biểu Đồ Giao Tiếp (Communication Diagram) - Quy Trình Tăng Lương

```
                 +--------------------+
                 |  SalaryController  |
                 +--------------------+
                 /          |          \
                /           |           \
               /            |            \
              v             v             v
+----------------+   +------------------+   +------------------+
| UserService    |   | SalaryService    |   | NotifyService    |
+----------------+   +------------------+   +------------------+
      |                      |                       |
      v                      v                       v
+----------------+   +------------------+   +------------------+
| UserRepository |   | SalaryRepository |   | EmailService     |
+----------------+   +------------------+   +------------------+
```

### 7. Biểu Đồ Thành Phần (Component Diagram) - Kiến Trúc Hệ Thống

```
+--------------------+        +-------------------+        +-------------------+
|   Web Client       |<------>|  REST Controllers  |<------>| Service Layer     |
|(React/Angular/Vue) |        |     Layer          |        | (Business Logic)  |
+--------------------+        +-------------------+        +-------------------+
                                       ^                           ^
                                       |                           |
                                       v                           v
+--------------------+        +-------------------+        +-------------------+
|    Security        |<------>|   Repository      |<------>|   Database        |
|(JWT Authentication)|        |     Layer         |        |   (MySQL)         |
+--------------------+        +-------------------+        +-------------------+
         ^                             ^                           ^
         |                             |                           |
         v                             v                           v
+--------------------+        +-------------------+        +-------------------+
|    Redis Cache     |<------>|   Scheduler       |<------>|  External APIs    |
|(Performance Boost) |        |(Quartz Jobs)      |        |(Email, Storage)   |
+--------------------+        +-------------------+        +-------------------+
```

### 8. Biểu Đồ Triển Khai (Deployment Diagram)

```
+------------------+        +-------------------+        +------------------+
|  Client Browser  |        |  Application      |        |    Database      |
|                  |        |    Server         |        |     Server       |
+------------------+        +-------------------+        +------------------+
| - Web Interface  |<------>| - Spring Boot App |<------>| - MySQL Database |
| - SPA            |        | - Tomcat          |        | - Redis Server   |
+------------------+        | - JVM             |        +------------------+
                            +-------------------+
                                    ^
                                    |
                                    v
                            +-------------------+
                            | External Services |
                            +-------------------+
                            | - Email Server    |
                            | - Cloud Storage   |
                            | - Authentication  |
                            +-------------------+
```

## Tác Giả
© 2025 PTITHCM 2021 - HR Management System
