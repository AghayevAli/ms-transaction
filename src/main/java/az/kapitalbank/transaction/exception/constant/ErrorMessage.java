package az.kapitalbank.transaction.exception.constant;

import lombok.NoArgsConstructor;


@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ErrorMessage {

    public static final String INVALID_ORIGINAL_TRANSACTION_MESSAGE =
            "Original transaction is not a completed purchase";
    public static final String REFUND_AMOUNT_EXCEEDS_ORIGINAL_MESSAGE =
            "Refund amount exceeds original transaction amount";
    public static final String TOTAL_REFUND_EXCEEDS_ORIGINAL_MESSAGE =
            "Total refunded amount exceeds original transaction amount";

    public static final String TRANSACTION_NOT_FOUND_MESSAGE =
            "Transaction with ID: [{}] not found";

}
