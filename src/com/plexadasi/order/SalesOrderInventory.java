/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.ebs.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrder;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;


/**
 *
 * @author SAP Training
 */
public class SalesOrderInventory {

    private Integer orderId;
    private Integer soldToOrgId;
    private String shipToOrgId;
    private Integer invoiceId;
    private Integer soldFromId;
    private Integer salesRepId;
    private Integer priceId;
    private String transactionCode;
    private String statusCode;
    private String purchaseOrderNumber;
    private Integer sourceId;
    private Array inventoryItem;
    private SiebelDataBean siebConn = null;
    private Connection ebsConn = null;
    private String siebelOrderId;
    EBSSqlData ebsData = null;
    
    public SalesOrderInventory(SiebelDataBean s, Connection e)
    {
        siebConn = s;
        ebsConn = e;
        ebsData = new EBSSqlData(e);
    }
    
    public void setSiebelOrderId(String orderId) 
    {
        siebelOrderId = orderId;
    }
    
    public String getSiebelOrderId()
    {
        return siebelOrderId;
    }
    /**
     * @return the soldToOrgId
     */
    public Integer getSoldToOrgId() 
    {
        return soldToOrgId;
    }

    /**
     * @param soldToOrgId the soldToOrgId to set
     */
    public void setSoldToOrgId(Integer soldToOrgId) {
        this.soldToOrgId = soldToOrgId;
    }

    /**
     * @return the orderId
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Integer orderId) 
    {
        this.orderId = orderId;
    }

    /**
     * @return the shipToOrgId
     */
    public String getShipToOrgId() 
    {
        return shipToOrgId;
    }

    /**
     * @param shipToOrgId the shipToOrgId to set
     */
    public void setShipToOrgId(Integer shipToOrgId) 
    {
        //this.shipToOrgId = shipToOrgId;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() 
    {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the soldFromId
     */
    public Integer getSoldFromId() 
    {
        return soldFromId;
    }

    /**
     * @param soldFromId the soldFromId to set
     */
    public void setSoldFromId(Integer soldFromId)
    {
        this.soldFromId = soldFromId;
    }

    /**
     * @return the salesRepId
     */
    public Integer getSalesRepId() 
    {
        return salesRepId;
    }

    /**
     * @param salesRepId the salesRepId to set
     */
    public void setSalesRepId(Integer salesRepId)
    {
        this.salesRepId = salesRepId;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() 
    {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    /**
     * @return the transactionCode
     */
    public String getTransactionCode() 
    {
        return transactionCode;
    }

    /**
     * @param transactionCode the transactionCode to set
     */
    public void setTransactionCode(String transactionCode)
    {
        this.transactionCode = transactionCode;
    }

    /**
     * @return the purchaseOrderNumber
     */
    public String getPurchaseOrderNumber() 
    {
        return purchaseOrderNumber;
    }

    /**
     * @param purchaseOrderNumber the purchaseOrderNumber to set
     */
    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    /**
     * @return the sourceId
     */
    public Integer getSourceId() 
    {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(Integer sourceId)
    {
        this.sourceId = sourceId;
    }

    /**
     * @return the inventoryItem
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.sql.SQLException
     */
    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException 
    {
        SalesOrder sales = new SalesOrder(siebConn);
        sales.setSiebelAccountId(siebelOrderId);
        shipToOrgId = ebsData.shipToAccount(soldToOrgId);
        sales.doTrigger();
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor("PRODUCT", ebsConn);
        int toSize  = sales.getList().size();
        String[][] stringArray = new String[toSize][3];
        for (int i = 0; i < toSize; i++) 
        {
            Map<String, String> map = sales.getList().get(i);
            String inventoryId = map.get(SalesOrder.PLX_INVENTORY);
            Integer itemPrice = DataConverter.toInt(map.get(SalesOrder.PLX_ITEM_PRICE));
            Integer lot_id = DataConverter.toInt(map.get(SalesOrder.PLX_LOT_ID));
            String product = map.get(SalesOrder.PLX_PRODUCT);
            priceId = DataConverter.toInt(HelperAP.getPriceListID());
            stringArray[i][0] = inventoryId;
            stringArray[i][1] = map.get(SalesOrder.PLX_QUANTITY);
            stringArray[i][2] = shipToOrgId;
            boolean isUpdated = ebsData.updatePriceList(itemPrice, lot_id, DataConverter.toInt(inventoryId), priceId);
            if(isUpdated)
            {
                MyLogging.log(Level.INFO, "Inventory with name " + product + " updated successfully");
            }
            else
            {
                MyLogging.log(Level.INFO, "Inventory " + product + " does not exists in table QP_LIST_LINES");  
            }
        }
        inventoryItem = new ARRAY(desc, ebsConn, stringArray);
        return inventoryItem;
    }

    /**
     * @param inventoryItem the inventoryItem to set
     */
    public void setInventoryItem(Array inventoryItem)
    {
        this.inventoryItem = inventoryItem;
    }
    
    @Override
    public String toString() 
    {
        String output = "";
        output += "\t\t[Siebel order number=" + siebelOrderId + "]\n";
        output += "\t\t[Order type id=" + orderId + "]\n";
        output += "\t\t[Sold to org id=" + soldToOrgId + "]\n";
        output += "\t\t[Ship to org id=" + shipToOrgId + "]\n";
        output += "\t\t[Invoice id=" + invoiceId + "]\n";
        output += "\t\t[Sold from id=" + soldFromId + "]\n";
        output += "\t\t[Sales rep id=" + salesRepId + "]\n";
        output += "\t\t[Price id=" + priceId + "]\n";
        output += "\t\t[Transaction code=" + transactionCode + "]\n";
        output += "\t\t[Status code=" + statusCode + "]\n";
        output += "\t\t[Purchase order number=" + purchaseOrderNumber + "]\n";
        output += "\t\t[Source id=" + sourceId + "]\n";
        return getClass().getSimpleName() + "\n[Details\n\t[\n" + output + "\t]\n";
    }

    public SiebelDataBean getSiebelConnection() 
    {
        return siebConn;
    }

    public Connection getEbsConnection() 
    {
        return ebsConn;
    }
}