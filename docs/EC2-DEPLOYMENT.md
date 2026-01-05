# EC2 Deployment Guide for Soccer Store

## Prerequisites
- AWS Account
- GitHub repository with your code
- SSH key pair for EC2 access

## Step 1: Launch EC2 Instance

### Instance Configuration:
- **AMI**: Amazon Linux 2023 or Ubuntu 22.04 LTS
- **Instance Type**: t2.medium or t3.medium (minimum)
  - 2 vCPU, 4 GB RAM
  - For production: t3.large recommended
- **Storage**: 20-30 GB gp3
- **Key Pair**: Create or use existing SSH key

### Security Group Rules:
| Type | Protocol | Port | Source | Description |
|------|----------|------|--------|-------------|
| SSH | TCP | 22 | Your IP | SSH access |
| HTTP | TCP | 80 | 0.0.0.0/0 | HTTP access |
| Custom TCP | TCP | 8080 | 0.0.0.0/0 | Application |
| Custom TCP | TCP | 3307 | Your IP (optional) | MySQL access |

## Step 2: Connect to EC2

```bash
# Replace with your key and EC2 public IP
ssh -i your-key.pem ec2-user@your-ec2-public-ip

# For Ubuntu:
ssh -i your-key.pem ubuntu@your-ec2-public-ip
```

## Step 3: Initial Setup

```bash
# Clone your repository
git clone https://github.com/your-username/your-repo.git
cd your-repo/Soccer-Items-Store

# Make scripts executable
chmod +x ec2-setup.sh deploy.sh

# Run initial setup (installs Docker, Docker Compose, Git)
./ec2-setup.sh

# IMPORTANT: Log out and log back in for Docker group to take effect
exit
```

## Step 4: Deploy Application

```bash
# SSH back into EC2
ssh -i your-key.pem ec2-user@your-ec2-public-ip

# Navigate to project
cd your-repo/Soccer-Items-Store

# Deploy the application
./deploy.sh
```

## Step 5: Access Your Application

Your app will be available at:
```
http://your-ec2-public-ip:8080
```

**Default Admin Login:**
- Username: `huynguyen`
- Password: `admin123`

## Optional: Setup Nginx Reverse Proxy (Port 80)

```bash
# Install Nginx
sudo yum install -y nginx  # Amazon Linux
# OR
sudo apt-get install -y nginx  # Ubuntu

# Create Nginx config
sudo tee /etc/nginx/conf.d/soccer-store.conf > /dev/null <<EOF
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

# Start Nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

Now access via: `http://your-ec2-public-ip` (port 80)

## Useful Commands

```bash
# View application logs
docker-compose logs -f app

# View MySQL logs
docker-compose logs -f mysql

# Restart application
docker-compose restart app

# Stop everything
docker-compose down

# Rebuild and restart
./deploy.sh

# Check container status
docker-compose ps

# Access MySQL directly
docker exec -it soccer-mysql mysql -u socceruser -psoccerpass soccerstore
```

## Troubleshooting

### Application won't start
```bash
# Check logs
docker-compose logs app

# Check if ports are in use
sudo netstat -tulpn | grep 8080
sudo netstat -tulpn | grep 3307
```

### Out of memory
```bash
# Check memory usage
free -h

# Upgrade to larger instance type (t3.large)
```

### Can't connect to application
1. Check Security Group allows port 8080
2. Check application is running: `docker-compose ps`
3. Check EC2 public IP hasn't changed

### Database connection issues
```bash
# Restart MySQL container
docker-compose restart mysql

# Check MySQL logs
docker-compose logs mysql
```

## Production Recommendations

1. **Use RDS for MySQL** instead of containerized MySQL
2. **Setup SSL/HTTPS** with Let's Encrypt
3. **Use Elastic IP** to prevent IP changes
4. **Setup CloudWatch** for monitoring
5. **Configure automated backups**
6. **Use Application Load Balancer** for high availability
7. **Store images in S3** instead of local storage
8. **Use environment variables** for sensitive data
9. **Setup CI/CD pipeline** with GitHub Actions
10. **Enable auto-scaling** for traffic spikes

## Cost Optimization

- Use t3.medium with reserved instances for 40% savings
- Stop instance when not in use (development)
- Use spot instances for non-critical environments
- Monitor with AWS Cost Explorer

## Security Best Practices

1. Change default admin password immediately
2. Restrict SSH access to your IP only
3. Keep system and Docker updated
4. Use AWS Secrets Manager for credentials
5. Enable CloudTrail for audit logging
6. Regular security patches
