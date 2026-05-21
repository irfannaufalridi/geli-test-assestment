package com.geli.testassessment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.dto.ItemRequestDTO;
import com.geli.testassessment.model.dto.ItemResponseDTO;
import com.geli.testassessment.model.entity.Item;
import com.geli.testassessment.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/item/")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/getAllItem")
    public ResponseEntity<BaseResponse<List<ItemResponseDTO>>> getAllItem() {
        return itemService.getAllItem();
    }

    @PostMapping("/addItem")
    public ResponseEntity<BaseResponse<Object>> addItem(@RequestBody ItemRequestDTO newItem) {
        return itemService.addItem(newItem);
    }
}
