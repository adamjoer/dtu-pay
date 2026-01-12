package dtu.example;

import dtu.example.record.Customer;
import dtu.example.record.Merchant;
import dtu.example.record.Payment;
import dtu.example.service.DTUPayService;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankService_Service;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PaymentServiceSteps {
    private final String API_KEY = System.getenv("SIMPLE_DTU_PAY_API_KEY");

    private Customer customer;
    private Merchant merchant;
    private String customerId, merchantId;
    private final DTUPayService payService = new DTUPayService();

    private Collection<Payment> payments;

    private boolean successful = false;
    private String errorMessage;

    private final BankService bank = new BankService_Service().getBankServicePort();
    private final List<String> accounts = new ArrayList<>();

    public String registerAccount(String firstName, String lastName, String cprNumber, double initialBalance) throws BankServiceException_Exception {
        var user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCprNumber(cprNumber);

        var balance = new BigDecimal(initialBalance);
        var account = bank.createAccountWithBalance(API_KEY, user, balance);
        accounts.add(account);
        return account;
    }

    @After
    public void tearDown() throws BankServiceException_Exception {
        if (customerId != null) {
            payService.unregisterCustomer(customerId);
        }
        if (merchantId != null) {
            payService.unregisterMerchant(merchantId);
        }

        for (var account : accounts) {
            bank.retireAccount(API_KEY, account);
        }
    }

    @Given("a customer with name {string}")
    public void aCustomerWithName(String name) {
        customer = new Customer(UUID.randomUUID(), name);
    }

    @Given("the customer is registered with Simple DTU Pay")
    public void theCustomerIsRegisteredWithSimpleDTUPay() {
        customerId = payService.register(customer);
        Assert.assertNotNull(customerId);
        Assert.assertNotEquals("", customerId);
    }

    @Given("a merchant with name {string}")
    public void aMerchantWithName(String name) {
        merchant = new Merchant(UUID.randomUUID(), name);
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
        Assert.assertTrue("Payment not successful", successful);
    }

    @Given("a customer with name {string}, who is registered with Simple DTU Pay")
    public void aCustomerWithNameWhoIsRegisteredWithSimpleDTUPay(String name) {
        customer = new Customer(UUID.randomUUID(), name);
        customerId = payService.register(customer);
        Assert.assertNotNull(customerId);
        Assert.assertNotEquals("", customerId);
    }

    @Given("a merchant with name {string}, who is registered with Simple DTU Pay")
    public void aMerchantWithNameWhoIsRegisteredWithSimpleDTUPay(String name) {
        merchant = new Merchant(UUID.randomUUID(), name);
        merchantId = payService.register(merchant);
        Assert.assertNotNull(merchantId);
        Assert.assertNotEquals("", merchantId);
    }

    @Given("a successful payment of {double} kr from the customer to the merchant")
    public void aSuccessfulPaymentOfKrFromTheCustomerToTheMerchant(double amount) {
        try {
            successful = payService.pay(amount, customerId, merchantId);
            Assert.assertTrue(successful);
        } catch (Exception e) {
            Assert.fail("Payment should have been successful");
        }
    }

    @When("the manager asks for a list of payments")
    public void theManagerAsksForAListOfPayments() {
        payments = payService.getAllPayments();
    }

    @Then("the list contains a payments where customer {string} paid {double} kr to merchant {string}")
    public void theListContainsAPaymentsWhereCustomerPaidKrToMerchant(String customerName, Double amount, String merchantName) {
        boolean found = payments.stream().anyMatch(payment ->
                payment.customer().name().equals(customerName) &&
                        payment.merchant().name().equals(merchantName) &&
                        payment.amount().doubleValue() == amount
        );
        Assert.assertTrue("Payment not found in the list", found);
    }

    @When("the merchant initiates a payment for {double} kr using customer id {string}")
    public void theMerchantInitiatesAPaymentForKrUsingCustomerId(Double amount, String customerId) {
        try {
            successful = payService.pay(amount, customerId, merchantId);
        } catch (Exception e) {
            successful = false;
            errorMessage = e.getMessage();
        }
    }

    @Then("the payment is not successful")
    public void thePaymentIsNotSuccessful() {
        Assert.assertFalse("Payment was successful, but should not be", successful);
    }

    @Then("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String message) {
        Assert.assertEquals(message, errorMessage);
    }
}
