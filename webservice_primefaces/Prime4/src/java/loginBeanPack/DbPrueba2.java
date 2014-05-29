/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginBeanPack;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mnrohoden
 */
@Entity
@Table(name = "db_prueba2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DbPrueba2.findAll", query = "SELECT d FROM DbPrueba2 d"),
    @NamedQuery(name = "DbPrueba2.findById", query = "SELECT d FROM DbPrueba2 d WHERE d.id = :id"),
    @NamedQuery(name = "DbPrueba2.findBySensor1", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor1 = :sensor1"),
    @NamedQuery(name = "DbPrueba2.findBySensor2", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor2 = :sensor2"),
    @NamedQuery(name = "DbPrueba2.findBySensor3", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor3 = :sensor3"),
    @NamedQuery(name = "DbPrueba2.findBySensor4", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor4 = :sensor4"),
    @NamedQuery(name = "DbPrueba2.findBySensor5", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor5 = :sensor5"),
    @NamedQuery(name = "DbPrueba2.findBySensor6", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor6 = :sensor6"),
    @NamedQuery(name = "DbPrueba2.findBySensor7", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor7 = :sensor7"),
    @NamedQuery(name = "DbPrueba2.findBySensor8", query = "SELECT d FROM DbPrueba2 d WHERE d.sensor8 = :sensor8"),
    @NamedQuery(name = "DbPrueba2.findByFecha", query = "SELECT d FROM DbPrueba2 d WHERE d.fecha = :fecha")})
public class DbPrueba2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sensor1")
    private Float sensor1;
    @Column(name = "sensor2")
    private Float sensor2;
    @Column(name = "sensor3")
    private Float sensor3;
    @Column(name = "sensor4")
    private Float sensor4;
    @Column(name = "sensor5")
    private Float sensor5;
    @Column(name = "sensor6")
    private Float sensor6;
    @Column(name = "sensor7")
    private Float sensor7;
    @Column(name = "sensor8")
    private Float sensor8;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public DbPrueba2() {
    }

    public DbPrueba2(Integer id) {
        this.id = id;
    }

    public DbPrueba2(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getSensor1() {
        return sensor1;
    }

    public void setSensor1(Float sensor1) {
        this.sensor1 = sensor1;
    }

    public Float getSensor2() {
        return sensor2;
    }

    public void setSensor2(Float sensor2) {
        this.sensor2 = sensor2;
    }

    public Float getSensor3() {
        return sensor3;
    }

    public void setSensor3(Float sensor3) {
        this.sensor3 = sensor3;
    }

    public Float getSensor4() {
        return sensor4;
    }

    public void setSensor4(Float sensor4) {
        this.sensor4 = sensor4;
    }

    public Float getSensor5() {
        return sensor5;
    }

    public void setSensor5(Float sensor5) {
        this.sensor5 = sensor5;
    }

    public Float getSensor6() {
        return sensor6;
    }

    public void setSensor6(Float sensor6) {
        this.sensor6 = sensor6;
    }

    public Float getSensor7() {
        return sensor7;
    }

    public void setSensor7(Float sensor7) {
        this.sensor7 = sensor7;
    }

    public Float getSensor8() {
        return sensor8;
    }

    public void setSensor8(Float sensor8) {
        this.sensor8 = sensor8;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        if (!(object instanceof DbPrueba2)) {
            return false;
        }
        DbPrueba2 other = (DbPrueba2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "loginBeanPack.DbPrueba2[ id=" + id + " ]";
    }
    
}
