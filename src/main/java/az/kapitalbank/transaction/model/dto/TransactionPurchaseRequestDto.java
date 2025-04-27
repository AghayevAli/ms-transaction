package az.kapitalbank.transaction.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPurchaseRequestDto {
    @NotNull(message = "Amount must be provided")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
