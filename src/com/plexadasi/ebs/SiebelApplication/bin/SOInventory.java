/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 *
 * @author SAP Training
 */
public class SOInventory {
    private SiebelDataBean siebConn = null;
    SalesOrderInventory so = null;
    private Connection ebsConn = null;
    //private Integer soldToOrgId = null;
    //private String shipToOrgId = "";
    private Integer priceId = null;
    //private String siebelOrderId = "";
    private EBSSqlData ebsData = null;
    private final String sqlName = "PRODUCT";
    private String[][] stringArray;
    private int length = 0;
    private final int maxLength = 4;
    
    public SOInventory(SiebelDataBean sb, Connection ebs, SalesOrderInventory sO)
    {
        siebConn = sb;
        ebsConn = ebs;
        this.so = sO;
        //soldToOrgId = sO.getSoldToOrgId();
        //shipToOrgId = sO.getShipToOrgId();
        //siebelOrderId = sO.getSiebelOrderId();
        ebsData = new EBSSqlData(ebs);
    }
    
    /**
     * @return the inventoryItem
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.sql.SQLException
     * @throws com.siebel.data.SiebelException
     */
    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException, SiebelException 
    {
        SalesOrderItem sales = new SalesOrderItem(siebConn);
        sales.setSiebelAccountId(this.so.getSiebelOrderId());
        sales.setOrgId(this.so.getSoldFromId());
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor(sqlName, ebsConn);
        List<Map<String, String>> inventoryItems = sales.inventoryItems(ebsConn);
        length = inventoryItems.size();
        int number = 0;
        stringArray = new String[length][maxLength];
        for(Map<String, String> inventoryItem : inventoryItems){
            for(Map.Entry<String,String> mapEntry : inventoryItem.entrySet()){
                stringArray[number][2] = ebsData.shipToAccount(this.so.getSoldToOrgId());
                stringArray[number][Integer.parseInt(mapEntry.getKey())] = mapEntry.getValue();
                number++;
            }
        }
        return new ARRAY(desc, ebsConn, stringArray);
    }
    
    public void updatePriceList() throws SiebelBusinessServiceException, SQLException, SiebelException
    {
        SalesOrderItem sales = new SalesOrderItem(siebConn);
        sales.setSiebelAccountId(this.so.getSiebelOrderId());
        sales.setOrgId(this.so.getSoldFromId());
        List<Map<String, String>> inventoryItems = sales.inventoryItems(ebsConn);
        int priceListId = DataConverter.toInt(HelperAP.getPriceListID());
        for(Map<String, String> inventoryItem : inventoryItems){
            String partNumber = inventoryItem.get(Product.PLX_PART_NUMBER);
            int warehouse = DataConverter.toInt(inventoryItem.get(Product.PLX_ITEM_PRICE));
            float price = Float.parseFloat(inventoryItem.get(Product.PLX_ITEM_PRICE));
            boolean isUpdated = ebsData.updatePriceList(price, warehouse, partNumber, priceListId);
        }
    }
}