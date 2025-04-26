package az.kapitalbank.transaction.service;

import az.kapitalbank.transaction.client.customer.CustomerClient;
import az.kapitalbank.transaction.client.customer.model.BalanceChangeRequest;
import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.dao.repository.TransactionRepository;
import az.kapitalbank.transaction.mapper.TransactionMapper;
import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
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

    public void topUpBalance(Long customerId, TransactionTopUpRequestDto request) {
        log.info("Starting top-up for customerId={}, amount={}", customerId, request.getAmount());

        TransactionEntity transactionEntity = createAndSaveTransaction(customerId, request);
        log.info("Created transaction with ID={} and status={}", transactionEntity.getTransactionId(),
                transactionEntity.getStatus());

        try {
            performBalanceTopUp(customerId, request.getAmount());
            updateTransactionStatus(transactionEntity, TransactionStatus.COMPLETED);
            log.info("Top-up successful for transactionId={}", transactionEntity.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to increase balance for customerId={} with transactionId={}",
                    customerId, transactionEntity.getTransactionId(), e);
            updateTransactionStatus(transactionEntity, TransactionStatus.FAILED);
        }
    }

    private TransactionEntity createAndSaveTransaction(Long customerId, TransactionTopUpRequestDto request) {
        TransactionEntity transactionEntity = TransactionMapper.INSTANCE.toTransactionEntity(customerId, request);
        transactionRepository.save(transactionEntity);
        log.debug("TransactionEntity saved: transactionId={}, customerId={}, amount={}",
                transactionEntity.getTransactionId(), customerId, request.getAmount());
        return transactionEntity;
    }

    private void performBalanceTopUp(Long customerId, BigDecimal amount) {
        BalanceChangeRequest balanceChangeRequest = BalanceChangeRequest.builder()
                .amount(amount)
                .build();
        log.debug("Calling customerClient.increaseBalance for customerId={}, amount={}", customerId, amount);
        customerClient.increaseBalance(customerId, balanceChangeRequest);
        log.debug("customerClient.increaseBalance call completed for customerId={}", customerId);
    }

    private void updateTransactionStatus(TransactionEntity transactionEntity, TransactionStatus status) {
        transactionEntity.setStatus(status);
        transactionRepository.save(transactionEntity);
        log.debug("Transaction status updated: transactionId={}, newStatus={}", transactionEntity.getTransactionId(),
                status);
    }
}
