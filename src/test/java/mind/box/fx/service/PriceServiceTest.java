package mind.box.fx.service;

import mind.box.fx.exception.PriceNotFoundException;
import mind.box.fx.model.PriceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceServiceTest {

    private PriceService priceService;
    private List<String> messages;

    @BeforeEach
    void setUp(){
        priceService = new PriceService();
        messages = new ArrayList<>();
        messages.add("106, EUR/USD, 1.1000, 1.2000, 01-06-2020 12:01:01:001");
        messages.add("107, EUR/JPY, 119.60, 119.90, 01-06-2020 12:01:02:002");
        messages.add("108, GBP/USD, 1.2500, 1.2560, 01-06-2020 12:01:02:002");
        messages.add("109, GBP/USD, 1.2499, 1.2561, 01-06-2020 12:01:02:100");
        messages.add("110, EUR/JPY, 119.61, 119.91, 01-06-2020 12:01:02:110");
    }

    @Test
    void Given_Message_When_CallOnMessage_Then_MapSizeIsGreaterThanZero(){
        String message = messages.get(0);
        priceService.onMessage(message);
        Map<Long, PriceDTO> map = priceService.getPriceDTOMap();
        assertThat(map.size()).isGreaterThan(0);
    }

    @Test
    void Given_Message_When_CallOnMessage_Then_PopulateDataMapWithKeyValuePriceDTOAndAssertReturnedData(){
        String message = messages.get(0);
        priceService.onMessage(message);
        Map<Long, PriceDTO> map = priceService.getPriceDTOMap();
        PriceDTO priceDTO = map.get(106L);
        assertThat(priceDTO.getId()).isEqualTo(106);
        assertThat(priceDTO.getName()).isEqualTo("EUR/USD");
        assertThat(priceDTO.getBid()).isEqualTo(new BigDecimal("1.0989")); //1.1000 - 0.0011 = 1,0989
        assertThat(priceDTO.getAsk()).isEqualTo(new BigDecimal("1.2012")); //1.2000 + 0.0012 = 1,2012
        assertThat(priceDTO.getTimestamp().toString()).isEqualTo("2020-06-01 12:01:01.001");
    }

    private static Stream<Arguments> provideMessagesAndResults(){
        return Stream.of(
          Arguments.of(
                  "106, EUR/USD, 1.1000, 1.2000, 01-06-2020 12:01:01:001",
                  new BigDecimal("1.0989"),
                  new BigDecimal("1.2012")),
          Arguments.of(
                  "107, EUR/JPY, 119.60, 119.90, 01-06-2020 12:01:02:002",
                  new BigDecimal("119.48"),
                  new BigDecimal("120.02")),
          Arguments.of(
                  "108, GBP/USD, 1.2500, 1.2560, 01-06-2020 12:01:02:002",
                  new BigDecimal("1.2488"),
                  new BigDecimal("1.2573"))

        );
    }

    @ParameterizedTest
    @MethodSource("provideMessagesAndResults")
    void Given_Messages_When_CallOnMessage_Then_ReturnProperValuesBidAndAskBasedOnCommission(String message,
                                                                                                      BigDecimal expectedBid,
                                                                                                      BigDecimal expectedAsk){
        priceService.onMessage(message);
        Map<Long, PriceDTO> map = priceService.getPriceDTOMap();
        for(Long e : map.keySet()){
            final PriceDTO priceDTO = map.get(e);
            assertThat(priceDTO.getBid()).isEqualTo(expectedBid);
            assertThat(priceDTO.getAsk()).isEqualTo(expectedAsk);
        }
    }

    @Test
    void Given_Messages_When_CallGetLastPrice_Then_ReturnLastPrice(){
        for(String m : messages) priceService.onMessage(m);
        PriceDTO priceDTO = priceService.getLastPrice();
        assertThat(priceDTO.getId()).isEqualTo(110);
        assertThat(priceDTO.getBid()).isEqualTo(new BigDecimal("119.49"));
        assertThat(priceDTO.getAsk()).isEqualTo(new BigDecimal("120.03"));
    }

    @Test
    void Given_EmptyMap_When_CallGetLastPrice_Then_ThrowNotFoundException(){
        assertThatThrownBy(() -> priceService.getLastPrice())
                .isExactlyInstanceOf(PriceNotFoundException.class);
    }
}