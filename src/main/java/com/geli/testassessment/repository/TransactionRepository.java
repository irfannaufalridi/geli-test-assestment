package com.geli.testassessment.repository;

import com.geli.testassessment.model.entity.Transaction;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    boolean existsByInvoiceNumber(String invoiceNumber);
    Optional<Transaction> findByInvoiceNumber(String invoiceNumber);
}
