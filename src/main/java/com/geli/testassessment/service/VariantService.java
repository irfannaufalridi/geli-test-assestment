package com.geli.testassessment.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.entity.Item;
import com.geli.testassessment.model.entity.Variant;
import com.geli.testassessment.repository.ItemRepository;
import com.geli.testassessment.repository.VariantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariantService {
    
    private final VariantRepository variantRepository;
    private final ItemRepository itemRepository;

    public ResponseEntity<BaseResponse<Object>> addVariant(List<Variant> newData, Long itemId) {
        try {

            Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            existingItem.getVariants().addAll(newData);
            itemRepository.save(existingItem);

            ResponseEntity<BaseResponse<Object>> responseEntity = ResponseEntity.ok(new BaseResponse<Object>(null, "Success", 200));

            return responseEntity;

        } catch (Exception e) {
            System.err.println("Error while adding product: " + e.getMessage());
            throw new RuntimeException("Error while adding product", e);
        }
    }
}
