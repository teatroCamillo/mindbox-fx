package mind.box.fx.controller;

import mind.box.fx.exception.PriceNotFoundException;
import mind.box.fx.model.PriceDTO;
import mind.box.fx.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static mind.box.fx.service.PriceService.convertStringToTimestamp;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = PriceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    private PriceDTO priceDTO;
    private final String PRICE_NOT_FOUND = "Price not found";

    @BeforeEach
    void setUp(){
        priceDTO = PriceDTO.builder()
                .id(106L)
                .name("EUR/USD")
                .bid(new BigDecimal("1.0989"))
                .ask(new BigDecimal("1.2012"))
                .timestamp(convertStringToTimestamp("01-06-2020 12:01:01:001"))
                .build();
    }

    @Test
    void Given_EndpointGetPrice_When_IsCalled_Then_ReturnHTTPStatusOK() throws Exception {
        given(priceService.getLastPrice()).willReturn(priceDTO);
        mockMvc
                .perform(get("/getLastPrice"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void Given_EndpointGetPrice_When_IsCalled_Then_ReturnNotFound() throws Exception {
        given(priceService.getLastPrice()).willThrow(new PriceNotFoundException(PRICE_NOT_FOUND));
        mockMvc
                .perform(get("/getLastPrice"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}