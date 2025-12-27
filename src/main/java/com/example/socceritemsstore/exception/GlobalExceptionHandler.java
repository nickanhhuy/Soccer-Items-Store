package com.example.socceritemsstore.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Simple global exception handler for the Soccer Items Store to handle common errors.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    // Handle when something is not found (like item or user)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/menu";
    }
    
    // Handle when there's not enough stock
    @ExceptionHandler(InsufficientStockException.class)
    public String handleInsufficientStock(InsufficientStockException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/order";
    }
    
    // Handle all other unexpected errors
    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception ex, Model model) {
        model.addAttribute("error", "Something went wrong: " + ex.getMessage());
        return "error/500";
    }
}
