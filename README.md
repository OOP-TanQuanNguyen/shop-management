# Shop Management System

A complete mini-market / POS management platform built with **Java (Swing + DTTP protocol)**, **MySQL**, and **Docker Compose**.  
The system supports full POS operations, inventory control, employee/customer management, and secure communication.

---

## 1. Overview

This system provides:

- **Backend Server**: Java 21, Maven, Hibernate, DTTP encrypted protocol  
- **Frontend (POS Client)**: Java Swing using MVC + Store/Reducer (Redux-like)  
- **MySQL Database** for persistent storage  
- **Docker Compose** for containerized deployment  
- **High-performance invoice workflow with multi thread**

---

## 2. Features

### POS / Invoice Features 
- Add/update/remove items  
- Automatic total/discount computation    
- Standard POS real-time behavior

### Management Modules
- Product / Category / Branch  
- Customer / Loyalty  
- Employee / Role  
- Inventory  

### Technical Features
- AES-256 encrypted DTTP protocol  
- Optional GZIP compression  
- Multi-module Maven  
- Hibernate ORM + HikariCP  
- Service–Repository–DTO architecture  
- Event-driven Swing Store (Redux-like)

---

## 3. Project Structure

```
shop-management/
│
├── backend/                   # Java backend server
│   ├── src/main/java/edu/ptithcm/
│   ├── pom.xml
│   ├── Dockerfile
│   └── resources
│
├── frontend/                  # Java Swing POS client
│   ├── src/main/java/edu/ptithcm/
│   ├── pom.xml
│   └── resources
│
├── database/                  # SQL schema & seed data
│   ├── schema.sql
│   ├── data.sql
│   └── ShopManagementERD.svg
│
├── docker-compose.yml
└── README.md
```

---

## 4. Requirements

| Component      | Version                     |
|----------------|-----------------------------|
| **JDK**        | 21+                         |
| **Maven**      | 3.9+                        |
| **Docker**     | 24+                         |
| **Compose**    | v2                          |
| **OS**         | Win / Linux / WSL2 / macOS  |

---

## 5. Installation

### Clone:

```
git clone https://github.com/OOP-TanQuanNguyen/shop-management.git
cd shop-management
```

---

## 6. Backend & Database Setup

### Configuration files:

**Backend App Config:**

```
backend/src/main/java/edu/ptithcm/configs/Config.java
```

**Database connection config (Hibernate Persistence):**

```
backend/src/main/resources/META-INF/persistence.xml
```

**Docker database config:**

```
docker-compose.yml
```

### Build & Start backend + database using Docker:

```
docker compose up --build
```

### Check logs:

```
docker logs mini_market_db
docker logs mini_market_backend
```

---

## 7. Frontend (Java Swing)

### Build:

```
cd frontend
mvn clean compile
```

### Run:

```
mvn exec:java
```

The POS client automatically connects to backend using DTTP.

---

## 8. DTTP Protocol Architecture

### Features:
- AES-256 encryption  
- GZIP compression  
- Non-blocking async request/response  
- Command-based routing  
- Minimal binary packet overhead  

### Packet Structure:

```
{
    "type": "DTTPType",
    "payload": { ... },
    "status": ["SUCCESS", "REQUEST", "ERROR", ...],
    "message": "message"
}
```

### DTTP Command Type Configuration:

```
backend/src/main/java/edu/ptithcm/configs/TypeDTTP.java
```

---


## 9. Developer Guidelines

### Code Architecture Conventions
- DTO → Mapper → Repository → Service  
- No business logic inside controllers  
- Clear separation between Model, DTO, Logic  
- Consistent Hibernate Mapping  
- Strongly typed models

### Git Workflow
- `main` → stable  
- `develop` → active development  

---

## 10. ERD Schema

ERD File:

```
database/ShopManagementERD.svg
```

---

## 11. Troubleshooting

### Contact Support  
(Click to open links)

- **Nguyễn Khánh Nguyên** — [Facebook](https://www.facebook.com/khanhnguyen2709)  
- **Đinh Việt Quân** — [Facebook](https://www.facebook.com/dinhvietquan)  
- **Đỗ Thanh Tân** — [Facebook](https://www.facebook.com/thanh.tan.552873)  

---

## 12. License

This project is for **educational and research purposes**.

---
