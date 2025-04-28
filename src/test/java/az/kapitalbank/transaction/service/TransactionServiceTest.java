package az.kapitalbank.transaction.service;

import static az.kapitalbank.transaction.common.TestConstants.CUSTOMER_ID;
import static az.kapitalbank.transaction.common.TestConstants.TRANSACTION_ENTITY_BALANCE_30;
import static az.kapitalbank.transaction.common.TestConstants.TRANSACTION_ENTITY_STATUS_COMPLETED;
import static az.kapitalbank.transaction.common.TestConstants.TRANSACTION_ENTITY_STATUS_PENDING;
import static az.kapitalbank.transaction.common.TestConstants.TRANSACTION_ID;
import static az.kapitalbank.transaction.common.TestConstants.VALID_PURCHASE_REQUEST;
import static az.kapitalbank.transaction.common.TestConstants.VALID_REFUND_REQUEST;
import static az.kapitalbank.transaction.common.TestConstants.VALID_TOP_UP_REQUEST;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import az.kapitalbank.transaction.client.customer.CustomerClient;
import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.dao.repository.TransactionRepository;
import az.kapitalbank.transaction.exception.InvalidOriginalTransactionException;
import az.kapitalbank.transaction.exception.RefundAmountExceedsOriginalException;
import az.kapitalbank.transaction.exception.TotalRefundExceedsOriginalException;
import az.kapitalbank.transaction.exception.TransactionNotFoundException;
import az.kapitalbank.transaction.mapper.TransactionMapper;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void topUpBalance_Should_ReturnSuccess() {
        // Given
        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_TOP_UP_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);

        // When
        var response = transactionService.topUpBalance(CUSTOMER_ID, VALID_TOP_UP_REQUEST);

        // Then
        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        verify(transactionRepository, times(2)).save(any(TransactionEntity.class));
    }

    @Test
    void purchase_Should_ReturnSuccess() {
        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_PURCHASE_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);

        var response = transactionService.purchase(CUSTOMER_ID, VALID_PURCHASE_REQUEST);

        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        verify(transactionRepository, times(2)).save(any(TransactionEntity.class));
    }

    @Test
    void partialRefund_Should_ReturnSuccess() {
        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_REFUND_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.findByTransactionIdForUpdate(TRANSACTION_ID))
                .willReturn(Optional.of(TRANSACTION_ENTITY_STATUS_COMPLETED));
        given(transactionRepository.findAllByReferencedTransactionIdAndStatus(TRANSACTION_ID,
                TransactionStatus.COMPLETED))
                .willReturn(List.of());

        var response = transactionService.partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST);

        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        verify(transactionRepository, times(2)).save(any(TransactionEntity.class));
    }

    @Test
    void partialRefund_Should_ThrowTransactionNotFoundException_When_OriginalNotFound() {
        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_REFUND_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.findByTransactionIdForUpdate(TRANSACTION_ID))
                .willReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST));
    }

    @Test
    void partialRefund_Should_ThrowInvalidOriginalTransactionException_When_OriginalTransactionInvalid() {
        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_REFUND_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.findByTransactionIdForUpdate(TRANSACTION_ID))
                .willReturn(Optional.of(TRANSACTION_ENTITY_STATUS_PENDING));

        assertThrows(InvalidOriginalTransactionException.class,
                () -> transactionService.partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST));
    }

    @Test
    void partialRefund_Should_ThrowRefundAmountExceedsOriginalException_When_RefundBiggerThanOriginal() {
        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_REFUND_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.findByTransactionIdForUpdate(TRANSACTION_ID))
                .willReturn(Optional.of(TRANSACTION_ENTITY_BALANCE_30));

        assertThrows(RefundAmountExceedsOriginalException.class,
                () -> transactionService.partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST));
    }

    @Test
    void partialRefund_Should_ThrowTotalRefundExceedsOriginalException_When_TotalRefundExceeded() {
        var originalTransaction = TransactionEntity.builder()
                .transactionId(UUID.randomUUID())
                .customerId(CUSTOMER_ID)
                .amount(BigDecimal.valueOf(100))
                .transactionType(az.kapitalbank.transaction.model.enums.TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .build();

        TransactionEntity previousRefund = TransactionEntity.builder()
                .transactionId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(60))
                .build();

        given(transactionMapper.toTransactionEntity(CUSTOMER_ID, VALID_REFUND_REQUEST))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.save(any(TransactionEntity.class)))
                .willReturn(TRANSACTION_ENTITY_STATUS_PENDING);
        given(transactionRepository.findByTransactionIdForUpdate(TRANSACTION_ID))
                .willReturn(Optional.of(originalTransaction));
        given(transactionRepository.findAllByReferencedTransactionIdAndStatus(originalTransaction.getTransactionId(),
                TransactionStatus.COMPLETED))
                .willReturn(List.of(previousRefund));

        assertThrows(TotalRefundExceedsOriginalException.class,
                () -> transactionService.partialRefund(CUSTOMER_ID, VALID_REFUND_REQUEST));
    }
}