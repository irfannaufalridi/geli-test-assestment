package com.geli.testassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.geli.testassessment.model.entity.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    boolean existsByVariantCode(String variantCode);
}
