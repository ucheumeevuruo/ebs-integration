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

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.bin.Inventory;
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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PurchaseOrder
extends Product
implements Impl {
    private static final String BUS_OBJ = "Order Entry";
    private static final String BUS_COMP = "Order Entry - Orders";

    public PurchaseOrder(SiebelDataBean CONN) {
        super(CONN);
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
    }

    public Map<String, String> getQuoteHeader(SiebelPropertySet property) throws SiebelException {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SiebelPropertySet values = CONN.newPropertySet();
        s.setSField(property);
        SiebelBusComp sbBC = s.fields("Order Entry", "Order Entry - Orders", this).getBusComp();
        HashMap<String, String> items = new HashMap<String, String>();
        boolean isRecord = sbBC.firstRecord();
        while (isRecord) {
            sbBC.getMultipleFieldValues(property, values);
            Enumeration e = property.getPropertyNames();
            while (e.hasMoreElements()) {
                String value = (String)e.nextElement();
                items.put(property.getProperty(value), values.getProperty(value));
            }
            isRecord = sbBC.nextRecord();
        }
        return items;
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Order Number", this.siebelAccountId);
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }

    public PartListItems getPartQuoteItem() {
        return new PartListItems(CONN);
    }

    public class PartListItems
    extends Product
    implements Impl {
        private static final String BUS_OBJ = "Order Entry";
        private static final String BUS_COMP = "Order Entry - Line Items";
        private Integer warehouse;

        public PartListItems(SiebelDataBean CONN) {
            super(CONN);
        }

        public List<Inventory> getInventories(Connection ebsConn) throws SiebelBusinessServiceException {
            ArrayList<Inventory> inventory;
            SiebelServiceClone siebelService = new SiebelServiceClone(CONN);
            inventory = new ArrayList<Inventory>();
            try {
                SiebelPropertySet values = CONN.newPropertySet();
                this.set = CONN.newPropertySet();
                this.set.setProperty("Part Number", "Part Number");
                this.set.setProperty("Quantity", "Quantity");
                this.set.setProperty("Unit Price", "Unit Price");
                this.set.setProperty("Source Inventory Loc Integration Id", "Source Inventory Loc Integration Id");
                siebelService.setSField(this.set);
                SiebelBusComp sbBC = siebelService.fields("Order Entry", "Order Entry - Line Items", this).getBusComp();
                boolean isRecord = sbBC.firstRecord();
                while (isRecord) {
                    sbBC.getMultipleFieldValues(this.set, values);
                    Inventory inventories = new Inventory();
                    inventories.setPart_number(values.getProperty("Part Number"));
                    inventories.setOrg_id(DataConverter.toInt(values.getProperty("Source Inventory Loc Integration Id")));
                    inventories.setQuantity(Integer.parseInt(values.getProperty("Quantity")));
                    inventories.setAmount(DataConverter.toFloat(values.getProperty("Unit Price")));
                    inventories.setLine_type(HelperAP.getLineType());
                    inventory.add(inventories);
                    isRecord = sbBC.nextRecord();
                }
            }
            catch (SiebelException ex) {
                ex.printStackTrace(new PrintWriter(this.error));
                MyLogging.log(Level.SEVERE, siebelService.SIEBEL_EXCEPTION + " in " + this.getClass().getSimpleName() + this.error.toString());
                throw new SiebelBusinessServiceException(siebelService.getClass().getSimpleName(), ex.getMessage());
            }
            finally {
                siebelService.release();
            }
            return inventory;
        }

        @Override
        public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
            sbBC.setSearchSpec("Order Number", this.siebelAccountId);
            sbBC.setSearchSpec("Product Type", "Equipment");
        }

        @Override
        public void getExtraParam(SiebelBusComp sbBC) {
        }

        @Override
        public void doTrigger() throws SiebelBusinessServiceException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Integer getWarehouse() {
            return this.warehouse;
        }

        public void setWarehouse(Integer warehouse) {
            this.warehouse = warehouse;
        }
    }

}

