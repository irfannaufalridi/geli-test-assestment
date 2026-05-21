package com.geli.testassessment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.geli.testassessment.model.dto.BaseResponse;
import com.geli.testassessment.model.dto.ItemRequestDTO;
import com.geli.testassessment.model.dto.ItemResponseDTO;
import com.geli.testassessment.model.dto.VariantResponseDTO;
import com.geli.testassessment.model.entity.Item;
import com.geli.testassessment.model.entity.Variant;
import com.geli.testassessment.repository.ItemRepository;
import com.geli.testassessment.repository.VariantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;

    public ResponseEntity<BaseResponse<List<ItemResponseDTO>>> getAllItem() {
        try {

            List<Item> items = itemRepository.findAll();
            List<ItemResponseDTO> response = new ArrayList<>();

            for (Item tempItem : items) {
                ItemResponseDTO tempResponse = new ItemResponseDTO();
                
                tempResponse.setId(tempItem.getId());
                tempResponse.setItemName(tempItem.getItemName());
                tempResponse.setDescription(tempItem.getDescription());
                tempResponse.setPrice(tempItem.getPrice());
                tempResponse.setStock(tempItem.getStock());

                List<VariantResponseDTO> responseVariant = new ArrayList<>();
                if (tempItem.getVariants() != null && !tempItem.getVariants().isEmpty()) {
                    tempResponse.setStock(0);
                    for (Variant tempVariant : tempItem.getVariants()) {
                        VariantResponseDTO vDto = new VariantResponseDTO();
                        
                        vDto.setId(tempVariant.getId());
                        vDto.setVariantCode(tempVariant.getVariantCode());
                        vDto.setVariantName(tempVariant.getVariantName());
                        vDto.setPrice(tempVariant.getPrice());
                        vDto.setStock(tempVariant.getStock());
                        
                        tempResponse.setStock(tempResponse.getStock() + tempVariant.getStock());
                        responseVariant.add(vDto);
                    }
                }
                tempResponse.setVariants(responseVariant);

                response.add(tempResponse);
            }

            ResponseEntity<BaseResponse<List<ItemResponseDTO>>> responseEntity = ResponseEntity.ok(new BaseResponse<List<ItemResponseDTO>>(response, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value()));

            return responseEntity;

        } catch (Exception e) {
            System.err.println("Error while adding product: " + e.getMessage());
            throw new RuntimeException("Error while get items", e);
        }
    }

    public ResponseEntity<BaseResponse<Object>> addItem(ItemRequestDTO newData) {
        try {

            Item item = new Item();
            item.setItemName(newData.getItemName());
            item.setPrice(newData.getPrice());
            item.setStock(newData.getStock());
            item.setDescription(newData.getDescription());
            List<Variant> variants = newData.getVariants();

            if (variants != null && !variants.isEmpty()) {
                for (Variant variant : variants) {
                    String generatedCode;
                    boolean isCodeExist;
                    
                    do {
                        String randomString = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                        generatedCode = "VAR-" + randomString;
                        
                        isCodeExist = variantRepository.existsByVariantCode(generatedCode);
                        
                    } while (isCodeExist);

                    variant.setItem(item);
                    variant.setVariantCode(generatedCode);
                }
                item.setVariants(variants);
            }

            itemRepository.save(item);

            ResponseEntity<BaseResponse<Object>> responseEntity = ResponseEntity.ok(new BaseResponse<Object>(null, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value()));

            return responseEntity;

        } catch (Exception e) {
            System.err.println("Error while adding product: " + e.getMessage());
            throw new RuntimeException("Error while adding item", e);
        }
    }
}
