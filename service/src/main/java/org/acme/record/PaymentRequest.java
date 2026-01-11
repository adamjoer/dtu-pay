package org.acme.record;

public record PaymentRequest(String costumerId, String merchantId, double amount) {
}
