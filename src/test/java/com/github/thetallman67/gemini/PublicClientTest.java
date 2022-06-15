package com.github.thetallman67.gemini;

import com.github.thetallman67.gemini.v1.client.PublicClient;
import com.github.thetallman67.gemini.v1.model.SymbolStatus;
import com.github.thetallman67.gemini.v1.request.SymbolRequest;
import com.github.thetallman67.gemini.v1.response.SymbolDetails;
import com.github.thetallman67.gemini.v1.response.Ticker;
import kong.unirest.UnirestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PublicClientTest {

    final Set<String> QUOTE_CURRENCIES = new HashSet<>(Arrays.asList("btc", "eth", "usd", "eur", "gbp", "dai", "ltc", "bch", "sgd"));

    private static PublicClient publicClient;

    final String BASE_CURRENCY = "BTC";
    final String QUOTE_CURRENCY = "USD";
    final String SYMBOL = BASE_CURRENCY + QUOTE_CURRENCY;

    @BeforeAll
    public static void setup() {
        publicClient = Gemini.publicClient()
                                .useSandbox()
                                .build();
    }

    @Test
    public void testGetSymbols() {
        Set<String> symbols = publicClient.getSymbols();
        assertNotNull(symbols);
        final int EXPECTED_SYMBOL_COUNT = publicClient.isSandbox() ? 119 : 131;
        assertTrue(symbols.size() >= EXPECTED_SYMBOL_COUNT, "Expected " + EXPECTED_SYMBOL_COUNT + " symbols but found " + symbols.size() + "");
        assertTrue(symbols.contains("btcusd"));
        symbols.forEach(s -> {
            assertTrue(QUOTE_CURRENCIES.stream().anyMatch(s::endsWith));
        });
    }

    @Test
    public void testGetSymbolDetails() {
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

    @Test
    public void testGetTickerBadSymbol() {
        final String BAD_SYMBOL = "BTCETH";
        UnirestException e = assertThrows(UnirestException.class, () -> {
            Ticker ticker = publicClient.getTicker(
                    SymbolRequest.builder().withSymbol(BAD_SYMBOL).build()
            );
        }, "UnirestException was expected");

        assertEquals("kong.unirest.UnirestException: Supplied value '" + BAD_SYMBOL + "' is not a valid symbol", e.getMessage());
    }

    @Test
    public void testGetTicker() {
        Ticker ticker = publicClient.getTicker(
                SymbolRequest.builder().withSymbol(SYMBOL).build()
        );
        assertNotNull(ticker);
        assertTrue(ticker.getBid() > 0);
        assertTrue(ticker.getAsk() > 0);
        assertTrue(ticker.getAsk() >= ticker.getBid());
        assertNotNull(ticker.getVolume());
        assertEquals(BASE_CURRENCY, ticker.getVolume().getPriceCurrencySymbol());
        assertNotNull(ticker.getVolume().getPriceVolume());
        assertEquals(QUOTE_CURRENCY, ticker.getVolume().getQuantityCurrencySymbol());
        assertNotNull(ticker.getVolume().getQuantityVolume());
        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
        assertTrue(fiveMinutesAgo.toEpochMilli() <=  ticker.getVolume().getTimestampms());
    }
}
