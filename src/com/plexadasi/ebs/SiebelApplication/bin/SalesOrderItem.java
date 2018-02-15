/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.services.SalesOrderService;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class SalesOrderItem extends Product implements Impl
{

    protected Integer soldToOrgId;
    protected String field;
    private static final String BUS_OBJ = "Order Entry (Sales)";
    private static final String BUS_COMP = "Order Entry - Line Items";
    
    public SalesOrderItem(SiebelDataBean CONN)
    {
        super(CONN);
        this.busComp = BUS_COMP;
        this.busObj = BUS_OBJ;
        field = Product.FIELD_ORDER_NUMBER;
    }
    
    com.plexadasi.ebs.model.SalesOrder salesOrder;
    
    public List<Map<String, String>> inventoryItems(Connection ebsConn) throws SiebelException
    {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SiebelPropertySet values = CONN.newPropertySet();
        set = CONN.newPropertySet();
        set.setProperty(Product.PLX_PART_NUMBER, "0");
        set.setProperty(SalesOrderItem.PLX_ITEM_PRICE, "1");
        set.setProperty(SalesOrderItem.PLX_LOT_ID, "3");
        // Pass the properties to siebel class
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        List<Map<String, String>> item = new ArrayList();
        while(isRecord){
            sbBC.getMultipleFieldValues(this.set, values);
            Map<String, String> items = new HashMap();
            final Enumeration<String> e = set.getPropertyNames();
            while (e.hasMoreElements()){
                String value = e.nextElement();
                items.put(set.getProperty(value), values.getProperty(value));
            }
            isRecord = sbBC.nextRecord();
        }
        return item;
    }
    
    public void onHandQuantities(Connection ebsConn, String type) throws SiebelBusinessServiceException, SiebelException, SQLException
    {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SalesOrderService sos = new SalesOrderService(ebsConn);
        this.salesOrder = new com.plexadasi.ebs.model.SalesOrder();
        SiebelPropertySet values = CONN.newPropertySet();
        // Set properties
        this.set.setProperty(this.field, this.field);
        this.set.setProperty(Product.PLX_PART_NUMBER, Product.PLX_PART_NUMBER);
        this.set.setProperty(Product.PLX_LOT_ID, Product.PLX_LOT_ID);
        // Pass the properties to siebel class
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        // Loop through record if it contains data
        while (isRecord)
        {
            sbBC.getMultipleFieldValues(this.set, values);
            salesOrder.setOrderNumber(values.getProperty(this.field));
            this.salesOrder.setPartNumber(values.getProperty(Product.PLX_PART_NUMBER));
            this.salesOrder.setWarehouseId(Integer.parseInt(values.getProperty(Product.PLX_LOT_ID)));
            com.plexadasi.ebs.model.SalesOrder salesOrderService = sos.findOnHandQuantity(this.salesOrder, type);
            // Write field value to siebel business component
            try{
                this.salesOrder.setQuantity(salesOrderService.getQuantity());
                sbBC.setFieldValue(Product.PLX_QUANTITY_AVAILABLE, String.valueOf(this.salesOrder.getQuantity()));
                MyLogging.log(Level.INFO, this.salesOrder.toString());
            }catch(NullPointerException e){}
            isRecord = sbBC.nextRecord();
        }
        // Write record and release object from memory
        sbBC.writeRecord();
        s.release();
    }
    
    public void backOrder(Connection ebsConn) throws SiebelBusinessServiceException, SQLException, SiebelException{
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SalesOrderService sos = new SalesOrderService(ebsConn);
        this.salesOrder = new com.plexadasi.ebs.model.SalesOrder();
        SiebelPropertySet values = CONN.newPropertySet();
        this.set.setProperty(Product.FIELD_ORDER_NUMBER, Product.FIELD_ORDER_NUMBER);
        this.set.setProperty(Product.PLX_PART_NUMBER, Product.PLX_PART_NUMBER);
        this.set.setProperty(Product.PLX_PRODUCT, Product.PLX_PRODUCT);
        this.set.setProperty(Product.PICK_MEANING, Product.PICK_MEANING);
        this.set.setProperty(Product.RELEASE_STATUS, Product.RELEASE_STATUS);
        this.set.setProperty(Product.PLX_LOT_ID, Product.PLX_LOT_ID);
        this.set.setProperty("Back Order Quantity", "Back Order Quantity");
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        while (isRecord)
        {
            sbBC.getMultipleFieldValues(this.set, values);
            salesOrder.setPartNumber(values.getProperty(Product.PLX_PART_NUMBER));
            salesOrder.setOrderNumber(values.getProperty(Product.FIELD_ORDER_NUMBER));
            salesOrder.setWarehouseId(DataConverter.toInt(values.getProperty(Product.PLX_LOT_ID)));
            salesOrder.setPartName(values.getProperty(Product.PLX_PRODUCT));
            BackOrder backorder = sos.findBackOrderedItem(salesOrder);
            // Write values to siebel business component
            try{
                sbBC.setFieldValue(Product.PICK_MEANING, backorder.getPickMeaning());
                sbBC.setFieldValue(Product.STATUS, backorder.getItemStatus());
                sbBC.setFieldValue(Product.RELEASE_STATUS, backorder.getReleaseStatus());
                sbBC.setFieldValue("Back Order Quantity", String.valueOf(backorder.getQuantity()));
                MyLogging.log(Level.INFO, this.salesOrder.toString() + backorder.toString());
            }catch(NullPointerException e){}
            isRecord = sbBC.nextRecord();
        }
        sbBC.writeRecord();
        s.release();
    }
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException 
    {
        sbBC.setSearchSpec(Product.FIELD_ORDER_NUMBER, this.siebelAccountId);  
        sbBC.setSearchSpec("Product Type", "Equipment");
        sbBC.setSearchSpec(Product.PLX_LOT_ID, String.valueOf(this.soldToOrgId));
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}

    public void setOrgId(Integer soldToOrgId) {
        this.soldToOrgId = soldToOrgId;
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}