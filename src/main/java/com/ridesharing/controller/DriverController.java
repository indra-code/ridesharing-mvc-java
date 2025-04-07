package com.ridesharing.controller;

import com.ridesharing.model.Driver;
import com.ridesharing.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "driver-login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam Long driverId, 
                            HttpSession session,
                            Model model) {
        try {
            Driver driver = driverService.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
            
            // Store driver ID in session
            session.setAttribute("driverId", driver.getId());
            
            // Set driver as available
            driverService.setDriverAvailability(driver.getId(), true);
            
            return "redirect:/ride/available";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid driver ID: " + e.getMessage());
            return "driver-login";
        }
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session) {
        Long driverId = (Long) session.getAttribute("driverId");
        if (driverId != null) {
            // Set driver as unavailable
            driverService.setDriverAvailability(driverId, false);
        }
        session.invalidate();
        return "redirect:/driver/login";
    }
} 