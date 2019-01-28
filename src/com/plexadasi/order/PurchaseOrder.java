/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.PurchaseOrderService;
import com.plexadasi.order.PurchaseOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

public class PurchaseOrder {
    private Connection EBS_CONN = null;
    private SiebelDataBean SIEBEL_CONN = new SiebelDataBean();
    StringWriter errors = new StringWriter();

    public void doInvoke(PurchaseOrderInventory poInventory, SiebelDataBean sb, Connection ebs) throws SiebelBusinessServiceException {
        this.SIEBEL_CONN = sb;
        this.EBS_CONN = ebs;
        poInventory.triggers(this.SIEBEL_CONN, new EBSSqlData(this.EBS_CONN));
        Boolean IsProcessed = this.processIsNotNull(this.EBS_CONN, poInventory.getOrderNumber());
        if (!IsProcessed) {
            PurchaseOrderService purchaseOrderService = new PurchaseOrderService(ebs);
            try {
                purchaseOrderService.createPurchaseOrder(sb, poInventory);
            }
            catch (SQLException ex) {
                ex.printStackTrace(new PrintWriter(this.errors));
                throw new SiebelBusinessServiceException("SQL", "Caught Sql Exception:" + this.errors.toString());
            }
            catch (SiebelException ex) {
                ex.printStackTrace(new PrintWriter(this.errors));
                MyLogging.log(Level.SEVERE, "Caught Siebel Exception:" + this.errors.toString());
            }
        }
    }

    private Order getBookingStatus(Connection ebsConn, String order_num) {
        try {
            PurchaseOrderService purchaseOrderService = new PurchaseOrderService(ebsConn);
            return purchaseOrderService.getBookingStatus(order_num);
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught Siebel Exception:" + this.errors.toString());
            return new Order();
        }
    }

    private Boolean isPurchaseOrderProcessed(Connection ebsConn, String order_num) throws SiebelBusinessServiceException {
        return this.getBookingStatus(ebsConn, order_num).getProcessCode() != null;
    }

    private Boolean processIsNotNull(Connection ebsConn, String order_num) throws SiebelBusinessServiceException {
        return this.getBookingStatus(ebsConn, order_num).getProcessCode() != null;
    }

    public String getPONumber(Connection ebsConn, String order_num) throws SiebelBusinessServiceException {
        return this.getBookingStatus(ebsConn, order_num).getOrderNumber();
    }

    public String getPurchaseOrderNumber(Connection ebsConn, String order_num) throws SiebelBusinessServiceException {
        return String.valueOf(this.getBookingStatus(ebsConn, order_num).getId());
    }

    public String getPurchaseOrderBookingStatus(Connection ebsConn, String order_num) throws SiebelBusinessServiceException {
        if (this.isPurchaseOrderProcessed(ebsConn, order_num).booleanValue()) {
            return this.getBookingStatus(ebsConn, order_num).getStatus();
        }
        return "";
    }
}

