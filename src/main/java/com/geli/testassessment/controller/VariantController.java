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
import com.geli.testassessment.model.dto.ItemRequestDTO;
import com.geli.testassessment.model.dto.ItemResponseDTO;
import com.geli.testassessment.model.dto.VariantRequestDTO;
import com.geli.testassessment.service.ItemService;
import com.geli.testassessment.service.VariantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/variant/")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;

    @PostMapping("/addVariant")
    public ResponseEntity<BaseResponse<Object>> addVariant(@RequestBody List<VariantRequestDTO> newVariant, @RequestParam Long itemId) {
        return variantService.addNewVariant(newVariant, itemId);
    }
}
