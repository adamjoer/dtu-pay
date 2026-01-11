package dtu.example.record;

import java.util.UUID;

public record Merchant(String name, UUID id) {
    public Merchant(String name) {
        this(name, UUID.randomUUID());
    }
}
