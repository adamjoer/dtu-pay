package org.acme.record;

import java.util.UUID;

public record Merchant(UUID id, String firstName, String lastName, String cprNumber, String bankId) {
}
