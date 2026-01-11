package dtu.example.record;

import java.util.UUID;

public record Customer (String name, UUID id) {
    public Customer(String name) {
        this(name, UUID.randomUUID());
    }
}
