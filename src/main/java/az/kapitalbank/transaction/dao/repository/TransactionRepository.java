package az.kapitalbank.transaction.dao.repository;

import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

}