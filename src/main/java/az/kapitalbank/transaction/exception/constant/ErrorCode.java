package az.kapitalbank.transaction.exception.constant;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ErrorCode {

    public static final String INVALID_ORIGINAL_TRANSACTION = "invlalid_original_transaction";
    public static final String REFUND_AMOUNT_EXCEEDS = "refund_amount_exceeds";
    public static final String TOTAL_REFUND_EXCEEDS = "total_refund_exceeds";
    public static final String TRANSACTION_NOT_FOUND = "transaction_not_found";

}
