package com.clowder.booking.service.client.fallback;

import com.clowder.booking.service.client.CategoryClient;
import com.clowder.common.dto.shared.CategoryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryClientFallback implements CategoryClient {

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(Long categoryId) {
        log.warn("CategoryClient.getCategoryById circuit open for categoryId={}", categoryId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<CategoryDTO> getCategoriesByIdAndSalonId(Long id, Long salonId) {
        log.warn("CategoryClient.getCategoriesByIdAndSalonId circuit open for id={}, salonId={}", id, salonId);
        return ResponseEntity.ok(null);
    }
}
