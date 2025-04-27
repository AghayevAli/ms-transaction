package az.kapitalbank.transaction.mapper;

import az.kapitalbank.transaction.dao.entity.TransactionEntity;
import az.kapitalbank.transaction.model.dto.TransactionPurchaseRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionRefundRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = java.util.UUID.class)
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "transactionId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "transactionType", constant = "TOP_UP")
    @Mapping(target = "status", constant = "PENDING")
    TransactionEntity toTransactionEntity(Long customerId, TransactionTopUpRequestDto request);

    @Mapping(target = "transactionId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "transactionType", constant = "PURCHASE")
    @Mapping(target = "status", constant = "PENDING")
    TransactionEntity toTransactionEntity(Long customerId, TransactionPurchaseRequestDto request);

    @Mapping(target = "transactionId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "transactionType", constant = "REFUND")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "referencedTransactionId", source = "request.transactionId")
    TransactionEntity toTransactionEntity(Long customerId, TransactionRefundRequestDto request);

}
