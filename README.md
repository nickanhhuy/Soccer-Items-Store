# HuSoccer Shop - Soccer Equipment E-commerce Platform

**Live Demo:** https://husoccershop.store

## Architecture

![AWS Architecture](docs/HuSoccerShop_AWS.png)

A full-stack e-commerce platform for soccer equipment built with Spring Boot and deployed on AWS EC2 with Docker containerization.

---

## Overview

HuSoccer Shop is a production-ready e-commerce application featuring user authentication, shopping cart functionality, order management, and an admin dashboard. The application demonstrates modern software engineering practices including containerization, cloud deployment, and secure data handling.

**Key Highlights:**
- Full-stack Java application with Spring Boot
- Deployed on AWS EC2 with Docker
- Secure authentication and role-based access control
- Real-time analytics dashboard
- Automated S3 backups with encryption
- Responsive modern UI design

---

## Features

### Customer Features
- Browse product catalog with real-time search and filtering
- Shopping cart with size and quantity selection
- User registration and profile management
- Order history and tracking
- Avatar upload and customization
- Secure checkout with multiple payment methods

### Admin Features
- Product management (CRUD operations)
- User management (create, edit, delete, role assignment)
- Order management and tracking
- Analytics dashboard with revenue tracking
- Inventory control with stock indicators
- Automated S3 backups for user data

---

## Screenshots

### User Interface
![Application UI](docs/UI.png)

### Shopping Cart
![Shopping Cart](docs/cart.png)

### Admin Dashboard
![Admin Panel](docs/admin.png)

### Email Notifications
![Email Verification](docs/emailNotify.png)

---

## Technology Stack

**Backend:** Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA, MySQL 8.0, Maven

**Frontend:** Thymeleaf, HTML5, CSS3, JavaScript

**Infrastructure:** Docker, Docker Compose, AWS (EC2, S3), Resend SMTP

**Security:** BCrypt password hashing, CSRF protection, SSL/TLS encryption, IAM roles

---


### Current Deployment Architecture

**Production Environment:**
- **EC2 Instance**: `18.206.76.32` (t2.medium, Amazon Linux 2023)
- **Containerized Services**: Docker Compose orchestration
  - Spring Boot Application (Port 8080)
  - MySQL 8.0 Database (Port 3307)
- **Email Service**: Resend SMTP for transactional emails
- **Storage**: AWS S3 for user data backups (optional)

**Container Architecture:**
```
Docker Host (EC2)
├── soccer-app (Spring Boot on port 8080)
│   └── Volumes: avatar-uploads
├── soccer-mysql (MySQL 8.0 on port 3307)
│   └── Volumes: mysql-data
└── Docker Network: soccer-network (internal communication)
```

**Data Flow:**
1. User Request → EC2 Instance (Port 8080) → Spring Boot Application
2. Application → MySQL Database (internal Docker network)
3. Email Notifications → Resend SMTP Service
4. User Data Backup → AWS S3 (encrypted, private bucket)

---

## Environment Setup

This application uses environment variables for sensitive configuration.

### Local Development Setup

1. Copy the `.env.example` file to `.env`:
```bash
cp .env.example .env
```

2. Update the `.env` file with your actual credentials:
   - Database credentials
   - Admin user credentials
   - Email service credentials (Resend API)
   - AWS credentials (optional)

3. **Important**: Never commit the `.env` file to Git. It's already in `.gitignore`.

### Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database connection URL | `jdbc:mysql://mysql:3306/soccerstore` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `socceruser` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `your_password` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate DDL mode | `update` |
| `ADMIN_USERNAME` | Admin account username | `huynguyen` |
| `ADMIN_PASSWORD` | Admin account password | `your_admin_password` |
| `ADMIN_EMAIL` | Admin email address | `admin@example.com` |
| `MAIL_HOST` | SMTP server host | `smtp.resend.com` |
| `MAIL_PORT` | SMTP server port | `587` |
| `MAIL_USERNAME` | Email username | `resend` |
| `MAIL_PASSWORD` | Email API key | `your_resend_api_key` |
| `MAIL_FROM` | Sender email address | `noreply@yourdomain.com` |
| `AWS_BUCKET_NAME` | S3 bucket name (optional) | `husoccer-shop` |
| `AWS_REGION` | AWS region | `us-east-1` |
| `SERVER_PORT` | Application port | `8080` |
| `APP_BASE_URL` | Application base URL | `https://husoccershop.site` |

### Running the Application

**With Docker Compose (Recommended):**
```bash
docker-compose up -d --build
```

**Without Docker:**
```bash
./mvnw spring-boot:run
```

The application will automatically load environment variables from the `.env` file.

### AWS EC2 Deployment

For AWS EC2 deployment, set environment variables directly on your EC2 instance:

1. SSH into your EC2 instance
2. Clone the repository
3. Create and configure the `.env` file
4. Run with Docker Compose

**Default Admin Credentials:**
```
Username: huynguyen
Password: admin123
```

---

## Installation & Setup

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Git

### Local Development

```bash
# Clone repository
git clone https://github.com/yourusername/Soccer-Items-Store.git
cd Soccer-Items-Store

# Configure environment
cp .env.example .env
# Edit .env with your configuration

# Build and run
docker-compose up --build

# Access application
http://localhost:8080
```

### Default Admin Credentials
```
Username: huynguyen
Password: admin123
```

---

## Deployment

### AWS EC2 Deployment

**Current Production Setup:**
- **Instance**: EC2 t2.medium (Amazon Linux 2023)
- **Public IP**: 18.206.76.32
- **Application URL**: http://18.206.76.32:8080

```bash
# 1. Connect to EC2
ssh -i hu.pem ec2-user@18.206.76.32

# 2. Clone repository
git clone https://github.com/yourusername/Soccer-Items-Store.git
cd Soccer-Items-Store/Soccer-Items-Store

# 3. Configure environment
nano .env
# Update with your configuration (database, email, etc.)

# 4. Build and start containers
docker-compose up -d --build

# 5. Verify deployment
docker-compose ps
docker-compose logs -f app
```

**Security Group Configuration:**
- Port 22 (SSH) - Your IP only
- Port 8080 (HTTP) - 0.0.0.0/0 (public access)
- Port 3307 (MySQL) - Optional, for direct DB access

**Email Service Setup:**
The application uses Resend for transactional emails. Configure in `.env`:
```properties
MAIL_HOST=smtp.resend.com
MAIL_PORT=587
MAIL_USERNAME=resend
MAIL_PASSWORD=your-resend-api-key
MAIL_FROM=noreply@yourdomain.com
```

**Detailed Guide:** [EC2-DEPLOYMENT.md](docs/EC2-DEPLOYMENT.md)

---

## Database Schema

### Core Entities

**User** - User accounts with authentication and profile data
- user_id, userName, password (BCrypt), email, role, fullName, phone, avatarUrl

**Item** - Product catalog
- id, name, category, quantity, price, gender, sizes, image

**Order** - Customer orders
- id, username, fullName, address, city, state, zipCode, phone, paymentMethod, totalAmount, orderDate

**OrderItem** - Order line items
- id, order_id (FK), productName, category, size, quantity, price, image

---

## API Endpoints

### Public Access
- `GET /` - Home page
- `GET /menu` - Product catalog
- `GET /login` - Login page
- `POST /register` - User registration

### Authenticated Users
- `POST /order` - Place order
- `GET /history` - Order history
- `GET /profile` - User profile
- `POST /profile/upload-avatar` - Upload avatar

### Admin Only
- `GET /admin` - Admin dashboard
- `POST /admin/saveItem` - Add/edit products
- `POST /admin/createUser` - Create users
- `GET /analytics` - Analytics dashboard
- `GET /api/s3/**` - S3 operations

---

## Security Features

- Spring Security with BCrypt password encryption
- Role-based access control (USER/ADMIN)
- CSRF protection on all forms
- SQL injection prevention via JPA
- XSS protection with Thymeleaf escaping
- HTTPS/SSL encryption
- Secure session management
- Private S3 backups (passwords excluded)
- Input validation and sanitization

---

## Performance & Scalability

- Docker containerization for consistent deployment
- Auto-scaling EC2 instances
- Multi-AZ RDS for high availability
- Application Load Balancer for traffic distribution
- CloudWatch monitoring and alerting
- Optimized database queries with JPA
- Static resource caching

---

## Project Structure

```
Soccer-Items-Store/
├── src/main/java/com/example/socceritemsstore/
│   ├── controller/      # REST controllers
│   ├── service/         # Business logic
│   ├── repository/      # Data access
│   ├── model/           # Entity classes
│   ├── dto/             # Data transfer objects
│   ├── config/          # Configuration classes
│   └── security/        # Security configuration
├── src/main/resources/
│   ├── templates/       # Thymeleaf templates
│   ├── static/          # CSS, JS, images
│   └── application.properties
├── docker-compose.yml   # Container orchestration
├── Dockerfile           # Application container
└── pom.xml             # Maven dependencies
```

---

## Monitoring & Logs

```bash
# View application logs
docker-compose logs -f app

# Check container status
docker-compose ps

# Monitor resource usage
docker stats
```

---

## Contact

**Developer:** Huy Nguyen  
**Email:** vuanhhuynguyen053004@gmail.com  
**Live Application:** http://18.206.76.32:8080

---

**Built with Spring Boot and deployed on AWS EC2 with Docker**
