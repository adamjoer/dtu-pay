package org.acme;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.exceptions.UserNotFoundException;
import org.acme.record.Customer;
import org.acme.record.Merchant;
import org.acme.record.PaymentRequest;
import org.acme.service.DTUPayService;

import java.math.BigDecimal;
import java.util.UUID;

@Path("/pay")
public class PaymentResource {

    private final DTUPayService payService;

    @Inject
    public PaymentResource(DTUPayService payService) {
        this.payService = payService;
    }

    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String registerCustomer(Customer customer) {
        try {
            var id = payService.registerCustomer(
                    customer.firstName(),
                    customer.lastName(),
                    customer.cprNumber(),
                    customer.bankId()
            );
            return id.toString();
        } catch (NullPointerException e) {
            throw new BadRequestException("Missing required fields");
        }
    }

    @GET
    @Path("/customers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomerById(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            return payService.getCustomerById(uuid).orElseThrow(() -> new NotFoundException("Customer not found"));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        }
    }

    @DELETE
    @Path("/customers/{id}")
    public void deleteCustomer(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            payService.deleteCustomer(uuid);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @POST
    @Path("/merchants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String registerMerchant(Merchant merchant) {
        try {
            var id = payService.registerMerchant(
                    merchant.firstName(),
                    merchant.lastName(),
                    merchant.cprNumber(),
                    merchant.bankId()
            );
            return id.toString();
        } catch (NullPointerException e) {
            throw new BadRequestException("Missing required fields");
        }
    }

    @GET
    @Path("/merchants/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Merchant getMerchantById(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            return payService.getMerchantById(uuid).orElseThrow(() -> new NotFoundException("Merchant not found"));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        }
    }

    @DELETE
    @Path("/merchants/{id}")
    public void deleteMerchant(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            payService.deleteMerchant(uuid);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @POST
    @Path("/payments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPayment(PaymentRequest paymentRequest) {
        try {
            var paymentId = payService.createPayment(
                    UUID.fromString(paymentRequest.customerId()),
                    UUID.fromString(paymentRequest.merchantId()),
                    new BigDecimal(paymentRequest.amount())
            );
            return Response.ok().entity(paymentId.toString()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID format").build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }
    }

    @GET
    @Path("/payments")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getAllPayments() {
        return payService.getAllPayments();
    }

    @GET
    @Path("/payments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getPaymentById(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            return payService.getPaymentById(uuid).orElseThrow(() -> new NotFoundException("Payment not found"));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        }
    }
}
