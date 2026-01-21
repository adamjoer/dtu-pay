package dtu.fm22.payment.record;

public record PaymentInfoRequest(
        String customerId,
        String merchantId
) {
}
