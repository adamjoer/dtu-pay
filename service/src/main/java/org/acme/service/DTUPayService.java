package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.exceptions.UserNotFoundException;
import org.acme.record.Costumer;
import org.acme.record.Merchant;
import org.acme.record.Payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DTUPayService {

    private final ConcurrentHashMap<UUID, Costumer> costumers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Merchant> merchants = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Payment> payments = new ConcurrentHashMap<>();

    public UUID registerCostumer(String name) {
        var id = UUID.randomUUID();
        var costumer = new Costumer(name);
        costumers.put(id, costumer);
        return id;
    }

    public void deleteCostumer(UUID id) {
        if (!costumers.containsKey(id)) {
            throw new UserNotFoundException("Costumer does not exist");
        }
        costumers.remove(id);
    }

    public Optional<Costumer> getCostumerById(UUID id) {
        var costumer = costumers.get(id);
        return Optional.ofNullable(costumer);
    }

    public UUID registerMerchant(String name) {
        var id = UUID.randomUUID();
        var merchant = new Merchant(name);
        merchants.put(id, merchant);
        return id;
    }

    public void deleteMerchant(UUID id) {
        if (!merchants.containsKey(id)) {
            throw new UserNotFoundException("Merchant does not exist");
        }
        merchants.remove(id);
    }

    public Optional<Merchant> getMerchantById(UUID id) {
        var merchant = merchants.get(id);
        return Optional.ofNullable(merchant);
    }

    public UUID createPayment(UUID costumerId, UUID merchantId, BigDecimal amount) {
        UUID paymentId = UUID.randomUUID();
        if (!costumers.containsKey(costumerId)) {
            throw new UserNotFoundException("Costumer does not exist");
        }
        if (!merchants.containsKey(merchantId)) {
            throw new UserNotFoundException("Merchant does not exist");
        }

        var payment = new Payment(paymentId, costumerId, merchantId, amount, Instant.now());
        payments.put(paymentId, payment);
        return paymentId;
    }

    public Optional<Payment> getPaymentById(UUID id) {
        var payment = payments.get(id);
        return Optional.ofNullable(payment);
    }

    public Collection<Payment> getAllPayments() {
        return payments.values();
    }
}
