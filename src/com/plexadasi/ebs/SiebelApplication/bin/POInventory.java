/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.build.EBSSqlData;
import com.plexadasi.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.order.PurchaseOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 *
 * @author SAP Training
 */
public class POInventory {
    private PurchaseOrderInventory poOrder = null;
    private SiebelDataBean siebConn = null;
    private Connection ebsConn = null;
    private final String sqlName = "PRODUCT";
    private String[][] stringArray;
    private int length = 0;
    private final int maxLength = 12;
    
    public POInventory(SiebelDataBean sb, Connection ebs, PurchaseOrderInventory PO)
    {
        siebConn = sb;
        ebsConn = ebs;
        poOrder = PO;
    }
    
    /**
     * @return the inventoryItem
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.sql.SQLException
     */
    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException 
    {
        PurchaseOrder po = new PurchaseOrder(siebConn);
        po.setSiebelAccountId(poOrder.getOrderNumber());
        po.doTrigger();
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor(sqlName, ebsConn);
        length  = po.getList().size();
        stringArray = new String[length][maxLength];
        EBSSqlData ebsData = new EBSSqlData(ebsConn);
        for (int i = 0; i < length; i++)
        {
            Map<String, String> map = po.getList().get(i);
            String[] org = ebsData.getOrgCode(Integer.parseInt(map.get(PurchaseOrder.PLX_LOT_ID)));
            stringArray[i][0] = map.get(PurchaseOrder.FIELD_LINE_NUMBER);
            stringArray[i][1] = map.get(PurchaseOrder.SHIPMENT_NUMBER);
            stringArray[i][2] = HelperAP.getLineType();
            stringArray[i][3] = map.get(PurchaseOrder.PLX_PART_NUMBER);
            stringArray[i][4] = map.get(PurchaseOrder.PLX_UNIT_OF_MEASURE);
            stringArray[i][5] = map.get(PurchaseOrder.FIELD_QUANTITY);
            stringArray[i][6] = map.get(PurchaseOrder.PLX_UNIT_PRICE);
            stringArray[i][7] = org[0];
            stringArray[i][8] = org[1];
            stringArray[i][9] = org[2];
            stringArray[i][10] = poOrder.getPromiseDate();
            stringArray[i][11] = map.get(PurchaseOrder.PLX_QUANTITY_REQUESTED);
            MyLogging.log(Level.INFO, Arrays.toString(stringArray[i]));
        }
        return new ARRAY(desc, ebsConn, stringArray);
    }
}
