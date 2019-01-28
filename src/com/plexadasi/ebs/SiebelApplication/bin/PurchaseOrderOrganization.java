/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelSearch;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.logging.Level;

public class PurchaseOrderOrganization
extends Account
implements Impl {
    private static final String BUS_COMP = "Account";
    private static final String BUS_OBJ = "Account";

    public PurchaseOrderOrganization(SiebelDataBean CONN) {
        super(CONN);
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        SiebelSearch s = new SiebelSearch(CONN);
        s.setSField("Currency Code", "");
        s.setSField("Primary Ship To Street Address", "");
        s.setSField("Primary Bill To Street Address", "");
        s.setSField("EBS Id", "");
        s.setSField("Primary Bill To City", "");
        s.setSField("Primary Bill To State", "");
        s.setSField("Primary Bill To Country", "");
        s.setSField("Primary Ship To City", "");
        s.setSField("Primary Ship To State", "");
        s.setSField("Primary Ship To Country", "");
        this.set = s.getSField("Account", "Account", this);
        MyLogging.log(Level.INFO, this.set.toString());
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Id", this.siebelAccountId);
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

