package com.plexadasi.ebs.SiebelApplication;


import com.siebel.eai.SiebelBusinessServiceException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class ApplicationProperties implements IProperties
{
       
    private static String propfilepath = "";
    private static String vlogFile = "";
    private static String templateFile = ""; 
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final StringWriter ERROR = new StringWriter();
    
    @Override
    public IProperties setProperties(String prop)
    {
        if (OS.contains("nix") || OS.contains("nux")) 
        {                
            propfilepath = "/usr/app/siebel/intg/intg.properties";
            vlogFile = "nix_logfile";
        } 
        else if (OS.contains("win")) 
        {
            propfilepath = "C:\\temp\\intg\\intg.properties";
            vlogFile = "win_logfile";
        }
        else
        {
            throw new NullPointerException("Null pointer exception");
        }
        templateFile = prop;
        return this;
    }
    
    @Override
    public IProperties setProperties(String nix, String win)
    {
        if (OS.contains("nix") || OS.contains("nux")) 
        {                
            propfilepath = "/usr/app/siebel/intg/intg.properties";
            vlogFile = "nix_logfile";
            templateFile = nix;
        } 
        else if (OS.contains("win")) 
        {
            propfilepath = "C:\\temp\\intg\\intg.properties";
            vlogFile = "win_logfile";
            templateFile = win;
        }
        else
        {
            throw new NullPointerException("Null pointer exception");
        }
        return this;
    }
    
    @Override
    public String getProperty() throws SiebelBusinessServiceException
    {
        String output = "";
        FileInputStream input;
        try 
        {
            Properties prop = new Properties();
            MyLogging.log(Level.INFO, "Properties file path:" + propfilepath);
            input = new FileInputStream(propfilepath);
            prop.load(input);
            output = prop.getProperty(templateFile);
            input.close();
        } 
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught Exception:" + ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, "Caught Exception:"+ERROR.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", ERROR.toString()); 
        }
        return output;
    }
}
