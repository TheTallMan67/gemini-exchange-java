package com.github.thetallman67.gemini;

import com.github.thetallman67.gemini.v1.client.PublicClient;
import com.github.thetallman67.gemini.v1.model.SymbolStatus;
import com.github.thetallman67.gemini.v1.request.SymbolRequest;
import com.github.thetallman67.gemini.v1.response.SymbolDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PublicClientTest {

    private static PublicClient publicClient;

    @BeforeAll
    public static void setup() {
        publicClient = Gemini.publicClient()
                                .useSandbox()
                                .build();
    }

    @Test
    public void testGetSymbolDetails() {
        final String BASE_CURRENCY = "BTC";
        final String QUOTE_CURRENCY = "USD";
        final String SYMBOL = BASE_CURRENCY + QUOTE_CURRENCY;

        SymbolDetails symbolDetails = publicClient.getSymbolDetails(
                SymbolRequest.builder().withSymbol(SYMBOL).build()
        );
        assertNotNull(symbolDetails);
        assertEquals(SYMBOL, symbolDetails.getSymbol());
        assertEquals(BASE_CURRENCY, symbolDetails.getBaseCurrency());
        assertEquals(QUOTE_CURRENCY, symbolDetails.getQuoteCurrency());
        assertEquals(.00000001, symbolDetails.getTickSize());
        assertEquals(0.01, symbolDetails.getQuoteIncrement());
        assertEquals("0.00001", symbolDetails.getMinOrderSize());
        assertEquals(SymbolStatus.OPEN, symbolDetails.getStatus());
        assertFalse(symbolDetails.isWrapEnabled());
    }
}
