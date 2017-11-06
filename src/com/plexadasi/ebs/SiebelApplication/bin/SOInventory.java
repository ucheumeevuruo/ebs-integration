/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.Helper.DataConverter;
import com.plexadasi.ebs.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.order.SalesOrderInventory;
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
public class SOInventory {
    private SiebelDataBean siebConn = null;
    private Connection ebsConn = null;
    private Integer soldToOrgId = null;
    private String shipToOrgId = "";
    private Integer priceId = null;
    private String siebelOrderId = "";
    private EBSSqlData ebsData = null;
    private final String sqlName = "PRODUCT";
    private String[][] stringArray;
    private int length = 0;
    private final int maxLength = 4;
    
    public SOInventory(SiebelDataBean sb, Connection ebs, SalesOrderInventory sO)
    {
        siebConn = sb;
        ebsConn = ebs;
        soldToOrgId = sO.getSoldToOrgId();
        shipToOrgId = sO.getShipToOrgId();
        siebelOrderId = sO.getSiebelOrderId();
        ebsData = new EBSSqlData(ebs);
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
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor(sqlName, ebsConn);
        length = sales.getList().size();
        stringArray = new String[length][maxLength];
        for (int i = 0; i < length; i++) 
        {
            Map<String, String> map = sales.getList().get(i);
            String partNumber = map.get(SalesOrder.PLX_PART_NUMBER);
            Float itemPrice = Float.parseFloat(map.get(SalesOrder.PLX_ITEM_PRICE));
            String lot_id = (map.get(SalesOrder.PLX_LOT_ID));
            String product = map.get(SalesOrder.PLX_PRODUCT);
            priceId = DataConverter.toInt(HelperAP.getPriceListID());
            stringArray[i][0] = partNumber;
            stringArray[i][1] = map.get(SalesOrder.FIELD_QUANTITY);
            stringArray[i][2] = shipToOrgId;
            stringArray[i][3] = lot_id;
            boolean isUpdated = ebsData.updatePriceList(itemPrice, DataConverter.toInt(lot_id), (partNumber), priceId);
            if(isUpdated)
            {
                MyLogging.log(Level.INFO, "Inventory with name " + product + " updated successfully");
            }
            else
            {
                MyLogging.log(Level.INFO, "Inventory " + product + " does not exists in table QP_LIST_LINES");  
            }
        }
        return new ARRAY(desc, ebsConn, stringArray);
    }
}
