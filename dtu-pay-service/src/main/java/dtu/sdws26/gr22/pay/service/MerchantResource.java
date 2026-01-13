package dtu.sdws26.gr22.pay.service;

import dtu.sdws26.gr22.pay.service.exceptions.DTUPayException;
import dtu.sdws26.gr22.pay.service.record.Merchant;
import dtu.sdws26.gr22.pay.service.service.MerchantService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/merchants")
public class MerchantResource {

    private final MerchantService merchantService;

    @Inject
    public MerchantResource(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String registerMerchant(Merchant merchant) {
        var id = merchantService.register(
                merchant.firstName(),
                merchant.lastName(),
                merchant.cprNumber(),
                merchant.bankId()
        );
        return id.toString();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Merchant getMerchantById(@PathParam("id") String id) {
        return merchantService.getById(id).orElseThrow(() -> new NotFoundException("Merchant not found"));
    }

    @DELETE
    @Path("/{id}")
    public void deleteMerchant(@PathParam("id") String id) {
        try {
            merchantService.unregister(id);
        } catch (DTUPayException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
