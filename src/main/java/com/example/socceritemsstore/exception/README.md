# Exception Handling - Simplified for Students

This folder contains simple exception handling for the Soccer Items Store.

## What's Here

### Custom Exceptions
- **ResourceNotFoundException**: Used when something isn't found (like an item or user)
- **InsufficientStockException**: Used when there's not enough stock for an order

### Global Exception Handler
- **GlobalExceptionHandler**: Catches all exceptions and shows user-friendly error messages

## How It Works

1. When something goes wrong in the application, it throws an exception
2. The GlobalExceptionHandler catches it
3. It shows a nice error message to the user
4. The user gets redirected to an appropriate page

## Error Pages
- **403.html**: Shows when user doesn't have permission (access denied)
- **500.html**: Shows when something unexpected happens (server error)

## Example Usage

```java
// In a service class - when something is not found
if (item == null) {
    throw new ResourceNotFoundException("Item not found with id: " + id);
}

// When there's not enough stock
if (item.getStock() < quantity) {
    throw new InsufficientStockException("Not enough stock available for " + item.getName());
}

// For simple validation errors, just use RuntimeException
if (username == null || username.isEmpty()) {
    throw new RuntimeException("Username cannot be empty");
}
```

## What Changed (Simplified)

- Removed complex exception classes (`DuplicateResourceException`, `InvalidRequestException`)
- Simplified constructors - just pass a simple message string
- Use `RuntimeException` for basic validation errors
- Cleaner, easier-to-understand code for students

The exception handler will automatically catch these and show nice error messages to users.