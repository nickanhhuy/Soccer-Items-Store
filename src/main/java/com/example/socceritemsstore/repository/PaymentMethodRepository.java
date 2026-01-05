package com.example.socceritemsstore.repository;

import com.example.socceritemsstore.model.PaymentMethod;
import com.example.socceritemsstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    
    List<PaymentMethod> findByUserOrderByCreatedAtDesc(User user);
    
    Optional<PaymentMethod> findByUserAndIsDefaultTrue(User user);
    
    @Modifying
    @Query("UPDATE PaymentMethod pm SET pm.isDefault = false WHERE pm.user = :user")
    void clearDefaultPaymentMethods(@Param("user") User user);
    
    long countByUser(User user);
    
    boolean existsByUserAndCardNumber(User user, String cardNumber);
}