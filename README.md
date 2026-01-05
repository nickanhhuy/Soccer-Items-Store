# HuSoccer Shop - E-commerce Platform

**Live Demo:** https://husoccershop.site

A full-stack e-commerce platform for soccer equipment built with Spring Boot and deployed on AWS EC2 with Docker.

**Tech Stack:** Java 17 • Spring Boot • MySQL • Docker • AWS EC2 • Resend SMTP

---

## Overview

Production-ready e-commerce application featuring user authentication, shopping cart, order management, and admin dashboard. Demonstrates modern software engineering practices including containerization, cloud deployment, and secure data handling.

**Key Highlights:**
- Secure authentication with role-based access control (USER/ADMIN)
- Real-time inventory management and analytics
- Automated email notifications with Resend
- Docker containerization for consistent deployment
- AWS S3 integration for data backups

---

## Architecture

![AWS Architecture](docs/HuSoccerShop_AWS.png)

**Deployment:**
- **Platform:** AWS EC2 (t2.medium, Amazon Linux 2023)
- **Containers:** Docker Compose (Spring Boot + MySQL 8.0)
- **Email:** Resend SMTP for transactional emails
- **Storage:** AWS S3 for encrypted backups

---

## Features

### 🛒 Shopping Experience
Browse products, add to cart with size/quantity selection, and secure checkout with multiple payment methods.

![Application UI](docs/UI.png)

### 🛍️ Shopping Cart
Real-time cart management with dynamic pricing and inventory validation.

![Shopping Cart](docs/cart.png)

### 👨‍💼 Admin Dashboard
Complete product, user, and order management with analytics and revenue tracking.

![Admin Panel](docs/admin.png)

### 📧 Email Notifications
Automated email verification and order confirmations using Resend API.

![Email Verification](docs/emailNotify.png)

---

## Quick Start

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Git

### Local Setup

```bash
# Clone and navigate
git clone https://github.com/yourusername/Soccer-Items-Store.git
cd Soccer-Items-Store

# Configure environment
cp .env.example .env
# Edit .env with your credentials

# Run with Docker
docker-compose up -d --build

# Access at http://localhost:8080
```

**Default Admin:** `huynguyen` / `admin123`

---

## Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:mysql://mysql:3306/soccerstore` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `socceruser` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `your_password` |
| `ADMIN_USERNAME` | Admin username | `huynguyen` |
| `ADMIN_PASSWORD` | Admin password | `your_password` |
| `MAIL_HOST` | SMTP host | `smtp.resend.com` |
| `MAIL_PASSWORD` | Resend API key | `your_api_key` |
| `MAIL_FROM` | Sender email | `noreply@yourdomain.com` |
| `AWS_BUCKET_NAME` | S3 bucket (optional) | `husoccer-shop` |

**Setup:** Copy `.env.example` to `.env` and update with your credentials. Never commit `.env` to Git.

---

## Key Features

### Customer Features
- Product catalog with search and filtering
- Shopping cart with size/quantity selection
- User registration with email verification
- Order history and tracking
- Profile management with avatar upload

### Admin Features
- Product management (CRUD operations)
- User management with role assignment
- Order tracking and management
- Analytics dashboard with revenue metrics
- Inventory control with stock alerts

### Security
- BCrypt password encryption
- Role-based access control (RBAC)
- CSRF protection
- SQL injection prevention (JPA)
- XSS protection (Thymeleaf)
- Secure session management

---

## Database Schema

**User:** Authentication and profile data (userName, password, email, role, avatarUrl)  
**Item:** Product catalog (name, category, price, quantity, sizes, image)  
**Order:** Customer orders (fullName, address, paymentMethod, totalAmount, orderDate)  
**OrderItem:** Order line items (productName, size, quantity, price)

---

## API Endpoints

**Public:** `/` (home), `/menu` (catalog), `/login`, `/register`  
**Authenticated:** `/order`, `/history`, `/profile`, `/profile/upload-avatar`  
**Admin:** `/admin`, `/admin/saveItem`, `/admin/createUser`, `/analytics`

---

## Deployment

### AWS EC2 Production

```bash
# Connect to EC2
ssh -i hu.pem ec2-user@your-ec2-ip

# Clone and setup
git clone https://github.com/yourusername/Soccer-Items-Store.git
cd Soccer-Items-Store/Soccer-Items-Store
nano .env  # Configure environment

# Deploy
docker-compose up -d --build

# Verify
docker-compose ps
docker-compose logs -f app
```

**Security Group:** Allow ports 22 (SSH), 8080 (HTTP), 3307 (MySQL - optional)

---

## Project Structure

```
Soccer-Items-Store/
├── src/main/java/com/example/socceritemsstore/
│   ├── controller/      # REST controllers
│   ├── service/         # Business logic
│   ├── repository/      # Data access
│   ├── model/           # JPA entities
│   ├── config/          # Spring configuration
│   └── security/        # Security config
├── src/main/resources/
│   ├── templates/       # Thymeleaf views
│   ├── static/          # CSS, JS, images
│   └── application.properties
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

---

## Contact

**Developer:** Huy Nguyen  
**Email:** vuanhhuynguyen053004@gmail.com  
**Live Site:** https://husoccershop.site

---

**Built with Spring Boot • Deployed on AWS EC2 • Containerized with Docker**
