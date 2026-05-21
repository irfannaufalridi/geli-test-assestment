package com.geli.testassessment.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.geli.testassessment.model.entity.Variant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequestDTO {
    @NotBlank(message = "Nama item tidak boleh kosong")
    private String itemName;
    
    private String description;
    
    @NotNull(message = "Price tidak boleh kosong")
    private BigDecimal price;
    
    @NotNull(message = "Stock item tidak boleh kosong")
    private Integer stock;

    private List<Variant> variants;
}
