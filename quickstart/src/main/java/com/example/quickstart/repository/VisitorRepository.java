package com.example.quickstart.repository;

import com.example.quickstart.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findByPhone(String phone);
    long countByVerified(boolean Verified);
    long countByVisitDate(LocalDate visitDate);
}
