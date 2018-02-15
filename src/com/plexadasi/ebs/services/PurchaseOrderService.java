/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.connect.ebs.EbsConnect;
import com.plexadasi.connect.siebel.SiebelConnect;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.CallableRunner;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ProcRunner;
import com.plexadasi.ebs.model.BackOrder;
import com.plexadasi.ebs.model.SalesOrder;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.naming.NamingException;
import oracle.jdbc.OracleTypes;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Uche Umeevuruo
 */
public class PurchaseOrderService {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;
    
    public PurchaseOrderService(Connection connection)
    {
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }
    
    public Map<Integer, Object> createSalesOrder(SalesOrderInventory salesOrder) throws SQLException, SiebelBusinessServiceException, SiebelException{
        
        CallableRunner prun = new CallableRunner();
	Map<Integer, Object> order = prun.queryProc(this.connection,
            "{CALL SALES_ORDER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", new Object[]
            {
                DataConverter.toInt(HelperAP.getEbsUserId()),
                DataConverter.toInt(HelperAP.getEbsUserResp()),
                salesOrder.getOrderId(), 
                salesOrder.getSoldToOrgId(),
                salesOrder.getShipToId(),
                salesOrder.getBillToId(),
                salesOrder.getSoldFromId(),
                salesOrder.getSalesRepId(),
                DataConverter.toInt(HelperAP.getPriceListID()),
                salesOrder.getTransactionCode(),
                salesOrder.getStatusCode(),
                salesOrder.getSiebelOrderId(),
                salesOrder.getSourceId(),
                salesOrder.inventory(SiebelConnect.connectSiebelServer(), this.connection).getInventoryItem(),
                new OutParameter(java.sql.Types.VARCHAR, Integer.class),
                new OutParameter(java.sql.Types.VARCHAR, Integer.class),
                new OutParameter(java.sql.Types.INTEGER, Integer.class)
            }
        );
        return order;
    }
    
    public void createReservation(Integer orderId) throws SQLException
    {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL ORDER_MGMT_RESERVATION(?,?,?,?)}";
        prun.queryProc(this.connection, this.query, 
            new Object[]{
                HelperAP.getEbsUserId(),
                HelperAP.getEbsUserResp(),
                HelperAP.getEbsAppId(),
                orderId
            }
        );
    }
    
    public void cancelSalesOrder(Integer headerId, Integer lineId) throws SQLException
    {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CANCEL_SALES_ORDER(?,?,?,?,?,?,?,?)}";
        
        prun.queryProc(this.connection, this.query,
            new Object[]{
                HelperAP.getEbsUserId(),
                HelperAP.getEbsUserResp(),
                HelperAP.getEbsAppId(),
                headerId
            }
        );
    }
    
    public void cancelSalesOrderLineItem(Integer headerId, Integer lineId, Integer quantity) throws SQLException
    {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CANCEL_LINE_ORDER(?,?,?,?,?,?,?,?,?)}";
        
        prun.queryProc(this.connection, this.query,
            new Object[]{
                HelperAP.getEbsUserId(),
                HelperAP.getEbsUserResp(),
                HelperAP.getEbsAppId(),
                headerId,
                lineId,
                quantity
            }
        );
    }
    
    public BackOrder findBackOrderedItem(SalesOrder salesOrder) throws SQLException
    {
        ResultSetHandler<BackOrder> rowMapper = new BeanHandler<BackOrder>(BackOrder.class);
        this.query = this.sqlBuilder.buildBackOrderSql().getQuery();
        BackOrder backOrder = this.jdbcTemplateObject.query(this.connection, this.query, rowMapper, 
            new Object[]{
                salesOrder.getOrderNumber(),
                salesOrder.getPartNumber(),
                salesOrder.getWarehouseId()
            }
        );
        return backOrder;
    }
    
    public SalesOrder findOnHandQuantity(SalesOrder salesOrder, String type) throws SQLException
    {
        ResultSetHandler<SalesOrder> rowMapper = new BeanHandler<SalesOrder>(SalesOrder.class);
        this.query = "SELECT XXMADN_GET_OHQTY(?, ?, ?) quantity FROM DUAL";
        SalesOrder onhand = this.jdbcTemplateObject.query(this.connection, this.query, rowMapper,
            new Object[]{
                salesOrder.getPartNumber(),
                salesOrder.getWarehouseId(),
                type
            }
        ); 
        return onhand != null ? onhand : new SalesOrder();
    }
    
    public List<SalesOrder> findOnHandQuantity (String partNumber, String location1, String location2) throws SQLException
    {
        ResultSetHandler<List<SalesOrder>> rowMapper = new BeanListHandler<SalesOrder>(SalesOrder.class);
        this.query = this.sqlBuilder.buildOnhandQuantitySql().getQuery();
        List<SalesOrder> onhand = this.jdbcTemplateObject.query(this.connection, this.query, rowMapper,
            new Object[]{
                partNumber,
                location1,
                location2
            }
        );
        return onhand != null ? onhand : new ArrayList<SalesOrder>();
    }
    
    public static void main(String[] args) throws SiebelBusinessServiceException, SQLException, SiebelException, NamingException
    {
        Connection ebs = EbsConnect.connectToEBSDatabase();
        SalesOrder salesOrder = new SalesOrder();
        PurchaseOrderService sos = new PurchaseOrderService(ebs);
        BillingAccountService billing = new BillingAccountService(ebs);
        SalesOrderInventory s = new SalesOrderInventory();
        int ebs_id = 51086;
        
        s.setSiebelOrderId("1-30474202");
        s.setOrderId(1001);//fixed
        s.setSoldToOrgId(ebs_id);//ebs customer id 35113
        //s.setShipToOrgId(34152);// site use id
        //s.setInvoiceId(34151);// site use id
        s.setBillToId(billing.findBillToCode(ebs_id));
        s.setShipToId(billing.findShipToCode(ebs_id));
        s.setSoldFromId(123);
        s.setSalesRepId(100000040);
        //s.setPriceId(9013);
        s.setTransactionCode("NGN");
        s.setStatusCode("ENTERED");
        s.setPurchaseOrderNumber("1-30474202");
        s.setSourceId(0);
        Map<Integer, Object> map = sos.createSalesOrder(s);
        for(Map.Entry<Integer, Object> entry : map.entrySet()){
            System.out.println(entry.getValue());
        }
        
        
        //salesOrder.setPartNumber("A9438202445");
        //salesOrder.setOrderNumber("1-28859582");
        //List<SalesOrder> s = sos.findOnHandQuantity(salesOrder.getPartNumber(), "123", "124");
        //for(SalesOrder salet : s){
          //  System.out.println(salet.getQuantity()+" "+salet.getWarehouseId()+"\\n");
        //}
        //BackOrder b = sos.findBackOrderedItem(salesOrder);
        //System.out.println(b.getPickMeaning());
        //System.out.println(DataConverter.nullToString(b.getItemStatus()));
        /*
        inputs.setProperty("partnumber", "A9438202445");
        inputs.setProperty("lotid", "123");
        inputs.setProperty("ordernumber", "1-28859582");*/
    }
}
