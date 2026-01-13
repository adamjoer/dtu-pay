package dtu.sdws26.gr22.pay.service.service;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankService_Service;
import jakarta.enterprise.context.ApplicationScoped;
import dtu.sdws26.gr22.pay.service.exceptions.CustomerNotFoundException;
import dtu.sdws26.gr22.pay.service.exceptions.MerchantNotFoundException;
import dtu.sdws26.gr22.pay.service.exceptions.PaymentException;
import dtu.sdws26.gr22.pay.service.record.Payment;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PaymentService {
    private final String API_KEY = System.getenv("SIMPLE_DTU_PAY_API_KEY");

    private final ConcurrentHashMap<UUID, Payment> payments = new ConcurrentHashMap<>();

    private final BankService bankService = new BankService_Service().getBankServicePort();

    private final CustomerService customerService;
    private final MerchantService merchantService;

    @Inject
    public PaymentService(CustomerService customerService, MerchantService merchantService) {
        this.customerService = customerService;
        this.merchantService = merchantService;
    }

    public UUID createPayment(String customerId, String merchantId, BigDecimal amount) {
        var maybeCustomer = customerService.getById(customerId);
        if (maybeCustomer.isEmpty()) {
            throw new CustomerNotFoundException(customerId);
        }
        var maybeMerchant = merchantService.getById(merchantId);
        if (maybeMerchant.isEmpty()) {
            throw new MerchantNotFoundException(merchantId);
        }
        var customer = maybeCustomer.get();
        var merchant = maybeMerchant.get();

        try {
            bankService.transferMoneyFromTo(customer.bankId(),
                    merchant.bankId(),
                    amount,
                    "from " + customer.firstName() + " to " + merchant.firstName());
        } catch (BankServiceException_Exception e) {
            throw new PaymentException("Payment failed: " + e.getMessage());
        }

        var paymentId = UUID.randomUUID();
        var payment = new Payment(paymentId, customer, merchant, amount, Instant.now());
        payments.put(paymentId, payment);

        return paymentId;
    }

    public Optional<Payment> getPaymentById(String id) {
        try {
            var uuid = UUID.fromString(id);
            var payment = payments.get(uuid);
            return Optional.ofNullable(payment);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Collection<Payment> getAllPayments() {
        return payments.values();
    }
}
