package com.plexadasi.ebs.SiebelApplication;


import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelBusObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Adeyemi
 */
public class SiebelSearch {  

    /**
     *
     */
    protected static SiebelDataBean sdb = new SiebelDataBean();
    private final StringWriter errors = new StringWriter();
    protected SiebelPropertySet properties, values;
    protected Integer beginCount = 1;
    public SiebelBusComp sbBC;
    
    public SiebelSearch(SiebelDataBean conn)
    {
        sdb = conn;
        properties = values = new SiebelPropertySet();
    }
    
    public static SiebelDataBean getService()
    {
        return sdb;
    }
    
    public void setSField(String K, String V)
    {
        properties.setProperty(K, V);
    }
    
    public SiebelPropertySet getSField(String bO, String bC, Impl qM) throws SiebelBusinessServiceException
    {
        SiebelPropertySet List = new SiebelPropertySet();
        try 
        {
            SiebelBusObject sbBO = sdb.getBusObject(bO);
            sbBC = sbBO.getBusComp(bC);
            values = sdb.newPropertySet();
            sbBC.setViewMode(3);
            sbBC.clearToQuery();
            // Activate all the fields
            sbBC.activateMultipleFields(properties);
            //Get search specification
            qM.searchSpec(sbBC);
            sbBC.executeQuery2(true, true);
            List = doTrigger(sbBC);
            sbBC.release();
            sbBC.release();
            
        } 
        catch (SiebelException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in getSField: " + errors.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", errors.toString()); 
        }
        return List;
    }
    
    protected SiebelPropertySet doTrigger(SiebelBusComp sbBC) throws SiebelBusinessServiceException
    {
        try 
        {
            boolean isRecord = sbBC.firstRecord();
            while (isRecord)
            {
                try {
                    sbBC.getMultipleFieldValues(properties, values);
                    isRecord = sbBC.nextRecord();
                } catch (SiebelException ex) {
                    ex.printStackTrace(new PrintWriter(errors));
                    MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + errors.toString());
                    throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", errors.toString()); 
                }
            }
        } 
        catch (SiebelException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + errors.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", errors.toString()); 
        }
        return values;
    }
    
    public String getFieldValue(String V) throws SiebelException
    {
        return sbBC.getFieldValue(V);
    }
}
