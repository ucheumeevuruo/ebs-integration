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
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.logging.Level;

public class Order
extends Product
implements Impl {
    private SiebelSearch siebelSearch = null;
    private SiebelPropertySet setProp;
    private static final String BUS_OBJ = "Order Entry";
    private static final String BUS_COMP = "Order Entry - Orders";

    public Order(SiebelDataBean CONN) {
        super(CONN);
        this.siebelSearch = new SiebelSearch(CONN);
    }

    public void setPropertySet(String key, String value) {
        this.siebelSearch.setSField(key, value);
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        this.setProp = this.siebelSearch.getSField("Order Entry", "Order Entry - Orders", this);
        MyLogging.log(Level.INFO, "Creating Objects: " + (Object)this.setProp);
    }

    public String getProperty(String property) {
        return this.setProp.getProperty(property);
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Id", this.siebelAccountId);
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }
}

