package com.github.thetallman67.gemini.v1.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Volume {
    private long timestampms;
    private String priceCurrencySymbol;
    private String priceVolume;
    private String quantityCurrencySymbol;
    private String quantityVolume;
}
