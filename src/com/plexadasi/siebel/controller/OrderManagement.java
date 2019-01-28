/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.siebel.controller;

import com.plexadasi.siebel.BeanProcessor;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.Logging;
import com.plexadasi.ebs.services.EBSOrderManagementService;
import com.plexadasi.helper.DataConverter;
import com.plexadasi.siebel.Iinventory;
import com.plexadasi.siebel.model.OrderEntry;
import com.plexadasi.siebel.model.Sales;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class OrderManagement {
    protected final Connection connection;
    protected BeanProcessor convert = new BeanProcessor();
    private final SiebelPropertySet input;
    private final SiebelPropertySet output;

    /**
     * 
     * @param connection
     * @param input
     * @param output 
     */
    public OrderManagement(Connection connection, SiebelPropertySet input, SiebelPropertySet output) {
        this.connection = connection;
        this.input = input;
        this.output = output;
    }

    /**
     * 
     * @throws SQLException
     * @throws SiebelBusinessServiceException
     * @throws SiebelException 
     */
    public void createInvoice() throws SQLException, SiebelBusinessServiceException, SiebelException {
        OrderEntry invoice = this.convert.toBean(this.input, OrderEntry.class);
        
        this.addExtraChildPropertySet(this.input, HelperAP.getFreightCharges(), invoice.getFreight());
        this.addExtraChildPropertySet(this.input, HelperAP.getSundries(), invoice.getSundries());
        this.addExtraChildPropertySet(this.input, HelperAP.getWithholdingTax(), invoice.getTax());
        this.addExtraChildPropertySet(this.input, HelperAP.getComputerProgramming(), invoice.getComputerProgramming());
        this.addExtraChildPropertySet(this.input, HelperAP.getDeliveryCharges(), invoice.getDeliveryCharges());
        
        List<Iinventory> inventories = (List)this.propertyChildToBeanList(this.input, Sales.class);
        
        Logging.logger(this.getClass()).log(Level.INFO, "{0}\n{1}",
                new Object[]{invoice.toString(), inventories.toString()});
        
        EBSOrderManagementService orderManagement = 
                new EBSOrderManagementService(this.connection, invoice, inventories);
        
        Map<String, Object> results = orderManagement.createInvoice();
        
        if(results != null){
            orderManagement.updateInvoice(
                    DataConverter.toInt(
                            (String)results.get("CUST_TRX_ID")
                    )
            );
            this.mapToPropertySet(results, this.output);
        }
        
    }
    
    /**
     * 
     * @throws SiebelException
     * @throws SQLException
     * @throws SiebelBusinessServiceException 
     */
    public void createPurchaseOrder() 
            throws SiebelException, SQLException, SiebelBusinessServiceException{
        OrderEntry order = this.convert.toBean(this.input, OrderEntry.class);
        List<Iinventory> inventories = (List)this.propertyChildToBeanList(this.input, Sales.class);
        
        Logging.logger(this.getClass()).log(Level.INFO, "{0}\n{1}", 
                new Object[]{order.toString(), inventories.toString()});
        
        EBSOrderManagementService orderManagement = 
                new EBSOrderManagementService(this.connection, order, inventories);
        
        Map<String, Object> results = orderManagement.createPurchaseOrder();
        this.mapToPropertySet(results, this.output);
    }
    
     public void createOrder() 
            throws SiebelException, SQLException, SiebelBusinessServiceException{
        OrderEntry order = this.convert.toBean(this.input, OrderEntry.class);
        List<Iinventory> inventories = (List)this.propertyChildToBeanList(this.input, Sales.class);
        
        Logging.logger(this.getClass()).log(Level.INFO, "{0}\n{1}", 
                new Object[]{order.toString(), inventories.toString()});
        
        EBSOrderManagementService orderManagement = 
                new EBSOrderManagementService(this.connection, order, inventories);
        
        Map<String, Object> results = orderManagement.createOrder();
        if(results != null)
            this.mapToPropertySet(results, this.output);
    }
     
    public void getInventoryId()
            throws SiebelException, SQLException, SiebelBusinessServiceException{
        OrderEntry order = this.convert.toBean(this.input, OrderEntry.class);
        List<Iinventory> inventories = (List)this.propertyChildToBeanList(this.input, Sales.class);
        
        Logging.logger(this.getClass()).log(Level.INFO, "{0}\n{1}", 
                new Object[]{order.toString(), inventories.toString()});
        
        EBSOrderManagementService orderManagement = 
                new EBSOrderManagementService(this.connection, order, inventories);
        
        Map<String, Object> results = orderManagement.getInventoryId();
        if(results != null)
            this.mapToPropertySet(results, this.output);
    }
    
    /**
     * 
     * @param input
     * @param partNumber
     * @param amount 
     */
    private void addExtraChildPropertySet(SiebelPropertySet input, String partNumber, Float amount) {
        if (partNumber != null && !"".equals(partNumber) && amount != null && amount > 0) {
            SiebelPropertySet propertySet = new SiebelPropertySet();
            propertySet.setProperty("Part Number", partNumber);
            propertySet.setProperty("Net Price", String.valueOf(amount));
            propertySet.setProperty("Quantity", "1");
            propertySet.setProperty("PLX Lot#", HelperAP.getLagosWarehouseId());
            input.addChild(propertySet);
        }
    }

    /**
     * 
     * @param <K>
     * @param <V>
     * @param results
     * @param output 
     */
    private <K, V> void mapToPropertySet(Map<K, V> results, SiebelPropertySet output) {
        for (Map.Entry<K, V> result : results.entrySet()) {
            output.setProperty(String.valueOf(result.getKey()), String.valueOf(result.getValue()));
        }
    }

    /**
     * 
     * @param <T>
     * @param propertySet
     * @param type
     * @return
     * @throws SiebelException 
     */
    private <T> List<T> propertyChildToBeanList(SiebelPropertySet propertySet, Class<? extends T> type) throws SiebelException {
        ArrayList<T> inventory = new ArrayList<T>();
        for (int i = 0; i < propertySet.getChildCount(); ++i) {
            SiebelPropertySet columnToProperty = propertySet.getChild(i);
            inventory.add(this.convert.toBean(columnToProperty, type));
        }
        return inventory;
    }
        
}

