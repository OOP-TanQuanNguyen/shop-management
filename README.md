# Shop Management System
Java Desktop (Swing) + Backend Server + MySQL + Custom DTTP Protocol

This repository contains a full multi-module Shop Management / POS System designed with enterprise-style architecture. It includes:

- Java backend server (Hibernate + HikariCP + MySQL)
- Java Swing desktop client
- MySQL database schema
- Custom encrypted socket protocol (DTTP)
- Optional Docker deployment

# 1. System Overview
The system manages:
- Products & Categories
- Branch & Inventory
- Employees, Customers, Loyalty
- Invoice creation, confirmation, cancellation
- Secure encrypted communication

# 2. Architecture
## 2.1 High-Level Architecture
Frontend Swing → DTTP (AES-GCM) → Backend Server → MySQL

## 2.2 Backend Layers
Controller, Service, Repository, Entity Models, DTO/Mapper, Utility.

## 2.3 Frontend Architecture
MVC + Store pattern, modular Swing UI, DTTP client.

# 3. Folder Structure
backend/, frontend/, database/, docker-compose.yml

# 4. Database Schema
Normalized 3NF tables for POS operations.

# 5. DTTP Protocol
Custom low-latency encrypted protocol using:
- AES-GCM
- Optional GZIP
- Message types: REQUEST, SUCCESS, INVALID, ERROR

# 6. Running the Project
Backend:
    docker-compose up
Frontend:
    mvn clean package ; java -jar target/frontend.jar

# 7. Deployment
Supports Docker, Podman, future Kubernetes.

# 8. Authentication & Security
SHA-256 executable integrity verification (future upgrade).
AES encrypted communication.

# 9. Future Improvements
- Distributed caching
- REST/gRPC API
- WebSocket updates
- Microservices redesign
- JWT + RBAC
- Web + Mobile clients
- Kubernetes + autoscaling
- CI/CD pipelines
- Centralized logging & monitoring

# 10. Author
Tan Quan Nguyen