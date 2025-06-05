package com.example.quickstart.controller;

import com.example.quickstart.model.Visitor;
import com.example.quickstart.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerVisior(@RequestBody Visitor visitor){
        try {
            return ResponseEntity.ok(visitorService.registerVisitor(visitor));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }

    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String phone, @RequestParam String otp) {
        Optional<Visitor> verified = visitorService.verifyOtp(phone, otp);
        return verified.isPresent() ? ResponseEntity.ok("Verified") : ResponseEntity.status(401).body("Invalid OTP");
    }

}
