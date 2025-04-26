package az.kapitalbank.transaction.common;

import az.kapitalbank.transaction.client.customer.model.BalanceChangeRequest;
import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
import az.kapitalbank.transaction.model.enums.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;

public interface TestConstants {

    String BASE_PATH = "/api/v1/customers";
    Long CUSTOMER_ID = 1L;
    UUID TRANSACTION_ID = UUID.fromString("fa213214-92ea-4c54-820c-81dece9bb318");

    TransactionTopUpRequestDto VALID_TOP_UP_REQUEST = TransactionTopUpRequestDto.builder()
            .amount(BigDecimal.valueOf(15))
            .build();

    TransactionTopUpRequestDto INVALID_TOP_UP_REQUEST = TransactionTopUpRequestDto.builder()
            .amount(BigDecimal.valueOf(-15))
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
            .transactionType(TransactionType.TOP_UP)
            .status(TransactionStatus.COMPLETED)
            .build();

    BalanceChangeRequest BALANCE_CHANGE_REQUEST = BalanceChangeRequest.builder()
            .amount(BigDecimal.valueOf(10))
            .build();

    TransactionTopUpRequestDto TRANSACTION_TOP_UP_REQUEST_DTO = TransactionTopUpRequestDto.builder()
            .amount(BigDecimal.valueOf(10))
            .build();

}