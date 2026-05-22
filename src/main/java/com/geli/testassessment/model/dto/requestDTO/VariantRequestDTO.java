package com.geli.testassessment.model.dto.requestDTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantRequestDTO {
    @NotBlank(message = "Nama variant tidak boleh kosong")
    private String variantName;

    @NotNull(message = "Price tidak boleh kosong")
    private BigDecimal price;
    
    @NotNull(message = "Stock variant tidak boleh kosong")
    private Integer stock;
}
 