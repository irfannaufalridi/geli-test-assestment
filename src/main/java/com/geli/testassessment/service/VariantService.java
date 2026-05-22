package com.geli.testassessment.service;

import java.util.List;
import java.util.UUID;

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

    public ResponseEntity<BaseResponse<Object>> addNewVariant(List<Variant> newData, Long itemId) {
        try {
            Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            if (newData != null && !newData.isEmpty()) {
                for (Variant newVariant : newData) {
                    newVariant.setItem(existingItem);

                    String generatedCode;
                    boolean isCodeExist;
                    do {
                        String randomString = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                        generatedCode = "VAR-" + randomString;
                        
                        isCodeExist = variantRepository.existsByVariantCode(generatedCode);
                    } while (isCodeExist);

                    newVariant.setVariantCode(generatedCode);

                    existingItem.getVariants().add(newVariant);
                }
            }

            variantRepository.saveAll(newData);

            return ResponseEntity.ok(new BaseResponse<>(null, "Success", HttpStatus.OK.value()));

        } catch (Exception e) {
            System.err.println("Error while adding new variants: " + e.getMessage());
            throw new RuntimeException("Error while adding new variants", e);
        }
    }
}
