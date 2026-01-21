package dtu.fm22.user.record;

public record PaymentInfoRequest(
        String customerId,
        String merchantId
) {
}
