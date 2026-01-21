package dtu.fm22.payment.record;

public record PaymentRequest(
        String merchantId,
        String amount,
        String token
) {
}
