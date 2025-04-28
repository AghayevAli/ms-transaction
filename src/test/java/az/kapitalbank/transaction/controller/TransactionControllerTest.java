package az.kapitalbank.transaction.controller;

import static az.kapitalbank.transaction.common.TestConstants.BASE_PATH;
import static az.kapitalbank.transaction.common.TestConstants.CUSTOMER_ID;
import static az.kapitalbank.transaction.common.TestConstants.CUSTOMER_ID_HEADER;
import static az.kapitalbank.transaction.common.TestConstants.TRANSACTION_RESPONSE;
import static az.kapitalbank.transaction.common.TestConstants.VALID_PURCHASE_REQUEST;
import static az.kapitalbank.transaction.common.TestConstants.VALID_REFUND_REQUEST;
import static az.kapitalbank.transaction.common.TestConstants.VALID_TOP_UP_REQUEST;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import az.kapitalbank.transaction.common.TestUtil;
import az.kapitalbank.transaction.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCustomer_Should_ReturnSuccess() throws Exception {
        given(transactionService.topUpBalance(CUSTOMER_ID, VALID_TOP_UP_REQUEST)).willReturn(TRANSACTION_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/top-up")
                        .header(CUSTOMER_ID_HEADER, CUSTOMER_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(VALID_TOP_UP_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtil.json(TRANSACTION_RESPONSE)));

        then(transactionService).should().topUpBalance(CUSTOMER_ID, VALID_TOP_UP_REQUEST);
    }

    @Test
    void purchase_Should_ReturnSuccess() throws Exception {
        given(transactionService.purchase(CUSTOMER_ID, VALID_PURCHASE_REQUEST)).willReturn(TRANSACTION_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/purchase")
                        .header(CUSTOMER_ID_HEADER, CUSTOMER_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(VALID_PURCHASE_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtil.json(TRANSACTION_RESPONSE)));

        then(transactionService).should().purchase(CUSTOMER_ID, VALID_PURCHASE_REQUEST);
    }

    @Test
    void refund_Should_ReturnSuccess() throws Exception {
        given(transactionService.partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST)).willReturn(TRANSACTION_RESPONSE);

        mockMvc.perform(post(BASE_PATH + "/refund")
                        .header(CUSTOMER_ID_HEADER, CUSTOMER_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(VALID_REFUND_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtil.json(TRANSACTION_RESPONSE)));

        then(transactionService).should().partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST);
    }
}