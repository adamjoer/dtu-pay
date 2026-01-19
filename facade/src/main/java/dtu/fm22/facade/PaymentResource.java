package dtu.fm22.facade;


import dtu.fm22.facade.record.Payment;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import dtu.fm22.facade.record.PaymentRequest;
import dtu.fm22.facade.service.PaymentFacadeService;

import java.util.Collection;

@Path("/payments")
public class PaymentResource {

    private final PaymentFacadeService payService;

    @Inject
    public PaymentResource(PaymentFacadeService payService) {
        this.payService = payService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Payment createPayment(PaymentRequest paymentRequest) {
        return payService.createPayment(paymentRequest);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Payment> getAllPayments() {
        return payService.getAllPayments();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment getPaymentById(@PathParam("id") String id) {
        return payService.getPaymentById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
    }
}
