package dtu.fm22.e2e.record;

import java.math.BigDecimal;
import java.util.UUID;

public record Payment(
        UUID id,
        Customer customer,
        Merchant merchant,
        BigDecimal amount,
        String token,
        String timestamp
) {
}
