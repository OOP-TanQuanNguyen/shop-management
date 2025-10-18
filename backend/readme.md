# ShopManagement \\
## Cấu trúc thư mục

```bash
ShopManagement/
│
├── pom.xml                 # File cấu hình Maven
├── .env                    # Cấu hình DB & ENV (không commit)
├── .gitignore              # Bỏ qua target/, .env, IDE files
│
└── src/
    └── main/
        ├── java/
        │   └── edu/ptithcm/shop/
        │       ├── main/
        │       │   └── App.java                # Điểm khởi động chương trình
        │       │
        │       ├── model/                     # Các lớp dữ liệu (Entity)
        │       │   ├── Product.java
        │       │   └── User.java
        │       │
        │       ├── view/                      # Giao diện Swing
        │       │   └── MainFrame.java
        │       │
        │       ├── controller/                # Logic điều khiển View ↔ Model
        │       │   └── ProductController.java
        │       │
        │       ├── network/                   # Socket Server & Client
        │       │   ├── SocketServer.java
        │       │   └── SocketClient.java
        │       │
        │       ├── config/                    # Đọc .env (Dotenv)
        │       │   └── DatabaseConfig.java
        │       │
        │       └── util/                      # Tiện ích chung (Singleton DB)
        │           └── DBConnection.java
        │
        └── resources/
            ├── icons/
            └── config/
