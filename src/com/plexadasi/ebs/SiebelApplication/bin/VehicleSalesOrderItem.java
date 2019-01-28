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

import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.lang.enu.Columns;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleSalesOrderItem
extends Product
implements Impl {
    protected Integer soldToOrgId;
    private static final String BUS_OBJ = "Order Entry (Sales)";
    private static final String BUS_COMP = "Order Entry - Line Items";
    private static final String PART_NUMBER = "Asset Number";

    public VehicleSalesOrderItem(SiebelDataBean CONN) {
        super(CONN);
        this.busComp = "Order Entry - Line Items";
        this.busObj = "Order Entry (Sales)";
    }

    public List<Map<String, String>> inventoryItems(Connection ebsConn) throws SiebelException {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SiebelPropertySet values = CONN.newPropertySet();
        this.set = CONN.newPropertySet();
        this.set.setProperty("Source Inventory Loc Integration Id", "Source Inventory Loc Integration Id");
        this.set.setProperty("Quantity", "Quantity");
        this.set.setProperty(PART_NUMBER, "Part Number");
        this.set.setProperty("Order Header Id", "Order Header Id");
        this.set.setProperty("Item Price", "Item Price");
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        ArrayList<Map<String, String>> item = new ArrayList<Map<String, String>>();
        while (isRecord) {
            sbBC.getMultipleFieldValues(this.set, values);
            HashMap<String, String> items = new HashMap<String, String>();
            Enumeration e = this.set.getPropertyNames();
            while (e.hasMoreElements()) {
                String value = (String)e.nextElement();
                items.put(this.set.getProperty(value), values.getProperty(value));
            }
            item.add(items);
            isRecord = sbBC.nextRecord();
        }
        return item;
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Order Number", this.siebelAccountId);
        sbBC.setSearchSpec("Product Type", "Vehicle");
        sbBC.setSearchSpec("ATP Status", "<>'Unavailable'");
        //sbBC.setSearchSpec("PLX Lot#", String.valueOf(this.soldToOrgId));
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }

    public void setOrgId(Integer soldToOrgId) {
        this.soldToOrgId = soldToOrgId;
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

