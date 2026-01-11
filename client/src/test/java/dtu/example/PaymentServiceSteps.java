package dtu.example;

import dtu.example.record.Customer;
import dtu.example.record.Merchant;
import dtu.example.record.Payment;
import dtu.example.service.DTUPayService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.Assert;

import java.util.Collection;

public class PaymentServiceSteps {
    private Customer customer;
    private Merchant merchant;
    private String customerId, merchantId;
    private final DTUPayService payService = new DTUPayService();

    private Collection<Payment> payments;

    private boolean successful = false;

    @Given("a customer with name {string}")
    public void aCustomerWithName(String name) {
        customer = new Customer(name);
    }

    @Given("the customer is registered with Simple DTU Pay")
    public void theCustomerIsRegisteredWithSimpleDTUPay() {
        customerId = payService.register(customer);
        Assert.assertNotNull(customerId);
        Assert.assertNotEquals("", customerId);
    }

    @Given("a merchant with name {string}")
    public void aMerchantWithName(String name) {
        merchant = new Merchant(name);
    }

    @Given("the merchant is registered with Simple DTU Pay")
    public void theMerchantIsRegisteredWithSimpleDTUPay() {
        merchantId = payService.register(merchant);
        Assert.assertNotNull(customerId);
        Assert.assertNotEquals("", customerId);
    }

    @When("the merchant initiates a payment for {double} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(Double amount) {
        try {
            successful = payService.pay(amount, customerId, merchantId);
        } catch (Exception e) {
            successful = false;
        }
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        Assert.assertTrue(successful);
    }

    @After
    public void tearDown() {
        if (customerId != null) {
            payService.unregisterCustomer(customerId);
        }
        if (merchantId != null) {
            payService.unregisterMerchant(merchantId);
        }
    }
}
