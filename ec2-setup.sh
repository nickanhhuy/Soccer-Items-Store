#!/bin/bash

# EC2 Initial Setup Script
# Run this ONCE on a fresh EC2 instance

set -e

echo "========================================="
echo "Soccer Store - EC2 Initial Setup"
echo "========================================="

# Update system
echo "Updating system packages..."
sudo yum update -y 2>/dev/null || sudo apt-get update -y

# Install Docker
echo "Installing Docker..."
if ! command -v docker &> /dev/null; then
    # For Amazon Linux 2023
    sudo yum install -y docker 2>/dev/null || sudo apt-get install -y docker.io
    sudo systemctl start docker
    sudo systemctl enable docker
    sudo usermod -aG docker $USER
    echo "Docker installed successfully"
else
    echo "Docker already installed"
fi

# Install Docker Compose
echo "Installing Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    echo "Docker Compose installed successfully"
else
    echo "Docker Compose already installed"
fi

# Install Git
echo "Installing Git..."
if ! command -v git &> /dev/null; then
    sudo yum install -y git 2>/dev/null || sudo apt-get install -y git
fi

echo "========================================="
echo "Setup Complete!"
echo "========================================="
echo "Next steps:"
echo "1. Log out and log back in (for Docker group to take effect)"
echo "2. Clone your repository: git clone <your-repo-url>"
echo "3. cd into the project directory"
echo "4. Run: ./deploy.sh"
echo "========================================="
