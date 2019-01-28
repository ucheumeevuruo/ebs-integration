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

import com.plexadasi.ebs.SiebelApplication.SiebelService;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelBusObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;

public class SiebelServiceClone
extends SiebelService {
    public SiebelServiceClone(SiebelDataBean conn) {
        super(conn);
    }

    public SiebelServiceClone fields(String bO, String bC, Impl qM) throws SiebelException {
        this.sbBO = sdb.getBusObject(bO);
        this.sbBC = this.sbBO.getBusComp(bC);
        values = sdb.newPropertySet();
        this.sbBC.setViewMode(3);
        this.sbBC.clearToQuery();
        this.sbBC.activateMultipleFields(properties);
        qM.searchSpec(this.sbBC);
        this.sbBC.executeQuery2(true, true);
        return this;
    }

    public SiebelBusComp getBusComp() {
        return this.sbBC;
    }

    public void release() {
        this.sbBC.release();
        this.sbBO.release();
    }
}

