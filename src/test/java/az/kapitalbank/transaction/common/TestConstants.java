package az.kapitalbank.transaction.common;

import static az.kapitalbank.transaction.model.enums.TransactionType.PURCHASE;

import az.kapitalbank.transaction.client.customer.model.BalanceChangeRequest;
import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.model.dto.TransactionPurchaseRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionRefundRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionResponseDto;
import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
import az.kapitalbank.transaction.model.enums.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;

public interface TestConstants {

    String BASE_PATH = "/api/v1/transactions";
    String CUSTOMER_ID_HEADER = "Customer-Id";
    Long CUSTOMER_ID = 123L;
    UUID TRANSACTION_ID = UUID.fromString("3a26031b-4a6f-407c-b35e-85aef654ae10");

    TransactionTopUpRequestDto VALID_TOP_UP_REQUEST = TransactionTopUpRequestDto.builder()
            .amount(BigDecimal.valueOf(15))
            .build();

    TransactionTopUpRequestDto INVALID_TOP_UP_REQUEST = TransactionTopUpRequestDto.builder()
            .amount(BigDecimal.valueOf(-15))
            .build();

    TransactionPurchaseRequestDto VALID_PURCHASE_REQUEST = TransactionPurchaseRequestDto.builder()
            .amount(BigDecimal.valueOf(250))
            .build();

    TransactionRefundRequestDto VALID_REFUND_REQUEST = TransactionRefundRequestDto.builder()
            .transactionId(TRANSACTION_ID)
            .amount(BigDecimal.valueOf(50))
            .build();

    TransactionResponseDto TRANSACTION_RESPONSE = TransactionResponseDto.builder()
            .transactionId(TRANSACTION_ID)
            .build();

    TransactionEntity TRANSACTION_ENTITY_STATUS_PENDING = TransactionEntity.builder()
            .transactionId(TRANSACTION_ID)
            .customerId(CUSTOMER_ID)
            .transactionType(TransactionType.TOP_UP)
            .status(TransactionStatus.PENDING)
            .build();

    TransactionEntity TRANSACTION_ENTITY_STATUS_COMPLETED = TransactionEntity.builder()
            .transactionId(TRANSACTION_ID)
            .customerId(CUSTOMER_ID)
            .transactionType(PURCHASE)
            .status(TransactionStatus.COMPLETED)
            .amount(BigDecimal.valueOf(100))
            .build();

    BalanceChangeRequest BALANCE_CHANGE_REQUEST = BalanceChangeRequest.builder()
            .amount(BigDecimal.valueOf(10))
            .build();

    TransactionTopUpRequestDto TRANSACTION_TOP_UP_REQUEST_DTO = TransactionTopUpRequestDto.builder()
            .amount(BigDecimal.valueOf(10))
            .build();

    TransactionEntity TRANSACTION_ENTITY_BALANCE_30 = TransactionEntity.builder()
            .transactionId(UUID.randomUUID())
            .customerId(CUSTOMER_ID)
            .amount(BigDecimal.valueOf(30))
            .transactionType(PURCHASE)
            .status(TransactionStatus.COMPLETED)
            .build();
}