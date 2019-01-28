/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelBusObject
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 */
package com.plexadasi.ebs.SiebelApplication;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelBusObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiebelService {
    protected static SiebelDataBean sdb = new SiebelDataBean();
    protected SiebelBusObject sbBO = new SiebelBusObject();
    protected SiebelBusComp sbBC = new SiebelBusComp();
    private StringWriter errors;
    protected static SiebelPropertySet properties;
    protected static SiebelPropertySet values;
    protected Integer beginCount = 1;
    public String SIEBEL_EXCEPTION = "Caught Siebel Exception";

    public SiebelService(SiebelDataBean conn) {
        sdb = conn;
    }

    public SiebelDataBean getService() {
        return sdb;
    }

    public void serviceLogOff() throws SiebelException {
        sdb.logoff();
    }

    public void setSField(SiebelPropertySet property) {
        properties = property;
    }

    public List<Map<String, String>> getSField(String bO, String bC, Impl qM) throws SiebelException {
        this.sbBO = sdb.getBusObject(bO);
        this.sbBC = this.sbBO.getBusComp(bC);
        values = sdb.newPropertySet();
        this.sbBC.setViewMode(3);
        this.sbBC.clearToQuery();
        this.sbBC.activateMultipleFields(properties);
        qM.searchSpec(this.sbBC);
        this.sbBC.executeQuery2(true, true);
        List<Map<String, String>> List2 = this.doTrigger(this.sbBC);
        qM.getExtraParam(this.sbBC);
        this.sbBC.release();
        this.sbBC.release();
        return List2;
    }

    protected List<Map<String, String>> doTrigger(SiebelBusComp sbBC) throws SiebelException {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        boolean isRecord = sbBC.firstRecord();
        while (isRecord) {
            sbBC.getMultipleFieldValues(properties, values);
            list.add(this.Service_PreInvokeMethod(properties, values));
            isRecord = sbBC.nextRecord();
        }
        return list;
    }

    private Map<String, String> Service_PreInvokeMethod(SiebelPropertySet Inputs, SiebelPropertySet Outputs) {
        String propName = Inputs.getFirstProperty();
        HashMap<String, String> mapProperty = new HashMap<String, String>();
        while (!"".equals(propName)) {
            String propVal = Outputs.getProperty(propName);
            if (Inputs.propertyExists(propName)) {
                if ("Outline Number".equals(propName)) {
                    Integer n = this.beginCount;
                    Integer n2 = this.beginCount = Integer.valueOf(this.beginCount + 1);
                    propVal = String.valueOf(n);
                }
                mapProperty.put(Inputs.getProperty(propName), propVal);
            }
            propName = Inputs.getNextProperty();
        }
        return mapProperty;
    }
}

