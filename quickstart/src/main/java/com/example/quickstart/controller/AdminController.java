package com.example.quickstart.controller;

import com.example.quickstart.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private VisitorRepository visitorRepository;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisitors", visitorRepository.count());
        stats.put("verifiedVisitors", visitorRepository.countByVerified(true));
        stats.put("todayVisitors", visitorRepository.countByVisitDate(LocalDate.now()));
        return stats;
    }

}
