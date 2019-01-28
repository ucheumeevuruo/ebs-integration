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
import com.plexadasi.ebs.SiebelApplication.SiebelService;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class QLubricant
extends Product
implements Impl {
    private static final String BUS_OBJ = "Quote";
    private static final String BUS_COMP = "Quote Item";

    public QLubricant(SiebelDataBean CONN) {
        super(CONN);
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        try {
            SiebelService s = new SiebelService(CONN);
            this.set.setProperty("Product", "Product");
            this.set.setProperty("Quantity", "Quantity");
            this.set.setProperty("Product Inventory Item Id", "Product Inventory Item Id");
            this.set.setProperty("Item Price", "Item Price");
            this.set.setProperty("PLX Lot#", "PLX Lot#");
            s.setSField(this.set);
            setList = s.getSField("Quote", "Quote Item", this);
            MyLogging.log(Level.INFO, "Creating Objects: " + setList.toString());
        }
        catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(this.error));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception Line in doTrigger: " + this.error.toString());
            throw new SiebelBusinessServiceException("CAUGHT_EXCEPT", this.error.toString());
        }
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        MyLogging.log(Level.INFO, this.siebelAccountId);
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId);
        sbBC.setSearchSpec("Product Type", "Lubricants");
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }
}

