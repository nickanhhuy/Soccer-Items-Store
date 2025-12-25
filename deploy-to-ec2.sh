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

# Deploy on EC2
ssh -i "$KEY_FILE" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_IP" << 'ENDSSH'
cd ~/soccer-store
sudo docker-compose down -v 2>/dev/null || true
sudo docker-compose up -d --build
sleep 10
sudo docker-compose ps
ENDSSH

echo "Deployment complete! App available at: http://$EC2_IP:8080"
