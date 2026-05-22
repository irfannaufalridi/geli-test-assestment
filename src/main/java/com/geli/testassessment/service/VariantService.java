package com.geli.testassessment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.dto.VariantRequestDTO;
import com.geli.testassessment.model.dto.VariantResponseDTO;
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

    public ResponseEntity<BaseResponse<List<VariantResponseDTO>>> getVariant(Long itemId) {
        try {
            Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            List<VariantResponseDTO> response = existingItem.getVariants().stream().map(variant -> {
                VariantResponseDTO dto = new VariantResponseDTO();
                dto.setId(variant.getId());
                dto.setVariantName(variant.getVariantName());
                dto.setVariantCode(variant.getVariantCode());
                dto.setPrice(variant.getPrice());
                dto.setStock(variant.getStock());
                return dto;
            }).toList();

            return ResponseEntity.ok(new BaseResponse<>(response, "Success", HttpStatus.OK.value()));

        } catch (IllegalArgumentException e) {
            System.err.println("Bad Request: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error while adding new variants: " + e.getMessage());
            throw new RuntimeException("Error while adding new variants", e);
        }
    }

    public ResponseEntity<BaseResponse<Object>> addNewVariant(List<VariantRequestDTO> newData, Long itemId) {
        try {
            Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            if (newData == null || newData.isEmpty()) {
                throw new IllegalArgumentException("Data varian baru tidak boleh kosong"); 
            }

            List<Variant> variantsToSave = newData.stream().map(dto -> {
                Variant variant = new Variant();
                String generatedCode;
                boolean isCodeExist;

                variant.setVariantName(dto.getVariantName());
                variant.setPrice(dto.getPrice());
                variant.setStock(dto.getStock());
                variant.setItem(existingItem);

                do {
                    String randomString = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                    generatedCode = "VAR-" + randomString;
                    
                    isCodeExist = variantRepository.existsByVariantCode(generatedCode);
                } while (isCodeExist);

                variant.setVariantCode(generatedCode);
                return variant;
            }).toList();

            variantRepository.saveAll(variantsToSave);

            return ResponseEntity.ok(new BaseResponse<>(null, "Success", HttpStatus.OK.value()));

        } catch (IllegalArgumentException e) {
            System.err.println("Bad Request: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error while adding new variants: " + e.getMessage());
            throw new RuntimeException("Error while adding new variants", e);
        }
    }
}
