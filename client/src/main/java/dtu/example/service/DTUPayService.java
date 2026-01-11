package dtu.example.service;

import dtu.example.record.Customer;
import dtu.example.record.Merchant;
import dtu.example.record.PaymentRequest;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

public class DTUPayService {

    private final String baseUrl;

    public DTUPayService() {
        this("http://localhost:8080");
    }

    public DTUPayService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String register(Customer customer) {
        try (var client = ClientBuilder.newClient()) {
            try (var response = client.target(baseUrl).path("pay/costumers").request().post(Entity.json(customer))) {
                return response.readEntity(String.class);
            }
        }
    }

    public void unregisterCustomer(String customerId) {
        try (var client = ClientBuilder.newClient()) {
            client.target(baseUrl).path("pay/costumers").path(customerId).request().delete();
        }
    }

    public String register(Merchant customer) {
        try (var client = ClientBuilder.newClient()) {
            try (var response = client.target(baseUrl).path("pay/merchants").request().post(Entity.json(customer))) {
                return response.readEntity(String.class);
            }
        }
    }

    public void unregisterMerchant(String merchantId) {
        try (var client = ClientBuilder.newClient()) {
            client.target(baseUrl).path("pay/merchants").path(merchantId).request().delete();
        }
    }

    public boolean pay(Double amount, String customerId, String merchantId) {
        try (var client = ClientBuilder.newClient()) {
            var paymentRequest = new PaymentRequest(customerId, merchantId, amount);
            try (var response = client.target(baseUrl).path("pay/payments").request().post(Entity.json(paymentRequest))) {
                return response.getStatus() == 200;
            } catch (Exception ex) {
                throw new RuntimeException("payment failed");
            }
        }
    }
}
