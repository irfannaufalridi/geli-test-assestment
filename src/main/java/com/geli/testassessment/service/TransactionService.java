package com.geli.testassessment.service;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.dto.requestDTO.CartItemRequestDTO;
import com.geli.testassessment.model.dto.requestDTO.TransactionRequestDTO;
import com.geli.testassessment.model.dto.responseDTO.TransactionDetailResponseDTO;
import com.geli.testassessment.model.dto.responseDTO.TransactionResponseDTO;
import com.geli.testassessment.model.entity.*;
import com.geli.testassessment.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;

    public ResponseEntity<BaseResponse<List<TransactionResponseDTO>>> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            List<TransactionResponseDTO> responseList = new ArrayList<>();

            for (Transaction t : transactions) {
                TransactionResponseDTO tDto = new TransactionResponseDTO();
                tDto.setInvoiceNumber(t.getInvoiceNumber());
                tDto.setTotalAmount(t.getTotalAmount());
                tDto.setTransactionDate(t.getTransactionDate());

                List<TransactionDetailResponseDTO> detailDtos = new ArrayList<>();
                for (TransactionDetail detail : t.getTransactionDetails()) {
                    TransactionDetailResponseDTO dDto = new TransactionDetailResponseDTO();
                    dDto.setId(detail.getId());
                    dDto.setQuantity(detail.getQuantity());
                    dDto.setSellingPrice(detail.getSellingPrice());
                    dDto.setSubTotal(detail.getSubTotal());
                    
                    if (detail.getVariant() != null) {
                        dDto.setItemName(detail.getItem().getItemName() + " - " + detail.getVariant().getVariantName());
                    } else if (detail.getItem() != null) {
                        dDto.setItemName(detail.getItem().getItemName());
                    }

                    detailDtos.add(dDto);
                }
                
                tDto.setDetails(detailDtos);
                responseList.add(tDto);
            }
            return ResponseEntity.ok(new BaseResponse<>(responseList, "Transactions retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(null, "Error occurred while fetching transactions", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    public ResponseEntity<BaseResponse<TransactionResponseDTO>> getTransactionDetail(String invoiceNumber) {
        try {
            Optional<Transaction> transactions = transactionRepository.findByInvoiceNumber(invoiceNumber);
            TransactionResponseDTO responseList = new TransactionResponseDTO();

            if (transactions.isPresent()) {
                Transaction t = transactions.get();
                responseList.setInvoiceNumber(t.getInvoiceNumber());
                responseList.setTotalAmount(t.getTotalAmount());
                responseList.setTransactionDate(t.getTransactionDate());

                List<TransactionDetailResponseDTO> detailDtos = new ArrayList<>();
                for (TransactionDetail detail : t.getTransactionDetails()) {
                    TransactionDetailResponseDTO dDto = new TransactionDetailResponseDTO();
                    dDto.setId(detail.getId());
                    dDto.setQuantity(detail.getQuantity());
                    dDto.setSellingPrice(detail.getSellingPrice());
                    dDto.setSubTotal(detail.getSubTotal());

                    if (detail.getVariant() != null) {
                        dDto.setItemName(detail.getItem().getItemName() + " - " + detail.getVariant().getVariantName());
                    } else if (detail.getItem() != null) {
                        dDto.setItemName(detail.getItem().getItemName());
                    }

                    detailDtos.add(dDto);
                }

                responseList.setDetails(detailDtos);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>(null, "Transaction not found", HttpStatus.NOT_FOUND.value()));
            }

            return ResponseEntity.ok(new BaseResponse<>(responseList, "Transactions retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(null, "Error occurred while fetching transactions", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse<Object>> createTransaction(TransactionRequestDTO request) {
        try {
            Transaction transaction = new Transaction();
        
            String invoiceNumber;
            boolean isInvoiceExist;

            do {
                String randomStr = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
                invoiceNumber = "INV-" + System.currentTimeMillis() + "-" + randomStr; 
                isInvoiceExist = transactionRepository.existsByInvoiceNumber(invoiceNumber);
            } while (isInvoiceExist);
            
            transaction.setInvoiceNumber(invoiceNumber);
            
            BigDecimal grandTotal = BigDecimal.ZERO;
            List<TransactionDetail> details = new ArrayList<>();

            for (CartItemRequestDTO cartItem : request.getCartItems()) {
                TransactionDetail detail = new TransactionDetail();
                detail.setTransaction(transaction);
                detail.setQuantity(cartItem.getQuantity());
                detail.setSellingPrice(cartItem.getPrice());
                
                BigDecimal subTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                detail.setSubTotal(subTotal);
                grandTotal = grandTotal.add(subTotal);

                if (cartItem.getVariantId() != null) {
                    Variant variant = variantRepository.findById(cartItem.getVariantId())
                            .orElseThrow(() -> new RuntimeException("Variant tidak ditemukan"));

                    if (cartItem.getQuantity() > variant.getStock()) {
                        return ResponseEntity.ok(new BaseResponse<>(null, "Stok habis untuk varian: " + variant.getVariantName(), HttpStatus.OK.value()));
                    }


                    variant.setStock(variant.getStock() - cartItem.getQuantity());
                    variantRepository.save(variant);

                    detail.setVariant(variant);
                    detail.setItem(variant.getItem());

                } else if (cartItem.getItemId() != null) {
                    Item item = itemRepository.findById(cartItem.getItemId())
                            .orElseThrow(() -> new RuntimeException("Item tidak ditemukan"));

                    if (cartItem.getQuantity() > item.getStock()) {
                        return ResponseEntity.ok(new BaseResponse<>(null, "Stok habis untuk item: " + item.getItemName(), HttpStatus.OK.value()));
                    }

                    item.setStock(item.getStock() - cartItem.getQuantity());
                    itemRepository.save(item);

                    detail.setItem(item);
                    detail.setVariant(null);

                } else {
                    throw new RuntimeException("Request tidak valid: Harus menyertakan itemId atau variantId");
                }

                details.add(detail);
            }

            transaction.setTotalAmount(grandTotal);
            transaction.setTransactionDetails(details);

            transactionRepository.save(transaction);

            return ResponseEntity.ok(new BaseResponse<>(null, "Transaction Success", HttpStatus.OK.value()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new BaseResponse<>(null, "Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            return ResponseEntity.ok(new BaseResponse<>(null, "Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}