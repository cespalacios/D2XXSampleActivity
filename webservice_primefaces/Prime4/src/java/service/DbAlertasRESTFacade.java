/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.DbAlertasJpaController;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.naming.InitialContext;
import loginBeanPack.DbAlertas;
import java.net.URI;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 *
 * @author mnrohoden
 */
@Path("loginbeanpack.dbalertas")
public class DbAlertasRESTFacade {

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return (EntityManagerFactory) new InitialContext().lookup("java:comp/env/persistence-factory");
    }

    private DbAlertasJpaController getJpaController() {
        try {
            UserTransaction utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            return new DbAlertasJpaController(utx, getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public DbAlertasRESTFacade() {
    }

    @POST
    @Consumes({"application/json"})
    public Response create(DbAlertas entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getItem().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/json"})
    public Response edit(DbAlertas entity) {
        try {
            getJpaController().edit(entity);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            getJpaController().destroy(id);
            return Response.ok().build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public DbAlertas find(@PathParam("id") Integer id) {
        return getJpaController().findDbAlertas(id);
    }

    @GET
    @Produces({"application/json"})
    public List<DbAlertas> findAll() {
        return getJpaController().findDbAlertasEntities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/json"})
    public List<DbAlertas> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findDbAlertasEntities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getDbAlertasCount());
    }
    
}
