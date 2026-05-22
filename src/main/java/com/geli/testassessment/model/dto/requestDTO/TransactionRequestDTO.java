package com.geli.testassessment.model.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransactionRequestDTO {

    @NotEmpty(message = "Keranjang belanja tidak boleh kosong")
    
    @Valid 
    private List<CartItemRequestDTO> cartItems;

}
