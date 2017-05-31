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
import com.plexadasi.ebs.SiebelApplication.bin.Order;
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrder;
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
import java.util.Map;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;


/**
 *
 * @author SAP Training
 */
public class PurchaseOrderInventory 
{
    private final StringWriter errors = new StringWriter();
    private static SiebelDataBean siebConn = null;
    private static Connection ebsConn = null;
    private static EBSSqlData ebsData = null;
    private static Order order = null;
    private static Account acc = null;
    private PurchaseOrder po = null;
    private String siebelOrderNumber;
    private String siebelOrderId;
    private String siebelAccountId;
    private String accountType;
    private String ebsId;
    private static String[] organization = new String[]{};
    private Array inventoryItem;
    private Integer sourceId;
    private String returnString;
    
    public PurchaseOrderInventory(SiebelDataBean s, Connection e) throws SiebelBusinessServiceException
    {
        siebConn = s;
        ebsConn = e;
        ebsData = new EBSSqlData(e);
        order = new Order(siebConn);
        po = new PurchaseOrder(siebConn);
    }
    
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
    
    public void setEbsId(String ebs_id)
    {
        ebsId = ebs_id;
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

    public SiebelDataBean getSiebelConnection() 
    {
        return siebConn;
    }

    public Connection getEbsConnection() 
    {
        return ebsConn;
    }

    public Integer getSourceId() 
    {
        return sourceId;
    }
    
    public String getShipToLocation() 
    {
        return acc.getProperty(Account.ACC_PRI_SHIP_TO_ADDR);
    }
    
    public String getBillToLocation()
    {
        return acc.getProperty(Account.ACC_PRI_BILL_TO_ADDR);
    }
    
    public String getCurrencyCode()
    {
        return acc.getProperty(Account.ACC_CURRENCY_CODE);
    }
    
    public Integer getAgentCode()
    {
        return DataConverter.toInt(order.getProperty(Product.PLX_AGENT_ID));
    }
    
    public String getOrganizationId() 
    {
        return organization[0] == null ? "" : organization[0];
    }
    
    public String getOrganizationCode()
    {
        System.out.println(String.valueOf(organization[1]));
        return organization[1] == null ? "" : organization[1];
    }
    
    public String getShipToAccount() throws SiebelBusinessServiceException
    {
        return ebsData.shipToAccount(DataConverter.toInt(ebsId));
    }
    
    private void triggerOrder() throws SiebelBusinessServiceException
    {
        order.setPropertySet(Product.FIELD_ORDER_NUMBER, Product.FIELD_ORDER_NUMBER);
        order.setPropertySet(Product.PLX_AGENT_ID, Product.PLX_AGENT_ID);
        order.setSiebelAccountId(siebelOrderId);
        order.doTrigger();
    }
    
    private void triggerOrderEntryInventory() throws SiebelBusinessServiceException
    {
        try 
        {
            siebelOrderNumber = order.getProperty(Product.FIELD_ORDER_NUMBER);
            MyLogging.log(Level.INFO, Product.FIELD_ORDER_NUMBER + ": " + siebelOrderNumber);
            po.setSiebelAccountId(siebelOrderNumber);
            po.doTrigger();
            ArrayDescriptor desc = ArrayDescriptor.createDescriptor("PRODUCT", ebsConn);
            int toSize  = po.getList().size();
            String[][] stringArray = new String[toSize][11];
            for (int i = 0; i < toSize; i++)
            {
                Map<String, String> map = po.getList().get(i);
                stringArray[i][0] = map.get(PurchaseOrder.FIELD_LINE_NUMBER);
                stringArray[i][1] = map.get(PurchaseOrder.SHIPMENT_NUMBER);
                stringArray[i][2] = HelperAP.getLineType();
                stringArray[i][3] = map.get(PurchaseOrder.PLX_PRODUCT);
                stringArray[i][4] = map.get(PurchaseOrder.PLX_UNIT_OF_MEASURE);
                stringArray[i][5] = map.get(PurchaseOrder.FIELD_QUANTITY);
                stringArray[i][6] = map.get(PurchaseOrder.PLX_UNIT_PRICE);
                stringArray[i][7] = getOrganizationCode();
                stringArray[i][8] = getOrganizationId();
                stringArray[i][9] = getShipToAccount();
                stringArray[i][10] = map.get(PurchaseOrder.PLX_QUANTITY_REQUESTED);
            }
            inventoryItem = new ARRAY(desc, ebsConn, stringArray);
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
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
    }
    
    public void triggers() throws SiebelBusinessServiceException
    {
        try 
        {
            organization = ebsData.orgCode(DataConverter.toInt(ebsId));
            triggetOrderAccount();
            triggerOrder();
            triggerOrderEntryInventory();
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
    }
    
    @Override
    public String toString() 
    {
        returnString = "";
        returnString += "\t\t[Siebel order number=" + siebelOrderNumber + "\n";
        returnString += "\t\t[Siebel order id=" + siebelOrderId + "\n";
        returnString += "\t\t[Siebel sccount id=" + siebelAccountId + "\n";
        returnString += "\t\t[Account type=" + accountType + "\n";
        returnString += "\t\t[Ebs id=" + ebsId + "\n";
        returnString += "\t\t[Organization id=" + organization[0] + "\n";
        returnString += "\t\t[Organization code=" + organization[1] + "\n";
        returnString += "\t\t[Source id=" + String.valueOf(sourceId) + "\n";
        return getClass().getSimpleName() + "\n[Details\n\t[\n" + returnString + "\t]\n";
    }
}