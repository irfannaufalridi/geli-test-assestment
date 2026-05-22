package com.geli.testassessment.model.dto.responseDTO;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TransactionResponseDTO {
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private LocalDateTime transactionDate;
    private List<TransactionDetailResponseDTO> details;
}
