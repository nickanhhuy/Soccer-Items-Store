# HuSoccer Shop - Soccer Equipment E-commerce Platform

![AWS Architecture](HuSoccerShop_AWS.drawio.png)

## Live Demo
**Website:** [https://husoccershop.store](https://husoccershop.store)


## Overview
HuSoccer Shop is a modern e-commerce platform for soccer equipment built with Spring Boot and deployed on AWS EC2. The application features a complete online store with user authentication, product management, shopping cart, and order processing.

## Features

### Customer Features
- **Product Catalog**: Browse soccer boots, jerseys, and accessories
- **Search & Filter**: Real-time search with category and price filtering
- **Shopping Cart**: Add products with size and quantity selection
- **User Registration**: Create account and manage profile
- **Order History**: View past purchases and order details
- **Responsive Design**: Mobile-friendly interface

### Admin Features
- **Product Management**: Add, edit, and delete products
- **Inventory Control**: Track stock levels with visual indicators
- **Order Management**: View and process customer orders
- **User Management**: Admin role-based access control

### UI/UX Features
- **Modern Design**: Clean, professional interface
- **Image Slideshow**: Featured products carousel
- **Stock Indicators**: Visual stock status (In Stock, Low Stock, Out of Stock)
- **Sorting Options**: Sort by name, price, and stock availability

## Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL 8.0** - Database
- **Maven** - Dependency management

### Frontend
- **Thymeleaf** - Server-side templating
- **HTML5/CSS3** - Markup and styling
- **JavaScript** - Client-side functionality
- **Responsive Design** - Mobile compatibility

### Infrastructure
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **AWS EC2** - Cloud hosting
- **Nginx** - Reverse proxy and SSL termination
- **Let's Encrypt** - SSL certificates

## Architecture

### Application Architecture
The application follows a layered architecture pattern:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │    │    Business     │    │      Data       │
│     Layer       │◄──►│     Layer       │◄──►│     Layer       │
│  (Controllers)  │    │   (Services)    │    │ (Repositories)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### AWS Cloud Architecture

The application is deployed on AWS using a production-ready, highly available architecture as shown in the diagram above. The infrastructure leverages multiple AWS services for scalability, security, and reliability.

### AWS Components Used

### AWS Components Used

#### **EC2 (Elastic Compute Cloud)**
- **Instance Type**: t2.medium/t3.medium (2 vCPU, 4GB RAM)
- **Auto Scaling Group**: Automatic scaling based on demand
- **Multi-AZ Deployment**: High availability across availability zones
- **Operating System**: Amazon Linux 2023
- **Storage**: 20-30 GB EBS (Elastic Block Store)

#### **RDS (Relational Database Service)**
- **Engine**: MySQL 8.0
- **Instance Class**: db.t3.micro/small
- **Multi-AZ**: Automatic failover for high availability
- **Backup**: Automated daily backups with point-in-time recovery
- **Security**: VPC isolation, encryption at rest and in transit

#### **Route 53 (DNS Service)**
- **Domain**: husoccershop.store (registered via Namecheap)
- **DNS Management**: A records pointing to EC2 public IP
- **Health Checks**: Monitor application availability
- **Failover Routing**: Automatic traffic routing to healthy instances

#### **Application Load Balancer (ALB)**
- **Layer 7 Load Balancing**: HTTP/HTTPS traffic distribution
- **SSL Termination**: Centralized certificate management
- **Health Checks**: Automatic unhealthy instance removal
- **Sticky Sessions**: User session persistence

#### **Security Groups & NACLs**
- **Web Tier Security Group**:
  - HTTP (80): Public access
  - HTTPS (443): Public access
  - SSH (22): Admin access only
- **Database Security Group**:
  - MySQL (3306): Application tier only
  - No direct internet access

#### **VPC (Virtual Private Cloud)**
- **Multi-AZ Architecture**: Spans multiple availability zones
- **Public Subnets**: Web servers and load balancers
- **Private Subnets**: Database servers (RDS)
- **Internet Gateway**: Public internet connectivity
- **NAT Gateway**: Outbound internet for private subnets

#### **S3 (Simple Storage Service)**
- **Static Assets**: CSS, JavaScript, images
- **Database Backups**: Automated RDS backup storage
- **Application Logs**: Centralized log storage
- **Versioning**: File version control

#### **CloudWatch (Monitoring)**
- **Application Metrics**: CPU, memory, disk usage
- **Custom Metrics**: Application-specific monitoring
- **Alarms**: Automated notifications and scaling triggers
- **Log Aggregation**: Centralized logging from all services

#### **IAM (Identity and Access Management)**
- **EC2 Instance Roles**: Secure AWS service access
- **Least Privilege**: Minimal required permissions
- **Service Accounts**: Application-specific access controls

#### **Certificate Manager**
- **SSL/TLS Certificates**: Free AWS-managed certificates
- **Auto-Renewal**: Automatic certificate renewal
- **Integration**: Seamless ALB integration

### Container Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Docker Host (EC2)                       │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Docker Network                         │   │
│  │                                                     │   │
│  │  ┌─────────────────┐    ┌─────────────────────┐   │   │
│  │  │  soccer-app     │    │   soccer-mysql      │   │   │
│  │  │                 │    │                     │   │   │
│  │  │  Spring Boot    │◄──►│   MySQL 8.0         │   │   │
│  │  │  Port: 8080     │    │   Port: 3306        │   │   │
│  │  └─────────────────┘    └─────────────────────┘   │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Traffic Flow

1. **User Request**: Browser → husoccershop.store
2. **DNS Resolution**: Namecheap DNS → EC2 Public IP
3. **SSL Termination**: Nginx handles HTTPS encryption
4. **Reverse Proxy**: Nginx forwards to Spring Boot (port 8080)
5. **Application Processing**: Spring Boot processes business logic
6. **Database Query**: Spring Boot queries MySQL container
7. **Response**: Data flows back through the same path

### Security Architecture

- **Network Security**: Security Groups, VPC isolation, SSH key authentication
- **Transport Security**: SSL/TLS encryption, HTTPS redirect, secure headers
- **Application Security**: Spring Security, BCrypt password hashing, CSRF protection, role-based access
- **Data Security**: Database authentication, encrypted storage, container isolation

## Application Specialties & Unique Features

### **Soccer-Focused E-commerce**
- **Specialized Product Catalog**: Curated selection of soccer boots, jerseys, and accessories from top brands (Nike, Adidas, Puma, New Balance)
- **Team Merchandise**: Official jerseys from Premier League, World Cup, and major soccer clubs
- **Size Management**: Comprehensive size selection system for different product categories
- **Soccer-Themed UI**: Custom design with soccer imagery and slideshow featuring professional players

### **Advanced Product Management**
- **Dynamic Inventory System**: Real-time stock tracking with visual indicators
  - **In Stock**: Available for purchase
  - **Low Stock**: Limited quantity warnings
  - **Out of Stock**: Automatic purchase prevention
- **Smart Search & Filtering**: Real-time product search with category-based filtering
- **Sorting Capabilities**: Sort by name, price, stock availability
- **Quantity Limits**: Prevents over-ordering based on available stock

### **Enhanced Shopping Experience**
- **Interactive Product Cards**: Hover effects and detailed product information
- **Size Selection**: Mandatory size selection for proper fitting
- **Quantity Controls**: Intuitive increment/decrement with stock validation
- **Visual Feedback**: Immediate stock status updates and purchase confirmations
- **Responsive Design**: Seamless experience across desktop, tablet, and mobile devices

### **Professional Admin Panel**
- **Complete Product Lifecycle Management**: Add, edit, delete products with image upload
- **Order Management System**: View and track customer orders with detailed information
- **User Role Management**: Secure admin access with role-based permissions
- **Inventory Dashboard**: Monitor stock levels and product performance
- **Real-time Updates**: Changes reflect immediately on the storefront

### **Enterprise-Grade Security**
- **Multi-Layer Authentication**: Spring Security with BCrypt password encryption
- **Role-Based Access Control**: Separate USER and ADMIN privileges
- **CSRF Protection**: Cross-site request forgery prevention
- **Session Management**: Secure user session handling
- **SQL Injection Prevention**: JPA/Hibernate protection against database attacks

### **Modern Development Practices**
- **Containerized Architecture**: Docker and Docker Compose for consistent deployment
- **Cloud-Native Design**: AWS-optimized with auto-scaling capabilities
- **Database Optimization**: Efficient JPA queries with connection pooling
- **RESTful API Design**: Clean, maintainable controller architecture
- **Separation of Concerns**: Layered architecture (Controller → Service → Repository)

### **User Experience Excellence**
- **Intuitive Navigation**: Clean, professional interface without cluttered icons
- **Visual Product Showcase**: High-quality product images with slideshow carousel
- **Instant Feedback**: Real-time search results and stock updates
- **Error Handling**: Graceful error messages and user guidance
- **Performance Optimized**: Fast loading times with efficient resource management

### **Operational Excellence**
- **Automated Data Seeding**: Pre-populated product catalog for immediate functionality
- **Health Monitoring**: Application and database health checks
- **Scalable Infrastructure**: Auto-scaling groups and load balancing
- **Backup & Recovery**: Automated database backups with point-in-time recovery
- **SSL/HTTPS**: Secure communication with Let's Encrypt certificates

### **Business Intelligence Ready**
- **Order Tracking**: Complete order history and customer purchase patterns
- **Inventory Analytics**: Stock level monitoring and reorder alerts
- **User Behavior**: Session management and user interaction tracking
- **Performance Metrics**: Application performance and usage statistics

### **Production-Ready Features**
- **Custom Domain**: Professional branding with husoccershop.store
- **CDN Integration**: Fast content delivery with S3 static assets
- **Monitoring & Alerting**: CloudWatch integration for proactive issue detection
- **Disaster Recovery**: Multi-AZ deployment with automatic failover
- **Compliance Ready**: Security best practices for e-commerce applications

## Installation & Deployment

### Prerequisites
- Java 17+
- Docker & Docker Compose
- AWS Account (for cloud deployment)

### Local Development
```bash
# Clone repository
git clone <repository-url>
cd Soccer-Items-Store

# Run with Docker Compose
docker-compose up --build

# Access application
http://localhost:8080
```### AWS EC
2 Deployment
```bash
# 1. Launch EC2 instance (t2.medium recommended)
# 2. Configure Security Groups (ports 22, 80, 443, 8080)
# 3. Deploy application
./deploy.sh

# 4. Setup SSL with Let's Encrypt
sudo certbot --nginx -d yourdomain.com
```

Detailed deployment guide: [EC2-DEPLOYMENT.md](EC2-DEPLOYMENT.md)

## Database Schema

### Core Entities
- **User**: User accounts with role-based access
- **Item**: Product catalog (boots, jerseys, accessories)
- **Order**: Customer orders with timestamps
- **OrderItem**: Individual items within orders

### Sample Data
The application includes a DataInitializer that seeds the database with:
- 13 Soccer boots (Nike, Adidas, Puma, New Balance)
- 12 Team jerseys (Premier League, World Cup)
- 6 Accessories (balls, gloves, equipment)

## Security Features
- **Password Encryption**: BCrypt hashing
- **Role-based Access**: USER and ADMIN roles
- **CSRF Protection**: Cross-site request forgery prevention
- **Session Management**: Secure session handling
- **SSL/HTTPS**: Encrypted communication

## Performance Features
- **Docker Optimization**: Multi-stage builds for smaller images
- **Database Indexing**: Optimized queries
- **Static Resource Caching**: Improved load times
- **Connection Pooling**: Efficient database connections

## API Endpoints

### Public Endpoints
- `GET /` - Home page redirect
- `GET /menu` - Product catalog
- `GET /login` - Login page
- `GET /register` - Registration page

### Protected Endpoints
- `POST /order` - Place order (authenticated users)
- `GET /history` - Order history (authenticated users)
- `GET /admin` - Admin panel (admin only)
- `POST /admin/add` - Add product (admin only)

## Configuration

### Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/[database-name]
SPRING_DATASOURCE_USERNAME=[db-username]
SPRING_DATASOURCE_PASSWORD=[db-password]
```

### Docker Configuration
- **Application Port**: 8080
- **MySQL Port**: 3307 (host) / 3306 (container)
- **Volumes**: Persistent MySQL data storage

## Testing
```bash
# Run tests
mvn test

# Build without tests
mvn clean package -DskipTests
```

## Monitoring & Logs
```bash
# View application logs
docker-compose logs -f app

# Check container status
docker-compose ps
```

## CI/CD Pipeline
The project supports automated deployment with GitHub Actions for testing, Docker image building, and EC2 deployment.

## Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Author
**[Your Name]**
- Website: [husoccershop.store](https://husoccershop.store)
- Email: [your-email@domain.com]

## Acknowledgments
- Spring Boot community for excellent documentation
- AWS for reliable cloud infrastructure
- Let's Encrypt for free SSL certificates
- Docker for containerization technology

---

**Built with passion for soccer and technology!**