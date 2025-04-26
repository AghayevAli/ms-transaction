package az.kapitalbank.transaction.controller;

import static az.kapitalbank.transaction.common.TestConstants.CUSTOMER_ID;
import static az.kapitalbank.transaction.common.TestConstants.INVALID_TOP_UP_REQUEST;
import static az.kapitalbank.transaction.common.TestConstants.VALID_TOP_UP_REQUEST;
import static az.kapitalbank.transaction.common.TestUtil.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import az.kapitalbank.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void topUpBalance_ValidRequest_ReturnsNoContent() throws Exception {
        willDoNothing().given(transactionService).topUpBalance(any(Long.class), any(TransactionTopUpRequestDto.class));

        mockMvc.perform(post("/api/v1/transactions/top-up")
                        .header("Customer-Id", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(VALID_TOP_UP_REQUEST)))
                .andExpect(status().isNoContent());

        then(transactionService).should().topUpBalance(CUSTOMER_ID, VALID_TOP_UP_REQUEST);
    }

    @Test
    void topUpBalance_MissingCustomerIdHeader_ReturnsBadRequest() throws Exception {

        mockMvc.perform(post("/api/v1/transactions/top-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(VALID_TOP_UP_REQUEST)))
                .andExpect(status().isBadRequest());

        then(transactionService).shouldHaveNoInteractions();
    }

    @Test
    void topUpBalance_InvalidRequest_ReturnsBadRequest() throws Exception {

        mockMvc.perform(post("/api/v1/transactions/top-up")
                        .header("Customer-Id", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(INVALID_TOP_UP_REQUEST)))
                .andExpect(status().isBadRequest());

        then(transactionService).shouldHaveNoInteractions();
    }
}
