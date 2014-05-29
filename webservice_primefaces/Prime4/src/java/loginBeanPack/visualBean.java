/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loginBeanPack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author mnrohoden
 */
@ManagedBean
@RequestScoped
public class visualBean {

    /**
     * Creates a new instance of visualBean
     */
    private String tabla_visible = "inline";
    private String grafico_visible = "false";

    public visualBean() {
    }

    public void verTabla() {
        tabla_visible = "inline";
        grafico_visible = "hidden";

    }

    public void verGrafico() {
        tabla_visible = "none";
        grafico_visible = "true";

    }

    /**
     * @return the tabla_visible
     */
    public String getTabla_visible() {
        return tabla_visible;
    }

    /**
     * @param tabla_visible the tabla_visible to set
     */
    public void setTabla_visible(String tabla_visible) {
        this.tabla_visible = tabla_visible;
    }

    /**
     * @return the grafico_visible
     */
    public String getGrafico_visible() {
        return grafico_visible;
    }

    /**
     * @param grafico_visible the grafico_visible to set
     */
    public void setGrafico_visible(String grafico_visible) {
        this.grafico_visible = grafico_visible;
    }
}
