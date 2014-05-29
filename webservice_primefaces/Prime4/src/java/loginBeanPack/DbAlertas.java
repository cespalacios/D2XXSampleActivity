/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginBeanPack;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mnrohoden
 */
@Entity
@Table(name = "db_alertas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DbAlertas.findAll", query = "SELECT d FROM DbAlertas d"),
    @NamedQuery(name = "DbAlertas.findByItem", query = "SELECT d FROM DbAlertas d WHERE d.item = :item"),
    @NamedQuery(name = "DbAlertas.findByS1", query = "SELECT d FROM DbAlertas d WHERE d.s1 = :s1"),
    @NamedQuery(name = "DbAlertas.findByS2", query = "SELECT d FROM DbAlertas d WHERE d.s2 = :s2"),
    @NamedQuery(name = "DbAlertas.findByS3", query = "SELECT d FROM DbAlertas d WHERE d.s3 = :s3"),
    @NamedQuery(name = "DbAlertas.findByS4", query = "SELECT d FROM DbAlertas d WHERE d.s4 = :s4"),
    @NamedQuery(name = "DbAlertas.findByS5", query = "SELECT d FROM DbAlertas d WHERE d.s5 = :s5"),
    @NamedQuery(name = "DbAlertas.findByS6", query = "SELECT d FROM DbAlertas d WHERE d.s6 = :s6"),
    @NamedQuery(name = "DbAlertas.findByS7", query = "SELECT d FROM DbAlertas d WHERE d.s7 = :s7"),
    @NamedQuery(name = "DbAlertas.findByS8", query = "SELECT d FROM DbAlertas d WHERE d.s8 = :s8")})
public class DbAlertas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "item")
    private Integer item;
    @Size(max = 10)
    @Column(name = "s1")
    private String s1;
    @Size(max = 10)
    @Column(name = "s2")
    private String s2;
    @Size(max = 10)
    @Column(name = "s3")
    private String s3;
    @Size(max = 10)
    @Column(name = "s4")
    private String s4;
    @Size(max = 10)
    @Column(name = "s5")
    private String s5;
    @Size(max = 10)
    @Column(name = "s6")
    private String s6;
    @Size(max = 10)
    @Column(name = "s7")
    private String s7;
    @Size(max = 10)
    @Column(name = "s8")
    private String s8;

    public DbAlertas() {
    }

    public DbAlertas(Integer item) {
        this.item = item;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getS5() {
        return s5;
    }

    public void setS5(String s5) {
        this.s5 = s5;
    }

    public String getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = s6;
    }

    public String getS7() {
        return s7;
    }

    public void setS7(String s7) {
        this.s7 = s7;
    }

    public String getS8() {
        return s8;
    }

    public void setS8(String s8) {
        this.s8 = s8;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (item != null ? item.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbAlertas)) {
            return false;
        }
        DbAlertas other = (DbAlertas) object;
        if ((this.item == null && other.item != null) || (this.item != null && !this.item.equals(other.item))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "loginBeanPack.DbAlertas[ item=" + item + " ]";
    }
    
}
