/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.SiebelApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogging {
    static Logger logger;
    public Handler fileHandler;
    Formatter plainText;
    private static InetAddress ip;
    private static String hIP;
    private static final String OS;
    String prop_file_path = "";
    String propfilepath;
    String vlogFile = "";
    String logFile = "";
    String logName = "EbsLogger_";

    public void setLogName(String name) {
        this.logName = name;
    }

    private MyLogging() throws IOException {
        ip = InetAddress.getLocalHost();
        hIP = ip.getHostAddress();
        if (OS.contains("nix") || OS.contains("nux")) {
            this.propfilepath = this.prop_file_path = "/usr/app/siebel/intg/intg.properties";
            this.vlogFile = "nix_logfile";
        } else if (OS.contains("win")) {
            this.propfilepath = "C:\\temp\\intg\\intg.properties";
            this.vlogFile = "win_logfile";
        }
        this.getProperties();
        Date date = new Date();
        SimpleDateFormat app = new SimpleDateFormat("dd-MM-yyyy");
        String dateApp = app.format(date);
        this.logFile = this.logFile + this.logName + dateApp + ".log";
        logger = Logger.getLogger(MyLogging.class.getName() + "\n\n");
        this.fileHandler = new FileHandler(this.logFile, true);
        this.plainText = new SimpleFormatter();
        this.fileHandler.setFormatter(this.plainText);
        logger.addHandler(this.fileHandler);
    }

    private void getProperties() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        FileInputStream input = new FileInputStream(this.propfilepath);
        prop.load(input);
        this.logFile = prop.getProperty(this.vlogFile);
    }

    private static Logger getLogger() {
        if (logger == null) {
            try {
                new MyLogging();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    public static void log(Level level, String msg) {
        MyLogging.getLogger().log(level, msg);
    }

    public static void log(Level level, String msg, Object obj) {
        MyLogging.getLogger().log(level, msg, obj);
    }

    public static void main(String[] args) {
        MyLogging.log(Level.INFO, "TRUE");
    }

    static {
        ip = null;
        hIP = "";
        OS = System.getProperty("os.name").toLowerCase();
    }
}

