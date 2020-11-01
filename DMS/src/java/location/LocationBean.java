/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package location;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jack Wang
 */
@Stateless
@LocalBean
public class LocationBean {
    @PersistenceContext
    private EntityManager entityManager;
    
    
    public void addNewLocation(int ID,double latitude, double longitude,String title) { 
        try{ 
            Locationdata location = new Locationdata(); 
            location.setId(ID);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setTitle(title);
            entityManager.persist(location); 
        } 
        catch(EntityExistsException e)
        {} 
    }
 
}
