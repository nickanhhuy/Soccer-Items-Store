#!/bin/bash

echo "Starting Soccer Store deployment on EC2..."

# Stop existing containers
echo "Stopping existing containers..."
docker-compose down -v

# Build and start containers
echo "Building and starting containers..."
docker-compose up -d --build

# Wait for containers to start
echo "Waiting for containers to start..."
sleep 30

# Check container status
echo "Checking container status..."
docker-compose ps

# Show logs
echo "Recent application logs:"
docker-compose logs app --tail 30

# Get public IP
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4 2>/dev/null || echo "localhost")

echo "========================================="
echo "Deployment complete!"
echo "========================================="
echo "Application URL: http://$PUBLIC_IP:8080"
echo "Admin Login: huynguyen / admin123"
echo ""
echo "Useful commands:"
echo "  View logs: docker-compose logs -f"
echo "  Stop app: docker-compose down"
echo "  Restart: docker-compose restart"
echo "========================================="
