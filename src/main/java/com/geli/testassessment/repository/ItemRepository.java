package com.geli.testassessment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geli.testassessment.model.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByItemNameIgnoreCase(String itemName);
}
