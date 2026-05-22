package com.geli.testassessment.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponseDTO {
    private Long id;
    private String itemName; 
    private Integer quantity;
    private BigDecimal sellingPrice;
    private BigDecimal subTotal;
    
}