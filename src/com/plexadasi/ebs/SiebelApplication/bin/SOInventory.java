/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 *  oracle.sql.ARRAY
 *  oracle.sql.ArrayDescriptor
 *  oracle.sql.STRUCT
 *  oracle.sql.StructDescriptor
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Inventory;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderItem;
import com.plexadasi.ebs.SiebelApplication.bin.VehicleSalesOrderItem;
import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

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

    public SOInventory(SiebelDataBean sb, Connection ebs, SalesOrderInventory sO) {
        this.siebConn = sb;
        this.ebsConn = ebs;
        this.so = sO;
        this.ebsData = new EBSSqlData(ebs);
    }

    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException, SiebelException {
        SalesOrderItem sales = new SalesOrderItem(this.siebConn);
        sales.setSiebelAccountId(this.so.getSiebelOrderId());
        sales.setOrgId(this.so.getSoldFromId());
        VehicleSalesOrderItem vehicleSalesOrderItem = new VehicleSalesOrderItem(this.siebConn);
        vehicleSalesOrderItem.setSiebelAccountId(this.so.getSiebelOrderId());
        vehicleSalesOrderItem.setOrgId(this.so.getSoldFromId());
        this.getClass();
        ArrayDescriptor desc = ArrayDescriptor.createDescriptor((String)"PRODUCT", (Connection)this.ebsConn);
        List<Map<String, String>> inventoryItems = sales.inventoryItems(this.ebsConn);
        inventoryItems.addAll(vehicleSalesOrderItem.inventoryItems(this.ebsConn));
        this.length = inventoryItems.size();
        int number = 0;
        this.getClass();
        this.stringArray = new String[this.length][4];
        String type = "Order";
        if(this.so.getTransactionCode().equalsIgnoreCase("NGN")){
            type = "WEST_ORDER";
        }
        for (Map<String, String> inventoryItem : inventoryItems) {
            this.stringArray[number][0] = inventoryItem.get("Part Number");
            this.stringArray[number][1] = inventoryItem.get("Quantity");
            this.stringArray[number][2] = String.valueOf(this.so.getShipToId());
            this.stringArray[number][2] = inventoryItem.get("Order Header Id");
            this.stringArray[number][3] = inventoryItem.get("Source Inventory Loc Integration Id");
            this.updatePriceList(inventoryItem, type);
            ++number;
        }
        MyLogging.log(Level.INFO, this.getClass().getSimpleName() + ":" + inventoryItems.toString());
        return new ARRAY(desc, this.ebsConn, (Object)this.stringArray);
    }

    public Array getSalesItems() throws SiebelException, SQLException {
        SalesOrderItem sales = new SalesOrderItem(this.siebConn);
        sales.setSiebelAccountId(this.so.getSiebelOrderId());
        sales.setOrgId(this.so.getSoldFromId());
        List<Map<String, String>> inventoryItems = sales.inventoryItems(this.ebsConn);
        STRUCT[] structArray = new STRUCT[inventoryItems.size()];
        Integer index = 0;
        for (Map<String, String> inventories : inventoryItems) {
            STRUCT genericStruct;
            Inventory inventory = new Inventory();
            inventory.setPart_number(inventories.get("Part Number"));
            inventory.setQuantity(DataConverter.toInt(inventories.get("Quantity")));
            inventory.setOrg_id(DataConverter.toInt(inventories.get("PLX Lot#")));
            inventory.setCust_po_number(this.so.getSiebelOrderId());
            Object[] structObj = new Object[]{inventory.getPart_number(), inventory.getOrg_id(), inventory.getQuantity(), inventory.getOrder_number(), inventory.getCust_po_number()};
            structArray[index.intValue()] = genericStruct = new STRUCT(StructDescriptor.createDescriptor((String)"SALES_ITEMS", (Connection)this.ebsConn), this.ebsConn, structObj);
            Integer n = index;
            Integer n2 = index = Integer.valueOf(index + 1);
        }
        return new ARRAY(ArrayDescriptor.createDescriptor((String)"SALES_ITEM", (Connection)this.ebsConn), this.ebsConn, (Object)structArray);
    }

    public void updatePriceList(Map<String, String> inventoryItem, String type) throws SiebelBusinessServiceException, SQLException, SiebelException {
        String partNumber = inventoryItem.get("Part Number");
        int warehouse = this.so.getSoldFromId();
        float price = DataConverter.toFloat(inventoryItem.get("Item Price"));
        Integer priceListId = DataConverter.toInt(HelperAP.getPriceListID());
        boolean isUpdated = this.ebsData.updatePriceList(price, warehouse, partNumber, type);
        MyLogging.log(Level.INFO, "Updated Status for " + partNumber + " " + isUpdated);
    }

    private Integer productKey(String key) {
        Integer productKey = 0;
        if (key.equalsIgnoreCase("Part Number")) {
            productKey = 0;
        } else if (key.equalsIgnoreCase("Quantity")) {
            productKey = 1;
        } else if (key.equalsIgnoreCase("PLX Lot#")) {
            productKey = 3;
        }
        return productKey;
    }
}

