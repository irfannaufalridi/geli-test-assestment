package com.geli.testassessment.model.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantResponseDTO {
    private Long id;
    private String variantName;
    private String variantCode;
    private BigDecimal price;
    private Integer stock;
}
 