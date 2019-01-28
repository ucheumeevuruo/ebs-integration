/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.plexadasi.connect.ebs.EbsConnect
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 *  org.apache.commons.dbutils.QueryRunner
 *  org.apache.commons.dbutils.ResultSetHandler
 *  org.apache.commons.dbutils.handlers.BeanHandler
 */
package com.plexadasi.ebs.services;

import com.plexadasi.build.SqlPreparedStatement;
import com.plexadasi.connect.ebs.EbsConnect;
import com.plexadasi.ebs.SiebelApplication.bin.POInventory;
import com.plexadasi.ebs.CallableRunner;
import com.plexadasi.ebs.model.Order;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.plexadasi.order.PurchaseOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.NamingException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class PurchaseOrderService {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;

    public PurchaseOrderService(Connection connection) {
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }

    public Map<String, Object> createPurchaseOrder(SiebelDataBean sdb, PurchaseOrderInventory poInventory) throws SQLException, SiebelBusinessServiceException, SiebelException {
        CallableRunner prun = new CallableRunner();
        Map<String, Object> order = prun.queryProc(this.connection, "{CALL PURCHASE_ORDER_TEST(?,?,?,?,?,?)}", 
            poInventory.getSourceId(), 
            poInventory.getShipToLocation(),
            poInventory.getCurrencyCode(), 
            poInventory.getAgentCode(), 
            poInventory.getOrderNumber(), 
            poInventory.inventory(sdb, this.connection).getInventoryItem()
        );
        return order;
    }

    public Order getBookingStatus(String orderId) throws SQLException {
        BeanHandler rowMapper = new BeanHandler(Order.class);
        this.query = this.sqlBuilder.buildPurchaseOrder().getQuery();
        Order purchaseOrder = (Order)this.jdbcTemplateObject.query(this.connection, this.query, (ResultSetHandler)rowMapper, new Object[]{orderId});
        return purchaseOrder == null ? new Order() : purchaseOrder;
    }

    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, NamingException {
        Connection ebs = EbsConnect.connectToEBSDatabase();
        PurchaseOrderService sos = new PurchaseOrderService(ebs);
        String ebs_id = "1-57452931";
        Order purchaseOrder = sos.getBookingStatus(ebs_id);
        System.out.println(purchaseOrder.toString());
    }
}

