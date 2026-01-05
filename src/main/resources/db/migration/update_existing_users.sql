-- Migration script to update existing users with email verification fields
-- Run this after deploying the new version with email verification

-- Add new columns if they don't exist (for existing databases)
ALTER TABLE user 
ADD COLUMN IF NOT EXISTS emailVerified BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS verificationToken VARCHAR(64),
ADD COLUMN IF NOT EXISTS tokenExpiryDate DATETIME;

-- Add unique constraint to email column if it doesn't exist
ALTER TABLE user 
ADD CONSTRAINT unique_email UNIQUE (email);

-- Mark all existing users as verified (they registered before verification was required)
UPDATE user 
SET emailVerified = TRUE 
WHERE emailVerified IS NULL OR emailVerified = FALSE;

-- Clear any verification tokens for existing users
UPDATE user 
SET verificationToken = NULL, 
    tokenExpiryDate = NULL 
WHERE verificationToken IS NOT NULL;
