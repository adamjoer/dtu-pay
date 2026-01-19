package dtu.fm22.facade.record;


import java.util.UUID;

public record Merchant(
        UUID id,
        String firstName,
        String lastName,
        String cprNumber,
        String bankId
) {
}
