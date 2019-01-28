/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelBusObject
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
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
import java.io.Writer;
import java.util.logging.Level;

public class SiebelSearch {
    protected static SiebelDataBean sdb = new SiebelDataBean();
    private final StringWriter errors = new StringWriter();
    protected SiebelPropertySet properties;
    protected SiebelPropertySet values;
    protected Integer beginCount = 1;
    public SiebelBusComp sbBC;

    public SiebelSearch(SiebelDataBean conn) {
        sdb = conn;
        this.properties = this.values = new SiebelPropertySet();
    }

    public static SiebelDataBean getService() {
        return sdb;
    }

    public void setSField(String K, String V) {
        this.properties.setProperty(K, V);
    }

    public SiebelPropertySet getSField(String bO, String bC, Impl qM) throws SiebelBusinessServiceException {
        SiebelPropertySet List2 = new SiebelPropertySet();
        try {
            SiebelBusObject sbBO = sdb.getBusObject(bO);
            this.sbBC = sbBO.getBusComp(bC);
            this.values = sdb.newPropertySet();
            this.sbBC.setViewMode(3);
            this.sbBC.clearToQuery();
            this.sbBC.activateMultipleFields(this.properties);
            qM.searchSpec(this.sbBC);
            this.sbBC.executeQuery2(true, true);
            List2 = this.doTrigger(this.sbBC);
            this.sbBC.release();
            this.sbBC.release();
        }
        catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in getSField: " + this.errors.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", this.errors.toString());
        }
        return List2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected SiebelPropertySet doTrigger(SiebelBusComp sbBC) throws SiebelBusinessServiceException {
        try {
            boolean isRecord = sbBC.firstRecord();
            while (isRecord) {
                try {
                    sbBC.getMultipleFieldValues(this.properties, this.values);
                    isRecord = sbBC.nextRecord();
                }
                catch (SiebelException ex) {
                    ex.printStackTrace(new PrintWriter(this.errors));
                    MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + this.errors.toString());
                    throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", this.errors.toString());
                }
            }
            return this.values;
        }
        catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + this.errors.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", this.errors.toString());
        }
    }

    public String getFieldValue(String V) throws SiebelException {
        return this.sbBC.getFieldValue(V);
    }
}

