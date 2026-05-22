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
import com.geli.testassessment.model.dto.requestDTO.ItemRequestDTO;
import com.geli.testassessment.model.dto.responseDTO.ItemResponseDTO;
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

    @GetMapping("/getItemByName")
    public ResponseEntity<BaseResponse<ItemResponseDTO>> getItemByName(@RequestParam String itemName) {
        return itemService.getItemByItemName(itemName);
    }

    @GetMapping("/getItemById")
    public ResponseEntity<BaseResponse<ItemResponseDTO>> getItemById(@RequestParam Long itemId) {
        return itemService.getItemByItemId(itemId);
    }

    @PostMapping("/addItem")
    public ResponseEntity<BaseResponse<Object>> addItem(@RequestBody ItemRequestDTO newItem) {
        return itemService.addItem(newItem);
    }

    @PutMapping("/updateItem")
    public ResponseEntity<BaseResponse<Object>> updateItem(@RequestBody ItemRequestDTO updatedItem, @RequestParam Long itemId) {
        return itemService.updateItem(updatedItem, itemId);
    }

    @DeleteMapping("/deleteItem/{itemId}")
    public ResponseEntity<BaseResponse<Object>> deleteItem(@PathVariable Long itemId) {
        return itemService.deleteItem(itemId);
    }
}
