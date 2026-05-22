package com.geli.testassessment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.dto.requestDTO.VariantRequestDTO;
import com.geli.testassessment.model.dto.responseDTO.VariantResponseDTO;
import com.geli.testassessment.service.VariantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/variant/")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;

    @PostMapping("/addVariant/{itemId}")
    public ResponseEntity<BaseResponse<Object>> addVariant(@RequestBody List<VariantRequestDTO> newVariant, @PathVariable Long itemId) {
        return variantService.addNewVariant(newVariant, itemId);
    }

    @GetMapping("/getVariant")
    public ResponseEntity<BaseResponse<List<VariantResponseDTO>>> getVariant(@RequestParam Long itemId) {
        return variantService.getVariant(itemId);
    }

    @PutMapping("/updateVariant/{itemId}/{variantId}")
    public ResponseEntity<BaseResponse<Object>> updateVariant(@RequestBody VariantRequestDTO newData, @PathVariable Long itemId, @PathVariable Long variantId) {
        return variantService.updateVariant(newData, itemId, variantId);
    }

    @DeleteMapping("/deleteVariant/{itemId}/{variantId}")
    public ResponseEntity<BaseResponse<Object>> deleteVariant(@PathVariable Long itemId, @PathVariable Long variantId) {
        return variantService.deleteVariant(itemId, variantId);
    }
}
