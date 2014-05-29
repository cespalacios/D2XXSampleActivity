package javaapplication4;

import java.io.*;
import java.net.*;
import java.sql.*;

public class JavaApplication4 {

    static int cont1 = 0; //contador general
    static int cont2 = 0; //contador cada 8 valores de sensor
    static int cont3 = 0; //contador base de datos
    static String valor1;
    static String valor2;
    static String valor3;
    static String valor4;
    static String valor5;
    static String valor6;
    static String valor7;
    static String valor8;
    static float val1;
    static float val2;
    static float val3;
    static float val4;
    static float val5;
    static float val6;
    static float val7;
    static float val8;
    static String s1;
    static String s2;
    static String s3;
    static String s4;
    static String s5;
    static String s6;
    static String s7;
    static String s8;
    static int lim = 28;
    static int fila = 1;
    static String on = "FUEGO";
    static String off = "nada";
    static String dbUserName = "root";
    static String dbPassword = "root";
    static String dbName = "db_utpl";
    static String ext = "ext";
    static String path = "F:/backup1/backupRFID";
    static String pathSQL = "C:\\Program Files\\MySQL\\bin\\mysqldump";
    static Connection conexion;
    static Statement st;
    static Socket s;
    static BufferedReader in;
    static boolean more = true;

    public static void main(String[] args) throws SQLException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_utpl", "root", "root");

            // Se crea un Statement, para realizar la consulta
            st = conexion.createStatement();

            st.executeUpdate("TRUNCATE db_utpl.db_prueba2");
            s = new Socket("192.168.11.2", 5000);
            s.setSoTimeout(17000);

            while (more) {
                InputStream ir = s.getInputStream();
                InputStreamReader isr = new InputStreamReader(ir);
                in = new BufferedReader(isr);

                try {
                    String line = in.readLine();
                    if (line.equals(null) || line.equals("")) {
                        //more = false;
                        //st.close();
                    } else {
                        System.out.println(line);
                        String[] line1 = line.split("\\_");
                        cont1++;

                        if (cont1 == 1) {
                            valor1 = line1[1];
                            val1 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 2) {
                            valor2 = line1[1];
                            val2 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 3) {
                            valor3 = line1[1];
                            val3 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 4) {
                            valor4 = line1[1];
                            val4 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 5) {
                            valor5 = line1[1];
                            val5 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 6) {
                            valor6 = line1[1];
                            val6 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 7) {
                            valor7 = line1[1];
                            val7 = Float.parseFloat(line1[1]);
                        }
                        if (cont1 == 8) {
                            valor8 = line1[1];
                            val8 = Float.parseFloat(line1[1]);
                        }

                        if (cont1 >= 8) {
                            cont2++;
                            if (val1 > lim) {
                                s1 = on;
                            } else {
                                s1 = off;
                            }
                            if (val2 > lim) {
                                s2 = on;
                            } else {
                                s2 = off;
                            }
                            if (val3 > lim) {
                                s3 = on;
                            } else {
                                s3 = off;
                            }
                            if (val4 > lim) {
                                s4 = on;
                            } else {
                                s4 = off;
                            }
                            if (val5 > lim) {
                                s5 = on;
                            } else {
                                s5 = off;
                            }
                            if (val6 > lim) {
                                s6 = on;
                            } else {
                                s6 = off;
                            }
                            if (val7 > lim) {
                                s7 = on;
                            } else {
                                s7 = off;
                            }
                            if (val8 > lim) {
                                s8 = on;
                            } else {
                                s8 = off;
                            }

                            st.executeUpdate("INSERT INTO db_utpl.db_prueba2 VALUES (" + cont2 + ",'" + valor1 + "','" + valor2 + "','" + valor3 + "','" + valor4 + "','" + valor5 + "','" + valor6 + "','" + valor7 + "','" + valor8 + "',CURRENT_TIMESTAMP)");
                            st.executeUpdate("TRUNCATE db_utpl.db_alertas");
                            st.executeUpdate("INSERT INTO db_utpl.db_alertas VALUES (" + cont2 + ",'" + s1 + "','" + s2 + "','" + s3 + "','" + s4 + "','" + s5 + "','" + s6 + "','" + s7 + "','" + s8 + "')");
                            cont1 = 0;

                            //BACKUP & TRUNCATE
                            if (cont2 == 50) {
                                cont3++;
                                String executeCmd = pathSQL + " -u root -proot " + dbName + " -r " + path + cont3 + ext;
                                Process runtimeProcess;
                                try {
                                    runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                                    int processComplete = runtimeProcess.waitFor();

                                    if (processComplete == 0) {
                                        System.out.println("Backup exitoso");
                                    } else {
                                        System.out.println("No se pudo crear el backup");
                                    }
                                } catch (Exception ex) {
                                    System.out.println("Error BACKUP : " + ex.getMessage());
                                }
                                cont2 = 0;
                                st.executeUpdate("TRUNCATE db_utpl.db_prueba2");
                            } //FIN - BACKUP & TRUNCATE
                        }
                    }
                } catch (SocketTimeoutException ex) {
                    System.out.println("Expiro el tiempo o fue desconectado");
                    conexion.close();
                    st.close();
                    s.close();
                } catch (Exception d) {
                    System.out.println("Expiro el tiempo o fue desconectado");
                    conexion.close();
                    st.close();
                    s.close();
                }//FIN TRY 2
            } //FIN WHILE
        } catch (Exception e) {
            System.out.println("Error CONECCIÓN : " + e.getClass());
            conexion.close();
            st.close();
            s.close();
        }  //FIN TRY 1
    }
}
