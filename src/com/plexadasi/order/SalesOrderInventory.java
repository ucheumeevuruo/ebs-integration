/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.ebs.SiebelApplication.bin.SOInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import java.sql.Connection;
import java.sql.SQLException;


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
    private String transactionCode;
    private String statusCode;
    private String purchaseOrderNumber;
    private Integer sourceId;
    private String siebelOrderId;
    private Integer billToId;
    private Integer shipToId;
    
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
    
    public SOInventory inventory(SiebelDataBean sb, Connection ebsConn) throws SiebelException, SQLException
    {
        return new SOInventory(sb, ebsConn, this);
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(Integer sourceId)
    {
        this.sourceId = sourceId;
    }
    
    @Override
    public String toString() 
    {
        String output = "";
        output += "\t\t[Siebel order number=" + siebelOrderId + "]\n";
        output += "\t\t[Order type id=" + orderId + "]\n";
        output += "\t\t[Sold to org id=" + soldToOrgId + "]\n";
        //output += "\t\t[Ship to org id=" + shipToOrgId + "]\n";
        //output += "\t\t[Invoice id=" + invoiceId + "]\n";
        output += "\t\t[Sold from id=" + soldFromId + "]\n";
        output += "\t\t[Sales rep id=" + salesRepId + "]\n";
        //output += "\t\t[Price id=" + priceId + "]\n";
        output += "\t\t[Transaction code=" + transactionCode + "]\n";
        output += "\t\t[Status code=" + statusCode + "]\n";
        output += "\t\t[Purchase order number=" + purchaseOrderNumber + "]\n";
        output += "\t\t[Source id=" + sourceId + "]\n";
        return getClass().getSimpleName() + "\n[Details\n\t[\n" + output + "\t]\n";
    }

    public int getBillToId() {
        return this.billToId;
    }

    public int getShipToId() {
        return this.shipToId;
    }

    public void setShipToId(Integer shipToId) {
        this.shipToId = shipToId;
    }

    public void setBillToId(Integer billToId) {
        this.billToId = billToId;
    }
}