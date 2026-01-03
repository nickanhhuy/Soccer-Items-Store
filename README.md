# HuSoccer Shop - Soccer Equipment E-commerce Platform

![AWS Architecture](HuSoccerShop_AWS.drawio.png)

**Live Demo:** [https://husoccershop.store](https://husoccershop.store)

A full-stack e-commerce platform for soccer equipment built with Spring Boot and deployed on AWS with enterprise-grade security and scalability.

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

## Technology Stack

**Backend:** Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA, MySQL 8.0, Maven

**Frontend:** Thymeleaf, HTML5, CSS3, JavaScript

**Infrastructure:** Docker, Docker Compose, AWS (EC2, S3, RDS, Route 53, IAM), Nginx, Let's Encrypt

**Security:** BCrypt password hashing, CSRF protection, SSL/TLS encryption, IAM roles

---

## Architecture

### Application Architecture

The application follows a layered architecture pattern with clear separation of concerns:

```
Presentation Layer (Controllers, Templates)
           ↓
Business Layer (Services, DTOs, Validators)
           ↓
Data Layer (Repositories, Entities, MySQL)
```

### AWS Infrastructure

- **EC2 Instances**: Auto-scaling group with t2.medium instances
- **RDS MySQL**: Multi-AZ deployment with automated backups
- **S3 Bucket**: Private bucket for encrypted user data backups
- **Route 53**: DNS management for custom domain
- **Application Load Balancer**: SSL termination and traffic distribution
- **VPC**: Network isolation with public/private subnets
- **CloudWatch**: Monitoring, logging, and alerting
- **IAM Roles**: Secure service-to-service authentication

### Container Architecture

```
Docker Host (EC2)
├── soccer-app (Spring Boot on port 8080)
├── soccer-mysql (MySQL 8.0 on port 3306)
└── Docker volumes (persistent data storage)
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

```bash
# 1. Launch EC2 instance (t2.medium, Amazon Linux 2023)
# 2. Install Docker and Docker Compose
# 3. Clone repository and configure .env
# 4. Build and start containers
docker-compose up -d --build

# 5. Setup SSL with Let's Encrypt
sudo certbot --nginx -d husoccershop.store
```

### IAM Configuration

Create IAM role `SoccerShop-EC2-Role` with S3 access policy:

```json
{
    "Version": "2012-10-17",
    "Statement": [{
        "Effect": "Allow",
        "Action": ["s3:PutObject", "s3:GetObject", "s3:DeleteObject", "s3:ListBucket"],
        "Resource": ["arn:aws:s3:::husoccer-shop/*", "arn:aws:s3:::husoccer-shop"]
    }]
}
```

Attach role to EC2 instance for automated S3 backups.

**Detailed Guide:** [EC2-DEPLOYMENT.md](EC2-DEPLOYMENT.md)

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
**Email:** huynguyen@husoccer.com  
**Website:** [https://husoccershop.store](https://husoccershop.store)

---

**Built with Spring Boot and deployed on AWS**
