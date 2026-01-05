package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.PaymentMethod;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    public List<PaymentMethod> getUserPaymentMethods(User user) {
        return paymentMethodRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public Optional<PaymentMethod> getDefaultPaymentMethod(User user) {
        return paymentMethodRepository.findByUserAndIsDefaultTrue(user);
    }
    
    @Transactional
    public PaymentMethod addPaymentMethod(User user, String cardHolderName, String cardNumber,
                                        String expiryMonth, String expiryYear, String cvv, 
                                        String cardType, boolean setAsDefault) {
        
        // Check if card already exists
        if (paymentMethodRepository.existsByUserAndCardNumber(user, cardNumber)) {
            throw new IllegalArgumentException("This card is already added to your account");
        }
        
        // Validate expiry date
        validateExpiryDate(expiryMonth, expiryYear);
        
        // Determine card type if not provided
        if (cardType == null || cardType.isEmpty()) {
            cardType = determineCardType(cardNumber);
        }
        
        PaymentMethod paymentMethod = new PaymentMethod(user, cardHolderName, cardNumber, 
                                                       expiryMonth, expiryYear, cvv, cardType);
        
        // If this is the first payment method or setAsDefault is true, make it default
        long existingCount = paymentMethodRepository.countByUser(user);
        if (existingCount == 0 || setAsDefault) {
            if (setAsDefault) {
                paymentMethodRepository.clearDefaultPaymentMethods(user);
            }
            paymentMethod.setDefault(true);
        }
        
        return paymentMethodRepository.save(paymentMethod);
    }
    
    @Transactional
    public void removePaymentMethod(User user, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
            .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));
        
        if (!paymentMethod.getUser().getUser_id().equals(user.getUser_id())) {
            throw new IllegalArgumentException("You can only remove your own payment methods");
        }
        
        boolean wasDefault = paymentMethod.isDefault();
        paymentMethodRepository.delete(paymentMethod);
        
        // If the deleted method was default, set another one as default
        if (wasDefault) {
            List<PaymentMethod> remainingMethods = paymentMethodRepository.findByUserOrderByCreatedAtDesc(user);
            if (!remainingMethods.isEmpty()) {
                PaymentMethod newDefault = remainingMethods.get(0);
                newDefault.setDefault(true);
                paymentMethodRepository.save(newDefault);
            }
        }
    }
    
    @Transactional
    public void setDefaultPaymentMethod(User user, Long paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
            .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));
        
        if (!paymentMethod.getUser().getUser_id().equals(user.getUser_id())) {
            throw new IllegalArgumentException("You can only modify your own payment methods");
        }
        
        // Clear all default flags for this user
        paymentMethodRepository.clearDefaultPaymentMethods(user);
        
        // Set this one as default
        paymentMethod.setDefault(true);
        paymentMethod.setUpdatedAt(LocalDateTime.now());
        paymentMethodRepository.save(paymentMethod);
    }
    
    private void validateExpiryDate(String month, String year) {
        try {
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            
            if (monthInt < 1 || monthInt > 12) {
                throw new IllegalArgumentException("Invalid expiry month");
            }
            
            LocalDateTime now = LocalDateTime.now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();
            
            if (yearInt < currentYear || (yearInt == currentYear && monthInt < currentMonth)) {
                throw new IllegalArgumentException("Card has expired");
            }
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid expiry date format");
        }
    }
    
    private String determineCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return "VISA";
        } else if (cardNumber.startsWith("5") || cardNumber.startsWith("2")) {
            return "MASTERCARD";
        } else if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
            return "AMEX";
        } else if (cardNumber.startsWith("6")) {
            return "DISCOVER";
        }
        return "UNKNOWN";
    }
}