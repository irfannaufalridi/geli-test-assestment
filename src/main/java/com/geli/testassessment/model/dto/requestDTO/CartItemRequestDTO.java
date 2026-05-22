package com.geli.testassessment.model.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CartItemRequestDTO {
    private Long itemId; 

    private Long variantId; 

    @NotNull(message = "Harga barang tidak boleh kosong")
    @Min(value = 0, message = "Harga tidak boleh kurang dari 0")
    private BigDecimal price;

    @NotNull(message = "Jumlah barang tidak boleh kosong")
    @Min(value = 1, message = "Minimal pembelian adalah 1 barang")
    private Integer quantity;
}
