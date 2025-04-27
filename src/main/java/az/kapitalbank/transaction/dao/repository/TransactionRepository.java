package az.kapitalbank.transaction.dao.repository;

import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findByTransactionId(UUID transactionId);

    List<TransactionEntity> findAllByReferencedTransactionIdAndStatus(UUID referencedTransactionId,
                                                                      TransactionStatus status);

}