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
import com.plexadasi.ebs.SiebelApplication.SiebelServiceClone;
import com.plexadasi.ebs.SiebelApplication.lang.enu.Columns;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.SalesOrderService;
import com.plexadasi.helper.DataConverter;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class QuotePartItem
extends SalesOrderItem {
    private static final String BUS_OBJ = "Quote";
    private static final String BUS_COMP = "Quote Item";
    Order quote;

    public QuotePartItem(SiebelDataBean CONN) {
        super(CONN);
        this.busComp = "Quote Item";
        this.busObj = "Quote";
    }

    @Override
    public void onHandQuantities(Connection ebsConn, String type) throws SiebelBusinessServiceException, SiebelException, SQLException {
        SiebelServiceClone s = new SiebelServiceClone(CONN);
        SalesOrderService sos = new SalesOrderService(ebsConn);
        SiebelPropertySet values = CONN.newPropertySet();
        this.set.setProperty(Columns.Quote.ORDER_NUMBER, Columns.Quote.ORDER_NUMBER);
        this.set.setProperty(Columns.Quote.PART_NUMBER, Columns.Quote.PART_NUMBER);
        this.set.setProperty("PLX Usage Asset Name", "PLX Usage Asset Name");
        this.set.setProperty("Product Type", "Product Type");
        this.set.setProperty("Source Inventory Location Integration Id", "Source Inventory Location Integration Id");
        s.setSField(this.set);
        SiebelBusComp sbBC = s.fields(this.busObj, this.busComp, this).getBusComp();
        Boolean isRecord = sbBC.firstRecord();
        while (isRecord) {
            sbBC.getMultipleFieldValues(this.set, values);
            
            String orderNumber = values.getProperty(Columns.Quote.ORDER_NUMBER);
            String partNumber = values.getProperty(Columns.Quote.PART_NUMBER);
            String assetNumber = values.getProperty("PLX Usage Asset Name");
            String productType = values.getProperty("Product Type");
            String warehouse = values.getProperty("Source Inventory Location Integration Id");
            if(productType.equalsIgnoreCase("Vehicle")){
                partNumber = assetNumber;
            }
            
            this.quote = new Order();
            this.quote.setOrderNumber(orderNumber);
            this.quote.setPartNumber(partNumber);
            this.quote.setWarehouseId(DataConverter.toInt(warehouse));
            
            Order salesOrderService = sos.findOnHandQuantity(this.quote, type);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            if(salesOrderService.getQuantity() != null) {
                Integer quantity = salesOrderService.getQuantity();
                this.quote.setQuantity(quantity);
                String status = "Out Of Stock";
                String date = "";
                if(quantity > 0){
                    status = "Available";
                    date = String.valueOf(dateFormat.format(new Date()));
                }
                sbBC.setFieldValue("Available Date", date);
                sbBC.setFieldValue("Available Quantity", String.valueOf(quantity));
                sbBC.setFieldValue("ATP Status", status);
                MyLogging.log(Level.INFO, this.quote.toString());
            }
            else{
                // empty catch block
                sbBC.setFieldValue("Available Quantity", "0");
                sbBC.setFieldValue("Available Date", "");
                //sbBC.setFieldValue("Available Date", String.valueOf(dateFormat.format(new Date())));
                sbBC.setFieldValue("ATP Status", "Unavailable");
                MyLogging.log(Level.INFO, this.quote.toString());
                
            }
            isRecord = sbBC.nextRecord();
        }
        sbBC.writeRecord();
        s.release();
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId);
        //sbBC.setSearchSpec("Product Type", "Equipment");
        //sbBC.setSearchSpec("Lot#", String.valueOf(this.soldToOrgId));
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

