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
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.lang.enu.Columns;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.SalesOrderService;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SalesOrderItem
extends Product
implements Impl {
    protected Integer soldToOrgId;
    private static final String BUS_OBJ = "Order Entry (Sales)";
    private static final String BUS_COMP = "Order Entry - Line Items";
    private static final String PART_NUMBER = Columns.Order.PART_NUMBER;
    Order salesOrder;
    Boolean checkAtpStatus = true;
    

    public SalesOrderItem(SiebelDataBean CONN) {
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
        this.set.setProperty(PART_NUMBER, PART_NUMBER);
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

    public void onHandQuantities(Connection ebsConn, String type) 
            throws SiebelBusinessServiceException, SiebelException, SQLException {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SalesOrderService sos = new SalesOrderService(ebsConn);
        checkAtpStatus = false;
        this.salesOrder = new Order();
        SiebelPropertySet values = CONN.newPropertySet();
        this.set.setProperty("Order Number", "Order Number");
        this.set.setProperty(PART_NUMBER, PART_NUMBER);
        this.set.setProperty("Source Inventory Loc Integration Id", "Source Inventory Loc Integration Id");
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        while (isRecord) {
            sbBC.getMultipleFieldValues(this.set, values);
            
            String orderNumber = values.getProperty("Order Number");
            String partNumber = values.getProperty(PART_NUMBER);
            String warehouse = values.getProperty("Source Inventory Loc Integration Id");
            
            if("".equals(warehouse)){
                warehouse = String.valueOf(this.soldToOrgId);
            }
            
            this.salesOrder.setOrderNumber(orderNumber);
            this.salesOrder.setPartNumber(partNumber);
            this.salesOrder.setWarehouseId(DataConverter.toInt(warehouse));
            
            Order salesOrderService = sos.findOnHandQuantity(this.salesOrder, type);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            if(salesOrderService.getQuantity() != null) {
                Integer quantity = salesOrderService.getQuantity();
                this.salesOrder.setQuantity(quantity);
                String status = "Out Of Stock";
                String date = "";
                if(quantity > 0){
                    status = "Available";
                    date = String.valueOf(dateFormat.format(new Date()));
                }
                sbBC.setFieldValue("Available Date", date);
                sbBC.setFieldValue("Available Quantity", String.valueOf(quantity));
                sbBC.setFieldValue("ATP Status", status);
                MyLogging.log(Level.INFO, this.salesOrder.toString());
            }
            else {
                // empty catch block
                sbBC.setFieldValue("Available Quantity", "0");
                sbBC.setFieldValue("Available Date", "");
                //sbBC.setFieldValue("Available Date", String.valueOf(dateFormat.format(new Date())));
                sbBC.setFieldValue("ATP Status", "Unavailable");
                MyLogging.log(Level.INFO, this.salesOrder.toString());
                
            }
            isRecord = sbBC.nextRecord();
        }
        sbBC.writeRecord();
        s.release();
    }

    public void backOrder(Connection ebsConn) throws SiebelBusinessServiceException, SQLException, SiebelException {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SalesOrderService sos = new SalesOrderService(ebsConn);
        this.salesOrder = new Order();
        SiebelPropertySet values = CONN.newPropertySet();
        this.set.setProperty("Order Number", "Order Number");
        this.set.setProperty(PART_NUMBER, PART_NUMBER);
        this.set.setProperty("Product", "Product");
        this.set.setProperty("Source Inventory Loc Integration Id", "Source Inventory Loc Integration Id");
        this.set.setProperty("PLX Pick Meaning", "PLX Pick Meaning");
        this.set.setProperty("PLX Release Status", "PLX Release Status");
        this.set.setProperty("Back Order Quantity", "Back Order Quantity");
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        while (isRecord) {
            sbBC.getMultipleFieldValues(this.set, values);
            
            
            String orderNumber = values.getProperty(Columns.Order.ORDER_NUMBER);
            String partNumber = values.getProperty(PART_NUMBER);
            Integer warehouseId = DataConverter.toInt(values.getProperty("Source Inventory Loc Integration Id"));
            warehouseId = warehouseId <= 0 ? soldToOrgId : warehouseId;
            
            this.salesOrder.setPartNumber(partNumber);
            this.salesOrder.setOrderNumber(orderNumber);
            this.salesOrder.setWarehouseId(warehouseId);
            this.salesOrder.setPartName(values.getProperty("Product"));
            BackOrder backorder = sos.findBookingLineItemStatus(this.salesOrder);
            try {
                sbBC.setFieldValue("PLX Pick Meaning", backorder.getPickMeaning());
                sbBC.setFieldValue("Status", backorder.getItemStatus());
                sbBC.setFieldValue("PLX Release Status", backorder.getReleaseStatus());
                sbBC.setFieldValue("Back Order Quantity", String.valueOf(backorder.getQuantity()));
                MyLogging.log(Level.INFO, this.salesOrder.toString() + backorder.toString());
            }
            catch (NullPointerException e) {
                // empty catch block
            }
            isRecord = sbBC.nextRecord();
        }
        sbBC.writeRecord();
        s.release();
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Order Number", this.siebelAccountId);
        sbBC.setSearchSpec("Product Type", "<>'Vehicle'");
        if(checkAtpStatus){
            sbBC.setSearchSpec("ATP Status", "<>'Unavailable'");
        }
        MyLogging.log(Level.INFO, "Check ATP STATUS: " + checkAtpStatus);
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

