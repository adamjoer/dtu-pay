package dtu.sdws26.gr22.pay.service.service;

import dtu.sdws26.gr22.pay.service.exceptions.CustomerNotFoundException;
import dtu.sdws26.gr22.pay.service.exceptions.MerchantNotFoundException;
import dtu.sdws26.gr22.pay.service.record.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CustomerService {
    private final ConcurrentHashMap<UUID, Customer> customers = new ConcurrentHashMap<>();

    public UUID register(String firstName, String lastName, String cprNumber, String bankId) {
        var id = UUID.randomUUID();
        var customer = new Customer(id, firstName, lastName, cprNumber, bankId);
        customers.put(id, customer);
        return id;
    }

    public void unregister(String id) {
        try {
            var uuid = UUID.fromString(id);
            if (!customers.containsKey(uuid)) {
                throw new MerchantNotFoundException(uuid.toString());
            }
            customers.remove(uuid);
        } catch (IllegalArgumentException e) {
            throw new CustomerNotFoundException(id);
        }
    }

    public Optional<Customer> getById(String id) {
        try {
            var uuid = UUID.fromString(id);
            var customer = customers.get(uuid);
            return Optional.ofNullable(customer);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
