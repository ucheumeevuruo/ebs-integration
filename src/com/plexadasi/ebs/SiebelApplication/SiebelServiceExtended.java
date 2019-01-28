/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 */
package com.plexadasi.ebs.SiebelApplication;

import com.plexadasi.ebs.SiebelApplication.SiebelService;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiebelServiceExtended
extends SiebelService {
    public SiebelServiceExtended(SiebelDataBean service) {
        super(service);
    }

    @Override
    protected List<Map<String, String>> doTrigger(SiebelBusComp sbBC) throws SiebelException {
        List list = new ArrayList<Map<String, String>>();
        boolean isRecord = sbBC.firstRecord();
        if (isRecord) {
            sbBC.getMultipleFieldValues(properties, values);
            list = this.Service_PreInvokeMethod(properties, values);
        }
        return list;
    }

    private List<Map<String, String>> Service_PreInvokeMethod(SiebelPropertySet Inputs, SiebelPropertySet Outputs) {
        String propName = Inputs.getFirstProperty();
        ArrayList<Map<String, String>> setList = new ArrayList<Map<String, String>>();
        while (!"".equals(propName)) {
            String propVal = Outputs.getProperty(propName);
            HashMap<String, String> mapProperty = new HashMap<String, String>();
            if (Inputs.propertyExists(propName)) {
                mapProperty.put(Inputs.getProperty(propName), propVal);
                setList.add(mapProperty);
            }
            propName = Inputs.getNextProperty();
        }
        return setList;
    }
}

