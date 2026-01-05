# Payment Methods Feature

## Overview
The payment methods feature allows users to securely store and manage their payment cards for faster checkout. This feature includes:

- Add multiple payment methods (Visa, Mastercard, Amex, Discover)
- Set default payment method
- Remove payment methods
- Secure card number masking
- Card type auto-detection
- Expiry date validation

## Security Features
- Card numbers are stored securely in the database
- CVV is not stored (only used for validation during addition)
- Card numbers are masked in the UI (showing only last 4 digits)
- Input validation for all card fields
- XSS protection through Thymeleaf templating

## Database Schema
The `payment_method` table includes:
- `id` - Primary key
- `user_id` - Foreign key to user table
- `card_holder_name` - Name on the card
- `card_number` - Full card number (16 digits)
- `expiry_month` - MM format
- `expiry_year` - YYYY format
- `cvv` - 3-4 digit security code (not stored after validation)
- `card_type` - VISA, MASTERCARD, AMEX, DISCOVER
- `is_default` - Boolean flag for default payment method
- `created_at` - Timestamp
- `updated_at` - Timestamp

## API Endpoints
- `GET /profile` - View profile with payment methods
- `POST /profile/add-payment-method` - Add new payment method
- `POST /profile/remove-payment-method/{id}` - Remove payment method
- `POST /profile/set-default-payment/{id}` - Set default payment method

## Validation Rules
- Card number: 16 digits, numeric only
- Expiry month: 01-12
- Expiry year: Current year or future
- CVV: 3-4 digits
- Card holder name: Required, max 100 characters
- No duplicate card numbers per user

## UI Features
- Modern, responsive design
- Real-time card type detection
- Card number formatting (spaces every 4 digits)
- Modal form for adding payment methods
- Confirmation dialogs for destructive actions
- Success/error message notifications

## Future Enhancements
- Integration with payment processors (Stripe, PayPal)
- Card encryption at rest
- PCI DSS compliance improvements
- Billing address support
- Payment method verification