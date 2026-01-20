package dtu.fm22.facade;

import dtu.fm22.facade.record.Payment;
import dtu.fm22.facade.service.ManagerFacadeService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.Collection;
import java.util.UUID;

@Path("/reports")
public class ManagerResource {

    public final ManagerFacadeService managerFacadeService;

    public ManagerResource(ManagerFacadeService managerFacadeService) {
        this.managerFacadeService = managerFacadeService;
    }

    @GET
    @Path("/customers/{id}")
    public Collection<Payment> getCustomerReport(@PathParam("id") String id) {
        return managerFacadeService.getCustomerReport(id).orElseThrow(() -> new NotFoundException("Customer not found"));
    }


    @GET
    @Path("/merchants/{id}")
    public Collection<Payment> getMerchantReport(@PathParam("id") String id) {
        return managerFacadeService.getMerchantReport(id).orElseThrow(() -> new NotFoundException("Customer not found"));
    }
}
