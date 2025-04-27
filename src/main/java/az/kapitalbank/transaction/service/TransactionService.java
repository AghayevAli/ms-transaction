package az.kapitalbank.transaction.service;

import static az.kapitalbank.transaction.exception.constant.ErrorMessage.INVALID_ORIGINAL_TRANSACTION_MESSAGE;
import static az.kapitalbank.transaction.exception.constant.ErrorMessage.REFUND_AMOUNT_EXCEEDS_ORIGINAL_MESSAGE;
import static az.kapitalbank.transaction.exception.constant.ErrorMessage.TOTAL_REFUND_EXCEEDS_ORIGINAL_MESSAGE;
import static az.kapitalbank.transaction.model.enums.TransactionStatus.COMPLETED;

import az.kapitalbank.transaction.client.customer.CustomerClient;
import az.kapitalbank.transaction.client.customer.model.BalanceChangeRequest;
import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.dao.repository.TransactionRepository;
import az.kapitalbank.transaction.exception.InvalidOriginalTransactionException;
import az.kapitalbank.transaction.exception.RefundAmountExceedsOriginalException;
import az.kapitalbank.transaction.exception.TotalRefundExceedsOriginalException;
import az.kapitalbank.transaction.mapper.TransactionMapper;
import az.kapitalbank.transaction.model.dto.TransactionPurchaseRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionRefundRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionResponseDto;
import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
import az.kapitalbank.transaction.model.enums.TransactionType;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    final TransactionRepository transactionRepository;
    final CustomerClient customerClient;

    /**
     * Synchronous implementation for simplicity.
     * In production, asynchronous processing is recommended to:
     * - Improve scalability and responsiveness,
     * - Avoid blocking threads,
     * - Enable better failure handling and retries.
     */

    public TransactionResponseDto topUpBalance(Long customerId, TransactionTopUpRequestDto request) {
        log.info("Starting top-up for customerId={}, amount={}", customerId, request.getAmount());

        TransactionEntity transactionEntity =
                TransactionMapper.INSTANCE.toTransactionEntity(customerId, request);
        transactionRepository.save(transactionEntity);
        log.debug("TransactionEntity saved: transactionId={}, customerId={}, amount={}",
                transactionEntity.getTransactionId(), customerId, request.getAmount());

        try {
            performBalanceTopUp(customerId, request.getAmount());
            updateTransactionStatus(transactionEntity, COMPLETED);
            log.info("Top-up successful for transactionId={}", transactionEntity.getTransactionId());

            return TransactionResponseDto.builder().transactionId(transactionEntity.getTransactionId()).build();
        } catch (Exception e) {
            log.error("Failed to increase balance for customerId={} with transactionId={}",
                    customerId, transactionEntity.getTransactionId(), e);
            updateTransactionStatus(transactionEntity, TransactionStatus.FAILED);
            throw e;
        }
    }

    private void performBalanceTopUp(Long customerId, BigDecimal amount) {
        BalanceChangeRequest balanceChangeRequest = BalanceChangeRequest.builder()
                .amount(amount)
                .build();
        log.debug("Calling customerClient.increaseBalance for customerId={}, amount={}", customerId, amount);
        customerClient.increaseBalance(customerId, balanceChangeRequest);
        log.debug("customerClient.increaseBalance call completed for customerId={}", customerId);
    }

    public TransactionResponseDto purchase(Long customerId, TransactionPurchaseRequestDto request) {
        log.info("Starting purchase for customerId={}, amount={}", customerId, request.getAmount());

        TransactionEntity transactionEntity =
                TransactionMapper.INSTANCE.toTransactionEntity(customerId, request);
        transactionRepository.save(transactionEntity);
        log.debug("TransactionEntity saved: transactionId={}, customerId={}, amount={}",
                transactionEntity.getTransactionId(), customerId, request.getAmount());

        try {
            performBalanceDecrease(customerId, request.getAmount());

            /* After successful balance decrease, further purchase logic (e.g., order processing)
             would be implemented here.
             In production, consider using the Outbox Pattern to ensure atomicity between database changes and
             event publishing. */

            updateTransactionStatus(transactionEntity, COMPLETED);
            log.info("Balance decrease successful for transactionId={}", transactionEntity.getTransactionId());
            return TransactionResponseDto.builder().transactionId(transactionEntity.getTransactionId()).build();
        } catch (Exception e) {
            log.error("Failed to decrease balance for customerId={} with transactionId={}",
                    customerId, transactionEntity.getTransactionId(), e);
            updateTransactionStatus(transactionEntity, TransactionStatus.FAILED);
            throw e;
        }
    }

    private void performBalanceDecrease(Long customerId, BigDecimal amount) {
        BalanceChangeRequest balanceChangeRequest = BalanceChangeRequest.builder()
                .amount(amount)
                .build();
        log.debug("Calling customerClient.decreaseBalance for customerId={}, amount={}", customerId, amount);
        customerClient.decreaseBalance(customerId, balanceChangeRequest);
        log.debug("customerClient.decreaseBalance call completed for customerId={}", customerId);
    }

    private void updateTransactionStatus(TransactionEntity transactionEntity, TransactionStatus status) {
        transactionEntity.setStatus(status);
        transactionRepository.save(transactionEntity);
        log.debug("Transaction status updated: transactionId={}, newStatus={}", transactionEntity.getTransactionId(),
                status);
    }

    public TransactionResponseDto partialRefund(Long customerId, TransactionRefundRequestDto request) {
        log.info("Starting partial refund for customerId={}, amount={}", customerId, request.getAmount());

        TransactionEntity refundTransaction = TransactionMapper.INSTANCE.toTransactionEntity(customerId, request);
        transactionRepository.save(refundTransaction);
        log.debug("Saved refund transaction: transactionId={}, customerId={}, amount={}",
                refundTransaction.getTransactionId(), customerId, request.getAmount());

        try {
            TransactionEntity originalTransaction =
                    transactionRepository.findByTransactionId(request.getTransactionId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Transaction not found for id: " + request.getTransactionId()));

            validateOriginalTransaction(originalTransaction, request.getAmount());

            performBalanceTopUp(customerId, request.getAmount());
            updateTransactionStatus(refundTransaction, TransactionStatus.COMPLETED);

            log.info("Partial refund completed successfully for transactionId={}",
                    refundTransaction.getTransactionId());
            return TransactionResponseDto.builder().transactionId(refundTransaction.getTransactionId()).build();
        } catch (Exception e) {
            log.error("Partial refund failed for customerId={} with transactionId={}",
                    customerId, refundTransaction.getTransactionId(), e);
            updateTransactionStatus(refundTransaction, TransactionStatus.FAILED);
            throw e;
        }
    }

    private void validateOriginalTransaction(TransactionEntity transaction, BigDecimal refundAmount) {
        if (!COMPLETED.equals(transaction.getStatus()) ||
                !TransactionType.PURCHASE.equals(transaction.getTransactionType())) {
            throw InvalidOriginalTransactionException.of(INVALID_ORIGINAL_TRANSACTION_MESSAGE);
        }

        if (refundAmount.compareTo(transaction.getAmount()) > 0) {
            throw RefundAmountExceedsOriginalException.of(REFUND_AMOUNT_EXCEEDS_ORIGINAL_MESSAGE);
        }

        BigDecimal executedRefunds = transactionRepository
                .findAllByReferencedTransactionIdAndStatus(transaction.getTransactionId(), COMPLETED)
                .stream()
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAfterRefund = refundAmount.add(executedRefunds);

        if (totalAfterRefund.compareTo(transaction.getAmount()) > 0) {
            throw TotalRefundExceedsOriginalException.of(TOTAL_REFUND_EXCEEDS_ORIGINAL_MESSAGE);
        }
    }

}
