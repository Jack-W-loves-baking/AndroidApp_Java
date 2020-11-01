/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package location.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import location.LocationBean;
import location.Locationdata;

/**
 *
 * @author Jack Wang
 */
@Stateless
@Path("/locationdata")
public class LocationdataFacadeREST extends AbstractFacade<Locationdata> {
    @EJB
    private LocationBean locationBean;
    @PersistenceContext(unitName = "AssignmentFourProjectPU")
    private EntityManager em;

    public LocationdataFacadeREST() {
        super(Locationdata.class);
    }
    
    //update the database
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewLocation(MultivaluedMap<String, Object> formParams){
        int ID = (Integer)formParams.getFirst("ID");
        Double latitude = (Double)formParams.getFirst("Latitude"); 
        Double longitude = (Double)formParams.getFirst("Longitude"); 
        String title = formParams.getFirst("Title").toString(); 
        locationBean.addNewLocation(ID,latitude, longitude,title);      
    }

    //list all the data in json format
    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Locationdata> findAll() {
        location.DBOperations db = new location.DBOperations();
        db.createConnection();
        db.createTable();
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
