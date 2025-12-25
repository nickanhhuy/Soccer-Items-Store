package com.example.socceritemsstore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceCalculator {
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08"); // 8% tax
    private static final BigDecimal SHIPPING_THRESHOLD = new BigDecimal("50.00");
    private static final BigDecimal SHIPPING_COST = new BigDecimal("5.99");
    
    public static double calculateSubtotal(double price, int quantity) {
        BigDecimal priceDecimal = BigDecimal.valueOf(price);
        BigDecimal quantityDecimal = BigDecimal.valueOf(quantity);
        return priceDecimal.multiply(quantityDecimal)
                          .setScale(2, RoundingMode.HALF_UP)
                          .doubleValue();
    }
    
    public static double calculateTax(double subtotal) {
        BigDecimal subtotalDecimal = BigDecimal.valueOf(subtotal);
        return subtotalDecimal.multiply(TAX_RATE)
                             .setScale(2, RoundingMode.HALF_UP)
                             .doubleValue();
    }
    
    public static double calculateShipping(double subtotal) {
        BigDecimal subtotalDecimal = BigDecimal.valueOf(subtotal);
        if (subtotalDecimal.compareTo(SHIPPING_THRESHOLD) >= 0) {
            return 0.00; // Free shipping
        }
        return SHIPPING_COST.doubleValue();
    }
    
    public static double calculateTotal(double subtotal, double tax, double shipping) {
        BigDecimal subtotalDecimal = BigDecimal.valueOf(subtotal);
        BigDecimal taxDecimal = BigDecimal.valueOf(tax);
        BigDecimal shippingDecimal = BigDecimal.valueOf(shipping);
        
        return subtotalDecimal.add(taxDecimal)
                             .add(shippingDecimal)
                             .setScale(2, RoundingMode.HALF_UP)
                             .doubleValue();
    }
    
    public static double applyDiscount(double price, double discountPercent) {
        if (discountPercent <= 0 || discountPercent > 100) {
            return price;
        }
        
        BigDecimal priceDecimal = BigDecimal.valueOf(price);
        BigDecimal discountDecimal = BigDecimal.valueOf(discountPercent).divide(BigDecimal.valueOf(100));
        BigDecimal discount = priceDecimal.multiply(discountDecimal);
        
        return priceDecimal.subtract(discount)
                          .setScale(2, RoundingMode.HALF_UP)
                          .doubleValue();
    }
    
    public static String formatPrice(double price) {
        return String.format("$%.2f", price);
    }
    
    public static double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
    }
}
