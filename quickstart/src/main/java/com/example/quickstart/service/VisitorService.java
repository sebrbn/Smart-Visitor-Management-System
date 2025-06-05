package com.example.quickstart.service;

import com.example.quickstart.model.Visitor;
import com.example.quickstart.repository.VisitorRepository;
import com.example.quickstart.util.EmailService;
import com.example.quickstart.util.OtpUtil;
import com.example.quickstart.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class VisitorService {

    private static final String QR_CODE_DIR = "qrcodes/";

    @Autowired
    private VisitorRepository visitorRepo;

    @Autowired
    private EmailService emailService;

    public Visitor registerVisitor(Visitor visitor) throws Exception {
        // Validate visitor data
        if (visitor == null) {
            throw new IllegalArgumentException("Visitor cannot be null");
        }

        // Set visitor properties
        visitor.setVisitDate(LocalDate.now());
        visitor.setVerified(false);
        String otp = OtpUtil.generateOtp();
        visitor.setOtp(otp);

        // Save visitor
        Visitor saved = visitorRepo.save(visitor);

        try {
            // Send OTP email
            emailService.sendOtp(visitor.getEmail(), otp);

            // Generate and save QR code
            byte[] qrCode = QrCodeGenerator.generateQrCode(
                    saved.getId().toString(),
                    saved.getId().toString()
            );

            // Ensure directory exists
            Path qrCodeDir = Paths.get(QR_CODE_DIR);
            if (!Files.exists(qrCodeDir)) {
                Files.createDirectories(qrCodeDir);
            }

            // Save QR code to file
            Path qrCodePath = qrCodeDir.resolve(saved.getId() + ".png");
            Files.write(qrCodePath, qrCode);

            return saved;

        } catch (Exception e) {
            // Clean up if registration fails
            visitorRepo.delete(saved);
            throw new Exception("Visitor registration failed: " + e.getMessage(), e);
        }
    }

    public Optional<Visitor> verifyOtp(String phone, String otp) {
        if (phone == null || phone.isBlank() || otp == null || otp.isBlank()) {
            throw new IllegalArgumentException("Phone and OTP cannot be null or empty");
        }

        Optional<Visitor> visitor = visitorRepo.findByPhone(phone);
        if (visitor.isPresent() && visitor.get().getOtp().equals(otp)) {
            visitor.get().setVerified(true);
            return Optional.of(visitorRepo.save(visitor.get()));
        }
        return Optional.empty();
    }
}