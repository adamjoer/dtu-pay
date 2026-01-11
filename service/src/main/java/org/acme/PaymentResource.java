package org.acme;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.exceptions.UserNotFoundException;
import org.acme.record.Costumer;
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
    @Path("/costumers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String registerCostumer(Costumer costumer) {
        var id = payService.registerCostumer(costumer.name());
        return id.toString();
    }

    @GET
    @Path("/costumers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Costumer getCostumerById(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            return payService.getCostumerById(uuid).orElseThrow(() -> new NotFoundException("Costumer not found"));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        }
    }

    @DELETE
    @Path("/costumers/{id}")
    public void deleteCostumer(@PathParam("id") String id) {
        try {
            var uuid = UUID.fromString(id);
            payService.deleteCostumer(uuid);
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
    public String registerMerchant(Merchant costumer) {
        var id = payService.registerMerchant(costumer.name());
        return id.toString();
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
    public String createPayment(PaymentRequest paymentRequest) {
        try {
            var paymentId = payService.createPayment(
                    UUID.fromString(paymentRequest.costumerId()),
                    UUID.fromString(paymentRequest.merchantId()),
                    new BigDecimal(paymentRequest.amount())
            );
            return paymentId.toString();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        } catch (UserNotFoundException e) {
            throw new NotFoundException(e.getMessage());
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
