/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.item;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.EBSSql;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.PLXBackOrder;
import com.plexadasi.ebs.SiebelApplication.bin.QuotePartItem;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderItem;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderItem_1;
import com.plexadasi.ebs.SiebelApplication.bin.VehicleSalesOrderItem;
import com.plexadasi.ebs.model.Product;
import com.plexadasi.ebs.services.ProductService;
import com.plexadasi.order.Invoice;
import com.plexadasi.order.SalesOrder;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

public class Items {
    private Connection EBS_CONN = null;
    private SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    private static final StringWriter ERROR = new StringWriter();
    private EBSSql ebsSql = null;
    private int orgId;
    private int invId;

    public Items(SiebelDataBean sb, Connection ebs) throws SiebelBusinessServiceException {
        this.SIEBEL_CONN = sb;
        this.EBS_CONN = ebs;
        if (this.SIEBEL_CONN == null) {
            MyLogging.log(Level.SEVERE, "Connection to siebel cannot be established.");
            throw new SiebelBusinessServiceException("NULL_DEF", "Connection to siebel cannot be established.");
        }
        if (this.EBS_CONN == null) {
            MyLogging.log(Level.SEVERE, "Connection to ebs cannot be established.");
            throw new SiebelBusinessServiceException("NULL_DEF", "Connection to ebs cannot be established.");
        }
        if (this.ebsSql == null) {
            this.ebsSql = new EBSSql(this.EBS_CONN);
        }
    }

    public Product CreateItem(String desc, String part) throws SiebelBusinessServiceException {
        Product item = null;
        try {
            ProductService service = new ProductService(this.SIEBEL_CONN, this.EBS_CONN);
            Product product = new Product();
            product.setPartNumber(part);
            product.setDescription(desc);
            product.setOrganizationCode(HelperAP.getMasterOrganizationCode());
            item = service.createProduct(product);
            MyLogging.log(Level.INFO, String.valueOf(item.getId()));
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        }
        return item;
    }

    public void AssignItem(Integer invId, String orgId) throws SiebelBusinessServiceException {
        try {
            ProductService service = new ProductService(this.SIEBEL_CONN, this.EBS_CONN);
            Product product = new Product();
            product.setId(invId);
            product.setOrganizationId(DataConverter.toInt(orgId));
            service.assignProduct(product);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, ERROR.toString());
            throw new SiebelBusinessServiceException("SQL", ex.getMessage());
        }
    }

    public void setOnHandQuantity(SiebelPropertySet inputs) throws SiebelBusinessServiceException {
        try {
            String type = inputs.getProperty("Type");
            String Id2 = inputs.getProperty("Id");
            String warehouse = inputs.getProperty("Warehouse");
            SalesOrderItem s = new SalesOrderItem_1(this.SIEBEL_CONN);
            if ("Quote".equals(type)) {
                s = new QuotePartItem(this.SIEBEL_CONN);
            }
            s.setSiebelAccountId(Id2);
            s.setOrgId(Integer.parseInt(warehouse));
            s.onHandQuantities(this.EBS_CONN, "ATT");
        }
        catch (SiebelException ex) {
            ex.printStackTrace(new PrintWriter(ERROR));
            MyLogging.log(Level.SEVERE, this.getClass().getSimpleName() + ERROR.toString());
            throw new SiebelBusinessServiceException("SIEBEL", ex.getMessage());
        }
        catch (SQLException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SQL", "Cannot process your request. Please try again.");
        }
    }

    public void returnSalesOrder(SiebelPropertySet inputs) throws SiebelBusinessServiceException {
        SalesOrderInventory salesOrderInventory = new SalesOrderInventory();
        SalesOrder salesOrder = new SalesOrder(this.SIEBEL_CONN, this.EBS_CONN);
        salesOrderInventory.setPurchaseOrderNumber(inputs.getProperty("order_number"));
        salesOrderInventory.setSalesRepId(DataConverter.toInt(inputs.getProperty("sales_rep_id")));
        salesOrderInventory.setSoldToOrgId(DataConverter.toInt(inputs.getProperty("ebs_customer_id")));
        salesOrderInventory.setSiebelOrderId(inputs.getProperty("order_number"));
        salesOrderInventory.setSoldFromId(DataConverter.toInt(inputs.getProperty("warehouse_location_id")));
        salesOrderInventory.setTransactionCode(inputs.getProperty("currency_code"));
        salesOrderInventory.setType("return_order");
        salesOrder.returnSalesOrder(salesOrderInventory);
    }

    public void setBackOrderStatus(SiebelPropertySet inputs) throws SiebelBusinessServiceException {
        try {
            String Id2 = inputs.getProperty("Id");
            String warehouse = inputs.getProperty("Warehouse");
            SalesOrderItem s = new SalesOrderItem_1(this.SIEBEL_CONN);
            String getBC = inputs.getProperty("Type");
            if ("PLXBackOrder".equals(getBC)) {
                s = new PLXBackOrder(this.SIEBEL_CONN);
            }
            s.setSiebelAccountId(Id2);
            s.setOrgId(DataConverter.toInt(warehouse));
            s.backOrder(this.EBS_CONN);
        }
        catch (SQLException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SIEBEL", ex.getMessage());
        }
        catch (SiebelException ex) {
            MyLogging.log(Level.SEVERE, ex.getMessage());
            throw new SiebelBusinessServiceException("SQL", "Cannot process your request. Please try again.");
        }
    }

    public void setOrderStatus(SiebelPropertySet inputs, SiebelPropertySet output) throws SiebelBusinessServiceException {
        EBSSqlData ebsdata = new EBSSqlData(this.EBS_CONN);
        Map<String, String> backorder = ebsdata.backOrder(inputs.getProperty("partnumber"), DataConverter.toInt(inputs.getProperty("lotid")), inputs.getProperty("ordernumber"));
        for (Map.Entry<String, String> backorderItems : backorder.entrySet()) {
            output.setProperty(backorderItems.getKey(), backorderItems.getValue());
        }
    }

    public void createInvoice(SiebelPropertySet input, SiebelPropertySet output) throws SQLException, SiebelBusinessServiceException, SiebelException {
        Invoice ebs = new Invoice(this.EBS_CONN);
        ebs.addExtraChildPropertySet(input, HelperAP.getFreightCharges(), DataConverter.toFloat(input.getProperty("freight")));
        ebs.addExtraChildPropertySet(input, HelperAP.getSundries(), DataConverter.toFloat(input.getProperty("supplies_sundries")));
        ebs.addExtraChildPropertySet(input, HelperAP.getWithholdingTax(), DataConverter.toFloat(input.getProperty("withholding_tax")));
        ebs.addExtraChildPropertySet(input, HelperAP.getComputerProgramming(), DataConverter.toFloat(input.getProperty("computer_programming")));
        ebs.addExtraChildPropertySet(input, HelperAP.getDeliveryCharges(), DataConverter.toFloat(input.getProperty("delivery_charges")));
        ebs.create(input, output);
        Integer custTrxId = DataConverter.toInt(output.getProperty("12"));
        ebs.update(custTrxId, input);
    }

    public Integer getOrganizationId() {
        return this.orgId;
    }

    public Integer getInventoryId() {
        return this.invId;
    }
}

