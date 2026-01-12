package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.exceptions.UserNotFoundException;
import org.acme.record.Customer;
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

    private final ConcurrentHashMap<UUID, Customer> customers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Merchant> merchants = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Payment> payments = new ConcurrentHashMap<>();

    public UUID registerCustomer(String firstName, String lastName, String cprNumber, String bankId) {
        var id = UUID.randomUUID();
        var customer = new Customer(id, firstName, lastName, cprNumber, bankId);
        customers.put(id, customer);
        return id;
    }

    public void deleteCustomer(UUID id) {
        if (!customers.containsKey(id)) {
            throw new UserNotFoundException(String.format("merchant with id \"%s\" is unknown", id));
        }
        customers.remove(id);
    }

    public Optional<Customer> getCustomerById(UUID id) {
        var customer = customers.get(id);
        return Optional.ofNullable(customer);
    }

    public UUID registerMerchant(String firstName, String lastName, String cprNumber, String bankId) {
        var id = UUID.randomUUID();
        var merchant = new Merchant(id, firstName, lastName, cprNumber, bankId);
        merchants.put(id, merchant);
        return id;
    }

    public void deleteMerchant(UUID id) {
        if (!merchants.containsKey(id)) {
            throw new UserNotFoundException(String.format("merchant with id \"%s\" is unknown", id));
        }
        merchants.remove(id);
    }

    public Optional<Merchant> getMerchantById(UUID id) {
        var merchant = merchants.get(id);
        return Optional.ofNullable(merchant);
    }

    public UUID createPayment(UUID customerId, UUID merchantId, BigDecimal amount) {
        UUID paymentId = UUID.randomUUID();
        var customer = customers.get(customerId);
        if (customer == null) {
            throw new UserNotFoundException(String.format("customer with id \"%s\" is unknown", customerId));
        }
        var merchant = merchants.get(merchantId);
        if (merchant == null) {
            throw new UserNotFoundException(String.format("merchant with id \"%s\" is unknown", merchantId));
        }

        var payment = new Payment(paymentId, customer, merchant, amount, Instant.now());
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
