/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.Helper.DataConverter;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
    private Integer priceId = null;
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
        SalesOrderItem sales = new SalesOrderItem(this.siebConn);
        sales.setSiebelAccountId(this.so.getSiebelOrderId());
        sales.setOrgId(this.so.getSoldFromId());
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor(this.sqlName, this.ebsConn);
        List<Map<String, String>> inventoryItems = sales.inventoryItems(this.ebsConn);
        this.length = inventoryItems.size();
        int number = 0;
        this.stringArray = new String[this.length][this.maxLength];
        for(Map<String, String> inventoryItem : inventoryItems){
            /*
            for(Map.Entry<String,String> mapEntry : inventoryItem.entrySet()){
                //this.stringArray[number][this.productKey(mapEntry.getKey())] = mapEntry.getValue();
                //this.stringArray[number][2] = String.valueOf(this.so.getShipToId());
            }*/
            this.stringArray[number][0] = inventoryItem.get(Product.PLX_PART_NUMBER);
            this.stringArray[number][1] = inventoryItem.get(Product.FIELD_QUANTITY);
            this.stringArray[number][2] = String.valueOf(this.so.getShipToId());
            this.stringArray[number][3] = inventoryItem.get(Product.PLX_LOT_ID);
            this.updatePriceList(inventoryItem);
            number++;
        }
        MyLogging.log(Level.INFO, getClass().getSimpleName()+":"+inventoryItems.toString());
        return new ARRAY(desc, this.ebsConn, this.stringArray);
    }
    
    public void updatePriceList(Map<String, String> inventoryItem) throws SiebelBusinessServiceException, SQLException, SiebelException
    {
        String partNumber = inventoryItem.get(Product.PLX_PART_NUMBER);
        int warehouse = DataConverter.toInt(inventoryItem.get(Product.PLX_LOT_ID));
        float price = Float.parseFloat(inventoryItem.get(Product.PLX_ITEM_PRICE));
        Integer priceListId = DataConverter.toInt(HelperAP.getPriceListID());
        boolean isUpdated = ebsData.updatePriceList( price, warehouse, partNumber, priceListId);
        MyLogging.log(Level.INFO, "Updated Status for "+partNumber+" "+isUpdated);
    }
    
    private Integer productKey(String key){
        Integer productKey = 0;
        if(key.equalsIgnoreCase(Product.PLX_PART_NUMBER))
            productKey = 0;
        else if(key.equalsIgnoreCase(Product.FIELD_QUANTITY))
            productKey = 1;
        else if(key.equalsIgnoreCase(Product.PLX_LOT_ID))
            productKey = 3;
        else{
            
        }
        return productKey;
    }
}