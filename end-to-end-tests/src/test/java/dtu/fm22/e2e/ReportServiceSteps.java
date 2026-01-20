package dtu.fm22.e2e;

import dtu.fm22.e2e.record.Customer;
import dtu.fm22.e2e.record.Merchant;
import dtu.fm22.e2e.service.CustomerService;
import dtu.fm22.e2e.service.MerchantService;
import dtu.fm22.e2e.service.PaymentService;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankService_Service;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReportServiceSteps {
    private final String API_KEY = System.getenv("SIMPLE_DTU_PAY_API_KEY");

    private Customer customer;
    private Merchant merchant;

    private List<String> tokens;

    private final CustomerService customerService = new CustomerService();
    private final MerchantService merchantService = new MerchantService();
    private final PaymentService paymentService = new PaymentService();

    private boolean successful = false;
    private String errorMessage;

    private final BankService bank = new BankService_Service().getBankServicePort();
    private final List<String> accounts = new ArrayList<>();

    public String registerAccount(String firstName, String lastName, String cprNumber, String initialBalance) throws BankServiceException_Exception {
        Assert.assertNotNull("API_KEY environment variable is not set", API_KEY);
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
        if (customer != null && customer.id != null) {
            customerService.unregister(customer);
        }
        if (merchant != null && merchant.id != null) {
            merchantService.unregister(merchant);
        }

        for (var account : accounts) {
            bank.retireAccount(API_KEY, account);
        }
    }

    @When("the customer initiates a payment for {string} kr to the merchant")
    public void theCustomerInitiatesAPaymentForKrToTheMerchant(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the merchant requests a transaction report")
    public void theMerchantRequestsATransactionReport() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the report includes a transaction of {string} kr from {string} to {string}")
    public void theReportIncludesATransactionOfKrFromTo(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}
