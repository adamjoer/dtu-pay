package dtu.sdws26.gr22.pay.service.record;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

// Use java.time.Instant for an unambiguous UTC timestamp (or OffsetDateTime/ZonedDateTime if you need zone/offset info)

public record Payment(UUID id, Customer customer, Merchant merchant, BigDecimal amount, Instant timestamp) {
}
