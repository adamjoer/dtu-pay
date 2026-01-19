package dtu.fm22.e2e.record;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Payment(UUID id, Customer customer, Merchant merchant, BigDecimal amount, Instant timestamp) {
}
