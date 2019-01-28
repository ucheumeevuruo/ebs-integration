/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.order;

import com.plexadasi.helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.services.SalesOrderService;
import com.plexadasi.siebel.Iinventory;
import com.plexadasi.siebel.model.OrderEntry;
import com.plexadasi.siebel.model.Sales;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class Invoice
extends OrderManagement_OLD {
    public static final String DEFAULT_QUANTITY = "1";
    public static final String METHOD_NAME = "CreateInvoice";

    public Invoice(Connection connection) {
        super(connection);
    }

    @Override
    public void create(SiebelPropertySet input, SiebelPropertySet output) throws SQLException, SiebelBusinessServiceException, SiebelException {
        SalesOrderService sos = new SalesOrderService(this.connection);
        OrderEntry invoice = new OrderEntry();//this.propertyToBean(input, OrderEntry.class);
        List<Iinventory> inventory = (List)this.propertyChildToBeanList(input, Sales.class);
        this.addExtraChildPropertySet(input, HelperAP.getFreightCharges(), invoice.getFreight());
        this.addExtraChildPropertySet(input, HelperAP.getSundries(), invoice.getSundries());
        this.addExtraChildPropertySet(input, HelperAP.getWithholdingTax(), invoice.getTax());
        this.addExtraChildPropertySet(input, HelperAP.getComputerProgramming(), invoice.getComputerProgramming());
        this.addExtraChildPropertySet(input, HelperAP.getDeliveryCharges(), invoice.getDeliveryCharges());
        MyLogging.log(Level.INFO, invoice.toString() + inventory.toString());
        Map<String, Object> results = sos.createInvoice(invoice, inventory);
        for (Map.Entry<String, Object> result : results.entrySet()) {
            output.setProperty(String.valueOf(result.getKey()), String.valueOf(result.getValue()));
        }
    }

    public Boolean update(Integer transactionId, SiebelPropertySet input) throws SQLException, SiebelException {
        SalesOrderService sos = new SalesOrderService(this.connection);
        OrderEntry invoice = new OrderEntry();//this.propertyToBean(input, OrderEntry.class);
        int isUpdated = sos.updateInvoice(transactionId, invoice);
        return isUpdated > 0;
    }

    public void addExtraChildPropertySet(SiebelPropertySet input, String partNumber, Float amount) {
        if (partNumber != null && !"".equals(partNumber) && amount != null && amount > 0) {
            SiebelPropertySet propertySet = new SiebelPropertySet();
            propertySet.setProperty("Part Number", partNumber);
            propertySet.setProperty("Net Price", String.valueOf(amount));
            propertySet.setProperty("Quantity", "1");
            propertySet.setProperty("PLX Lot#", HelperAP.getLagosWarehouseId());
            input.addChild(propertySet);
        }
    }

    @Override
    public Boolean match(String MethodName) {
        return MethodName.equals("CreateInvoice");
    }
}

