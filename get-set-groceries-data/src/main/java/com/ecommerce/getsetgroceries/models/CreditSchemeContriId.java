package com.ecommerce.getsetgroceries.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditSchemeContriId implements Serializable {
    private Long creditSchemeId;
    private Long userId;
    private LocalDate date;
}
