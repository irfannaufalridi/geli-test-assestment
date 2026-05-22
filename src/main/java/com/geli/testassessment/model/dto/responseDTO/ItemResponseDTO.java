package com.geli.testassessment.model.dto.responseDTO;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO {
    private Long id;
    private String itemName;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private List<VariantResponseDTO> variants;
}
 