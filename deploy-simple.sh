#!/bin/bash

EC2_IP="3.239.114.25"
EC2_USER="ec2-user"
KEY_FILE="/tmp/hu.pem"

echo "Deploying to EC2: $EC2_IP"

# Copy key to temp with correct permissions
cp "../hu.pem" "$KEY_FILE"
chmod 600 "$KEY_FILE"

# Copy files to EC2
scp -i "$KEY_FILE" -o StrictHostKeyChecking=no -r . "$EC2_USER@$EC2_IP:~/soccer-store/"

# Deploy using Docker directly
ssh -i "$KEY_FILE" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_IP" << 'ENDSSH'
cd ~/soccer-store

# Stop existing containers
sudo docker stop soccer-app soccer-mysql 2>/dev/null || true
sudo docker rm soccer-app soccer-mysql 2>/dev/null || true

# Create network
sudo docker network create soccer-network 2>/dev/null || true

# Start MySQL
sudo docker run -d \
  --name soccer-mysql \
  --network soccer-network \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=soccerstore \
  -e MYSQL_USER=socceruser \
  -e MYSQL_PASSWORD=soccerpass \
  -p 3307:3306 \
  mysql:8.0

# Wait for MySQL
sleep 30

# Build and run app
sudo docker build -t soccer-app .
sudo docker run -d \
  --name soccer-app \
  --network soccer-network \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://soccer-mysql:3306/soccerstore \
  -e SPRING_DATASOURCE_USERNAME=socceruser \
  -e SPRING_DATASOURCE_PASSWORD=soccerpass \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  -p 8080:8080 \
  soccer-app

# Check status
sleep 10
sudo docker ps
ENDSSH

echo "Deployment complete! App available at: http://$EC2_IP:8080"
