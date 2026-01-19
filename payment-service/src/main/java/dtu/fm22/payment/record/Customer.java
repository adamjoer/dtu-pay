package dtu.fm22.payment.record;

import java.util.UUID;

public record Customer(
        UUID id,
        String firstName,
        String lastName,
        String cprNumber,
        String bankId
) {
}
