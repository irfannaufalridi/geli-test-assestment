package com.geli.testassessment.controller;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.dto.requestDTO.TransactionRequestDTO;
import com.geli.testassessment.model.dto.responseDTO.TransactionResponseDTO;
import com.geli.testassessment.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/getAllTransactions")
    public ResponseEntity<BaseResponse<List<TransactionResponseDTO>>> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/getTransactionDetail/{invoiceNumber}")
    public ResponseEntity<BaseResponse<TransactionResponseDTO>> getTransactionDetail(@PathVariable String invoiceNumber) {
        return transactionService.getTransactionDetail(invoiceNumber);
    }

    @PostMapping("/makeTransaction")
    public ResponseEntity<BaseResponse<Object>> createTransaction(@Valid @RequestBody TransactionRequestDTO request) {
        return transactionService.createTransaction(request);
    }
}
