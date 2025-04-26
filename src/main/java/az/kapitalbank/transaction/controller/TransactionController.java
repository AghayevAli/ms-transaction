package az.kapitalbank.transaction.controller;

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
    public ResponseEntity<Void> createCustomer(@RequestHeader("Customer-Id") Long customerId,
                                               @Valid @RequestBody TransactionTopUpRequestDto request) {
        transactionService.topUpBalance(customerId, request);

        return ResponseEntity.noContent().build();
    }
}
