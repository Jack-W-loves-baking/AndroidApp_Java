/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package location;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jack Wang
 */
@Entity
@Table(name = "LOCATIONDATA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Locationdata.findAll", query = "SELECT l FROM Locationdata l")
    , @NamedQuery(name = "Locationdata.findById", query = "SELECT l FROM Locationdata l WHERE l.id = :id")
    , @NamedQuery(name = "Locationdata.findByLatitude", query = "SELECT l FROM Locationdata l WHERE l.latitude = :latitude")
    , @NamedQuery(name = "Locationdata.findByLongitude", query = "SELECT l FROM Locationdata l WHERE l.longitude = :longitude")
    , @NamedQuery(name = "Locationdata.findByTitle", query = "SELECT l FROM Locationdata l WHERE l.title = :title")})
public class Locationdata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LATITUDE")
    private Double latitude;
    @Column(name = "LONGITUDE")
    private Double longitude;
    @Size(max = 255)
    @Column(name = "TITLE")
    private String title;

    public Locationdata() {
    }

    public Locationdata(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Locationdata)) {
            return false;
        }
        Locationdata other = (Locationdata) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "location.Locationdata[ id=" + id + " ]";
    }
    
}
