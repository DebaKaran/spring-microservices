package com.eazybytes.accounts.services.client.fallbacks;

import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.services.client.CardsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardsFallbackFactory implements FallbackFactory<CardsFeignClient> {

    @Override
    public CardsFeignClient create(Throwable cause) {
        return (correlationId, mobileNumber) -> {
            log.warn("Cards fallback triggered, correlationId={}, mobileNumber={}, reason={}",
                    correlationId, mobileNumber, cause.toString());
            CardsDto dto = new CardsDto();
            dto.setMobileNumber(mobileNumber);
            // IMPORTANT:
            // We intentionally do NOT modify LoansDto to indicate degradation.
            // Reason:
            // 1. LoansDto is a business contract owned by Loans MS
            // 2. "Service down" is a technical concern, not business data
            // 3. Customer may legitimately have no loans
            // Therefore, degradation must NOT be inferred from DTO fields.

            return ResponseEntity.ok()
                    // WHY HEADER:
                    // - Headers carry technical metadata, not business semantics
                    // - This explicitly signals a fallback without altering the API payload
                    // - Avoids polluting LoansDto with non-domain fields (e.g. degraded, message)
                    // - Cleanly distinguishes:
                    //     a) "Customer has no loans"
                    //     b) "Loans service is unavailable"
                    // - Safe for future domain changes
                    .header("X-SERVICE-DEGRADED", "CARDS")
                    .body(dto);
        };
    }
}
