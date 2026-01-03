# Deployment Scripts

This folder contains scripts for deploying the HuSoccer Shop application to various environments.

## Scripts

### deploy.sh
Main deployment script with full configuration and setup.

### deploy-simple.sh
Simplified deployment script for quick deployments.

### deploy-to-ec2.sh
AWS EC2-specific deployment with IAM and security group configuration.

### ec2-setup.sh
Initial EC2 instance setup script (installs Docker, configures environment).

## Usage

```bash
# Make scripts executable
chmod +x *.sh

# Run deployment
./deploy.sh
```

Refer to [EC2-DEPLOYMENT.md](../docs/EC2-DEPLOYMENT.md) for detailed deployment instructions.
