#!/bin/bash

echo "Starting Soccer Store deployment..."

# Stop existing containers
echo "Stopping existing containers..."
docker-compose down

# Removeold images (optional - saves space)
echo "Cleaning up old images..."
docker image prune -f

# Build and start containers
echo "Building and starting containers..."
docker-compose up -d --build

# Wait a moment for containers to start
echo "Waiting for containers to start..."
sleep 10

# Check container status
echo "Checking container status..."
docker-compose ps

# Show logs
echo "Recent application logs:"
docker-compose logs app --tail 20

echo "Deployment complete!"
echo "Access your application at: http://localhost:8080"
echo "View logs with: docker-compose logs -f app"