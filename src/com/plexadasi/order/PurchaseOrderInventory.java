/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.order;

import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Order;
import com.plexadasi.ebs.SiebelApplication.bin.POInventory;
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrderIndividual;
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrderOrganization;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;


/**
 *
 * @author SAP Training
 */
public class PurchaseOrderInventory 
{
    private final StringWriter errors = new StringWriter();
    private static SiebelDataBean siebConn = null;
    private static EBSSqlData ebsData = null;
    private static Order order = null;
    private static Account acc = null;
    private String siebelOrderNumber;
    private String siebelOrderId;
    private String siebelAccountId;
    private String accountType;
    private String[] organization = new String[]{};
    private Array inventoryItem;
    private Integer sourceId;
    private String returnString;
    
    public void setSiebelOrderId(String orderId) 
    {
        siebelOrderId = orderId;
    }

    /**
     * @param accountId the siebelAccountId to set
     */
    public void setSiebelAccountId(String accountId) {
        siebelAccountId = accountId;
    }

    public void setSourceId(int string) {
        sourceId = string;
    }

    /**
     * @param inventoryItem the inventoryItem to set
     */
    public void setInventoryItem(Array inventoryItem)
    {
        this.inventoryItem = inventoryItem;
    }

    /**
     * @param type the type to set
     */
    public void setAccountType(String type) 
    {
        accountType = type;
    }

    /**
     * @return the siebelAccountId
     */
    public String getSiebelAccountId() {
        return siebelAccountId;
    }
    
    public String getSiebelOrderId()
    {
        return siebelOrderId;
    }
    
    public String getOrderNumber()
    {
        return order.getProperty(Product.FIELD_ORDER_NUMBER);
    }

    /**
     * @return the inventoryItem
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.sql.SQLException
     */
    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException 
    {
        return inventoryItem;
    }
    
    /**
     * @return the accountType
     */
    public String getAccountType() 
    {
        return accountType;
    }

    public Integer getSourceId() 
    {
        return sourceId;
    }
    
    public String getShipToLocation() 
    {
        return acc.getProperty(Account.ACC_PRI_SHIP_TO_ADDR);
    }
    
    public String getShipToCity()
    {
        return acc.getProperty(Account.ACC_SHIP_TO_PRI_CITY_FIELD);
    }
    
    public String getShipToState()
    {
        return acc.getProperty(Account.ACC_BILL_TO_PRI_STATE_FIELD);
    }
    
    public String getShipToCountry()
    {
        return acc.getProperty(Account.ACC_SHIP_TO_PRI_COUNTRY_FIELD);
    }
    
    public String getBillToLocation()
    {
        return acc.getProperty(Account.ACC_PRI_BILL_TO_ADDR);
    }
    
    public String getBillToCity()
    {
        return acc.getProperty(Account.ACC_BILL_TO_PRI_CITY_FIELD);
    }
    
    public String getBillToState()
    {
        return acc.getProperty(Account.ACC_BILL_TO_PRI_STATE_FIELD);
    }
    
    public String getBillToCountry()
    {
        return acc.getProperty(Account.ACC_BILL_TO_PRI_COUNTRY_FIELD);
    }
    
    public String getCurrencyCode()
    {
        return acc.getProperty(Account.ACC_CURRENCY_CODE);
    }
    
    public Integer getAgentCode()
    {
        return DataConverter.toInt(order.getProperty(Product.PLX_AGENT_ID));
    }
    
    public String getEbsId()
    {
        return acc.getProperty(Account.ACC_EBS_ID);
    }
    
    public String getOrganizationId() 
    {
        return organization[0];
    }
    
    public String getOrganizationCode()
    {
        return organization[1];
    }
    
    public String getShipToAccount() throws SiebelBusinessServiceException
    {
        return ebsData.shipToAccount(DataConverter.toInt(getEbsId()));
    }
    
    public POInventory inventory(SiebelDataBean sb, Connection ebs)
    {
        return new POInventory(sb, ebs, this);
    }
    
    private void triggerOrder() throws SiebelBusinessServiceException
    {
        order = new Order(siebConn);
        order.setPropertySet(Product.FIELD_ORDER_NUMBER, Product.FIELD_ORDER_NUMBER);
        order.setPropertySet(Product.PLX_AGENT_ID, Product.PLX_AGENT_ID);
        order.setSiebelAccountId(siebelOrderId);
        order.doTrigger();
    }
    
    private void triggetOrderAccount() throws SiebelBusinessServiceException
    {
        if(accountType.equalsIgnoreCase("individual"))
        {
            acc = new PurchaseOrderIndividual(siebConn);
        }
        else if (accountType.equalsIgnoreCase("organization"))
        {
            acc = new PurchaseOrderOrganization(siebConn);
        }
        acc.setSiebelAccountId(siebelAccountId);
        acc.doTrigger();
        try 
        {
            organization = ebsData.orgCode(DataConverter.toInt(getEbsId()));
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    public PurchaseOrderInventory triggers(SiebelDataBean sb, EBSSqlData ed) throws SiebelBusinessServiceException
    {
        siebConn = sb;
        ebsData = ed;
        triggetOrderAccount();
        triggerOrder();
        return this;
    }
    
    @Override
    public String toString() 
    {
        returnString = "";
        returnString += "\t\t[Siebel order number=" + siebelOrderNumber + "\n";
        returnString += "\t\t[Siebel order id=" + siebelOrderId + "\n";
        returnString += "\t\t[Siebel sccount id=" + siebelAccountId + "\n";
        returnString += "\t\t[Account type=" + accountType + "\n";
        returnString += "\t\t[Ebs id=" + getEbsId()+ "\n";
        returnString += "\t\t[Organization id=" + organization[0] + "\n";
        returnString += "\t\t[Organization code=" + organization[1] + "\n";
        returnString += "\t\t[Source id=" + String.valueOf(sourceId) + "\n";
        return getClass().getSimpleName() + "\n[Details\n\t[\n" + returnString + "\t]\n";
    }
}