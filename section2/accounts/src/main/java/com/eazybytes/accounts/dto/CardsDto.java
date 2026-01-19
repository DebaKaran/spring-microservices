package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name = "Cards",
        description = "Schema to hold Card information"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardsDto {

    private String mobileNumber;
    private String cardNumber;
    private String cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
}

