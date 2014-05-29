/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import controller.DbPrueba2JpaController;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.naming.InitialContext;
import loginBeanPack.DbPrueba2;
import java.net.URI;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 *
 * @author mnrohoden
 */
@Path("loginbeanpack.dbprueba2")
public class DbPrueba2RESTFacade {

    private EntityManagerFactory getEntityManagerFactory() throws NamingException {
        return (EntityManagerFactory) new InitialContext().lookup("java:comp/env/persistence-factory");
    }

    private DbPrueba2JpaController getJpaController() {
        try {
            UserTransaction utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            return new DbPrueba2JpaController(utx, getEntityManagerFactory());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public DbPrueba2RESTFacade() {
    }

    @POST
    @Consumes({"application/json"})
    public Response create(DbPrueba2 entity) {
        try {
            getJpaController().create(entity);
            return Response.created(URI.create(entity.getId().toString())).build();
        } catch (Exception ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes({"application/json"})
    public Response edit(DbPrueba2 entity) {
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
    public DbPrueba2 find(@PathParam("id") Integer id) {
        return getJpaController().findDbPrueba2(id);
    }

    @GET
    @Produces({"application/json"})
    public List<DbPrueba2> findAll() {
        return getJpaController().findDbPrueba2Entities();
    }

    @GET
    @Path("{max}/{first}")
    @Produces({"application/json"})
    public List<DbPrueba2> findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        return getJpaController().findDbPrueba2Entities(max, first);
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String count() {
        return String.valueOf(getJpaController().getDbPrueba2Count());
    }
    
}
