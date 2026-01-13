package dtu.sdws26.gr22.pay.service.service;

import dtu.sdws26.gr22.pay.service.exceptions.MerchantNotFoundException;
import dtu.sdws26.gr22.pay.service.record.Merchant;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class MerchantService {
    private final ConcurrentHashMap<UUID, Merchant> merchants = new ConcurrentHashMap<>();

    public UUID register(String firstName, String lastName, String cprNumber, String bankId) {
        var id = UUID.randomUUID();
        var merchant = new Merchant(id, firstName, lastName, cprNumber, bankId);
        merchants.put(id, merchant);
        return id;
    }

    public void unregister(String id) {
        try {
            var uuid = UUID.fromString(id);
            if (!merchants.containsKey(uuid)) {
                throw new MerchantNotFoundException(uuid.toString());
            }
            merchants.remove(uuid);
        } catch (IllegalArgumentException e) {
            throw new MerchantNotFoundException(id);
        }
    }

    public Optional<Merchant> getById(String id) {
        try {
            var uuid = UUID.fromString(id);
            var merchant = merchants.get(uuid);
            return Optional.ofNullable(merchant);
        } catch( IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
