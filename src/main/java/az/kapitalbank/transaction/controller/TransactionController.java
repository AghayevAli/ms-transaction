package az.kapitalbank.transaction.controller;

import az.kapitalbank.transaction.model.dto.TransactionPurchaseRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionRefundRequestDto;
import az.kapitalbank.transaction.model.dto.TransactionResponseDto;
import az.kapitalbank.transaction.model.dto.TransactionTopUpRequestDto;
import az.kapitalbank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/top-up")
    public ResponseEntity<TransactionResponseDto> createCustomer(@RequestHeader("Customer-Id") Long customerId,
                                                                 @Valid @RequestBody
                                                                 TransactionTopUpRequestDto request) {

        return ResponseEntity.ok(transactionService.topUpBalance(customerId, request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<TransactionResponseDto> purchase(@RequestHeader("Customer-Id") Long customerId,
                                                           @Valid @RequestBody TransactionPurchaseRequestDto request) {

        return ResponseEntity.ok(transactionService.purchase(customerId, request));
    }

    @PostMapping("/refund")
    public ResponseEntity<TransactionResponseDto> refund(@RequestHeader("Customer-Id") Long customerId,
                                                         @Valid @RequestBody TransactionRefundRequestDto request) {


        return ResponseEntity.ok(transactionService.partialRefund(customerId, request));
    }
}
