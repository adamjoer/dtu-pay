package dtu.fm22.facade.record;

public record TokenRequest(
        String customerId,
        int numberOfTokens
) {
}
