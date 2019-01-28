/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;

public class PLXBackOrder
extends SalesOrderItem {
    public PLXBackOrder(SiebelDataBean CONN) {
        super(CONN);
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("PLX Release Status", "B");
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }
}

