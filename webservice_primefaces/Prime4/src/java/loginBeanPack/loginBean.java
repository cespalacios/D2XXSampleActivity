package loginBeanPack;

import java.sql.*;
import java.util.*;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.activation.DataSource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.management.Query;
import javax.naming.InitialContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

@ManagedBean(name = "loginBean")
public class loginBean implements Serializable {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String salir() {
        return "index";
    }

    //Funcíón para acceder a la página pag2.xhtml
    public String login2() {
        if (username != null && username.equals("admin") && password != null && password.equals("admin")) {
            System.out.println("VERDADERO");
            return "pag2";
        } else {
            System.out.println("FALSO");
            return null;
        }
    }

    public void login(ActionEvent eee) {

        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;

        if (username != null && username.equals("admin") && password != null && password.equals("admin")) {
            loggedIn = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
            System.out.println("VERDADERO");
        } else {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
            System.out.println("FALSO");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);

    }
    Connection con;
    Statement st;
    ResultSet rs;
    private List perInfoAll = new ArrayList();

    public List getperInfoAll() throws SQLException {
        int i = 0;
        try {
            perInfoAll.clear();
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_utpl", "root", "root");
            // Se crea un Statement, para realizar la consulta
            st = con.createStatement();
            rs = (ResultSet) st.executeQuery("SELECT * FROM db_prueba2");

            while (rs.next()) {
                System.out.println("ja ja ja");
                //System.out.println(rs.getString(10));
                perInfoAll.add(i, new perInfo(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getString(10)));
                i++;
                System.out.println(i);
            }
            con.close();
            rs.close();

        } catch (Exception e) {
            System.out.println("Error Data : " + e.getMessage());
        }
        createLinearModel();
        return perInfoAll;
    }

    /**
     * @param linearModel the linearModel to set
     */
    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }

    public class perInfo {

        String id;
        String sensor1;
        String sensor2;
        String sensor3;
        String sensor4;
        String sensor5;
        String sensor6;
        String sensor7;
        String sensor8;
        String sensor9;
        String sensor10;
        String fecha_hora;

        public perInfo(String id, String sensor1, String sensor2, String sensor3, String sensor4,
                String sensor5, String sensor6, String sensor7, String sensor8, String fecha_hora) {
            this.id = id;
            this.sensor1 = sensor1;
            this.sensor2 = sensor2;
            this.sensor3 = sensor3;
            this.sensor4 = sensor4;
            this.sensor5 = sensor5;
            this.sensor6 = sensor6;
            this.sensor7 = sensor7;
            this.sensor8 = sensor8;
            this.fecha_hora = fecha_hora;
        }

        public String getId() {
            return id;
        }

        public String getSensor1() {
            return sensor1;
        }

        public String getSensor2() {
            return sensor2;
        }

        public String getSensor3() {
            return sensor3;
        }

        public String getSensor4() {
            return sensor4;
        }

        public String getSensor5() {
            return sensor5;
        }

        public String getSensor6() {
            return sensor6;
        }

        public String getSensor7() {
            return sensor7;
        }

        public String getSensor8() {
            return sensor8;
        }

        public String getFecha_hora() {
            return fecha_hora;
        }
    }
    //////////////////////////////////////TABLA/////////////////////////////////
    private CartesianChartModel linearModel;

    public loginBean() {
        //createLinearModel();
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public void createLinearModel() throws SQLException {
        setLinearModel(new CartesianChartModel());

        LineChartSeries series1 = new LineChartSeries();
        LineChartSeries series2 = new LineChartSeries();
        LineChartSeries series3 = new LineChartSeries();
        LineChartSeries series4 = new LineChartSeries();
        LineChartSeries series5 = new LineChartSeries();
        LineChartSeries series6 = new LineChartSeries();
        LineChartSeries series7 = new LineChartSeries();
        LineChartSeries series8 = new LineChartSeries();
        series1.setLabel("Sensor 1");
        series2.setLabel("Sensor 2");
        series3.setLabel("Sensor 3");
        series4.setLabel("Sensor 4");
        series5.setLabel("Sensor 5");
        series6.setLabel("Sensor 6");
        series7.setLabel("Sensor 7");
        series8.setLabel("Sensor 8");

        //////////////// SENSORES //////////////////////////
        int i = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_utpl", "root", "root");
            // Se crea un Statement, para realizar la consulta
            st = con.createStatement();
            rs = (ResultSet) st.executeQuery("SELECT * FROM db_prueba2");

            while (rs.next()) {
                series1.set(rs.getInt("id"), rs.getInt("sensor1"));
                series2.set(rs.getInt("id"), rs.getInt("sensor2"));
                series3.set(rs.getInt("id"), rs.getInt("sensor3"));
                series4.set(rs.getInt("id"), rs.getInt("sensor4"));
                series5.set(rs.getInt("id"), rs.getInt("sensor5"));
                series6.set(rs.getInt("id"), rs.getInt("sensor6"));
                series7.set(rs.getInt("id"), rs.getInt("sensor7"));
                series8.set(rs.getInt("id"), rs.getInt("sensor8"));
//                series1.set(rs.getInt("fecha"), rs.getInt("sensor1"));
//                series2.set(rs.getInt("fecha"), rs.getInt("sensor2"));
//                series3.set(rs.getInt("fecha"), rs.getInt("sensor3"));
//                series4.set(rs.getInt("fecha"), rs.getInt("sensor4"));
//                series5.set(rs.getInt("fecha"), rs.getInt("sensor5"));
//                series6.set(rs.getInt("fecha"), rs.getInt("sensor6"));
//                series7.set(rs.getInt("fecha"), rs.getInt("sensor7"));
//                series8.set(rs.getInt("fecha"), rs.getInt("sensor8"));
                i++;
            }
            
            
            getLinearModel().addSeries(series1);
            getLinearModel().addSeries(series2);
            getLinearModel().addSeries(series3);
            getLinearModel().addSeries(series4);
            getLinearModel().addSeries(series5);
            getLinearModel().addSeries(series6);
            getLinearModel().addSeries(series7);
            getLinearModel().addSeries(series8);

        } catch (Exception e) {
            System.out.println("Error Data : " + e.getMessage());
        } finally{
            con.close();
        }


    }
}
