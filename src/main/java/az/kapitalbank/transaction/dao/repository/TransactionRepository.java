package az.kapitalbank.transaction.dao.repository;

import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.model.enums.TransactionStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from TransactionEntity t where t.transactionId = :transactionId")
    Optional<TransactionEntity> findByTransactionIdForUpdate(@Param("transactionId") UUID transactionId);

    List<TransactionEntity> findAllByReferencedTransactionIdAndStatus(UUID referencedTransactionId,
                                                                      TransactionStatus status);
}