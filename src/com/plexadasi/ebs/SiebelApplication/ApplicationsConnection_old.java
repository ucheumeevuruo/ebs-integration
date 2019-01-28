/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 */
package com.plexadasi.ebs.SiebelApplication;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationsConnection_old {
    private static final Logger LOG = Logger.getLogger(ApplicationsConnection_old.class.getName());
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static String prop_file_path = "";
    private static String ebs_database = "";
    private static String ebs_dbuser = "";
    private static String ebs_dbpassword = "";
    public static String propfilepath = "";
    private static String logFile = "";
    private static String vlogFile = "";
    private static String entrpr_name = "";
    private static String gateway_server = "";
    private static String sieb_objmgr = "";
    private static String username = "";
    private static String password = "";
    private static String sieb_database = "";
    private static String sieb_username = "";
    private static String sieb_password = "";
    private static final String gateway_port = "2321";
    private static StringWriter errors = new StringWriter();

    public ApplicationsConnection_old() {
        ApplicationsConnection_old.initializePropertyValues();
    }

    private static void initializePropertyValues() {
        LOG.log(Level.INFO, "Initializing connection properties .... ");
        if (OS.contains("nix") || OS.contains("nux")) {
            prop_file_path = "/usr/app/siebel/intg/intg.properties";
            vlogFile = "nix_connect_logfile";
        } else if (OS.contains("win")) {
            prop_file_path = "C:\\temp\\intg\\intg.properties";
            vlogFile = "win_connect_logfile";
        }
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream(prop_file_path);
            prop.load(input);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            LOG.log(Level.SEVERE, "initializePropertyValues:FileNotFoundException", errors.toString());
        }
        catch (IOException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            LOG.log(Level.SEVERE, "initializePropertyValues:IOException", errors.toString());
        }
        ebs_database = prop.getProperty("ebs_database");
        ebs_dbuser = prop.getProperty("ebs_dbuser");
        ebs_dbpassword = prop.getProperty("ebs_dbpassword");
        entrpr_name = prop.getProperty("entrpr_name");
        gateway_server = prop.getProperty("gateway_server");
        sieb_objmgr = prop.getProperty("sieb_objmgr");
        username = prop.getProperty("username");
        password = prop.getProperty("password");
        sieb_database = prop.getProperty("sieb_database");
        sieb_username = prop.getProperty("sieb_dbuser");
        sieb_password = prop.getProperty("sieb_dbpassword");
        LOG.log(Level.INFO, "Values are entrpr_name:{0},gateway_server:{1},username:{2},password{3}" + entrpr_name + "" + gateway_server + "" + username + "" + password);
        LOG.log(Level.INFO, "Siebel database:{0},sieb_username:{1},sieb_password:{2}" + sieb_database + "" + sieb_username + "" + sieb_password);
        logFile = prop.getProperty(vlogFile);
        LOG.log(Level.INFO, "EBS database:{0},username:{1},password{2}" + ebs_database + "" + ebs_dbuser + "" + ebs_dbpassword);
    }

    public static Connection connectToEBSDatabase() {
        ApplicationsConnection_old.initializePropertyValues();
        Connection connection = null;
        try {
            MyLogging.log(Level.INFO, "Connection to EBS begin ....");
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace(new PrintWriter(errors));
                MyLogging.log(Level.SEVERE, "Error in driver class:" + errors.toString());
            }
            MyLogging.log(Level.INFO, "Driver found ....");
            MyLogging.log(Level.INFO, "Values ..{0}:{1}:{2}" + ebs_database + ":" + ebs_dbuser + ":" + ebs_dbpassword);
            connection = DriverManager.getConnection(ebs_database, ebs_dbuser, ebs_dbpassword);
            MyLogging.log(Level.INFO, "Connected");
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "ERROR IN connecting To EBS Database Method:" + errors.toString());
            return connection;
        }
    }

    public static Connection connectToSiebelDatabase() {
        ApplicationsConnection_old.initializePropertyValues();
        Connection connection = null;
        try {
            MyLogging.log(Level.SEVERE, "Connection to Siebel begin ....");
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace(new PrintWriter(errors));
                MyLogging.log(Level.SEVERE, "Error in driver class:", errors.toString());
            }
            MyLogging.log(Level.INFO, "Driver found ....");
            MyLogging.log(Level.INFO, "Values ..{0}:{1}:{2}" + sieb_database + "" + sieb_username + "" + sieb_password);
            connection = DriverManager.getConnection(sieb_database, sieb_username, sieb_password);
            MyLogging.log(Level.INFO, "Connected");
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "ERROR IN connecting To Siebel Database Method:", errors.toString());
            return connection;
        }
    }

    public static SiebelDataBean connectSiebelServer() {
        ApplicationsConnection_old.initializePropertyValues();
        MyLogging.log(Level.INFO, "Connecting to Siebel .... ");
        SiebelDataBean dataBean = null;
        String connectString = String.format("Siebel://" + gateway_server + ":" + "2321" + "/" + entrpr_name + "/" + sieb_objmgr, new Object[0]);
        MyLogging.log(Level.INFO, "Connection string is connectString:{0} ", connectString);
        try {
            dataBean = new SiebelDataBean();
            MyLogging.log(Level.INFO, "Username is:" + username + " Password is:" + password);
            dataBean.login(connectString, username, password, "enu");
            MyLogging.log(Level.INFO, "Connection SUCCESSFUL");
        }
        catch (SiebelException e) {
            e.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "ERROR IN ConnectSiebelServer Method:" + errors.toString());
        }
        return dataBean;
    }
}

