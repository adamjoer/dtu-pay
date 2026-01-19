package dtu.fm22.facade.record;

public record PaymentRequest(
        String customerId,
        String merchantId,
        String amount,
        String token
) {
}
