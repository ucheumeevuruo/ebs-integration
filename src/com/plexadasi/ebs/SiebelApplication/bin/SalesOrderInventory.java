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
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.Inventory;
import com.plexadasi.ebs.SiebelApplication.bin.SalesOrderItem;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public class SalesOrderInventory {
    private SiebelDataBean siebConn = null;
    private Connection connection = null;
    private final com.plexadasi.order.SalesOrderInventory salesOrder;

    public SalesOrderInventory(SiebelDataBean sb, Connection ebs, com.plexadasi.order.SalesOrderInventory salesOrder) {
        this.siebConn = sb;
        this.connection = ebs;
        this.salesOrder = salesOrder;
    }

    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException, SiebelException {
        SalesOrderItem product = new SalesOrderItem(this.siebConn);
        product.setSiebelAccountId(this.salesOrder.getSiebelOrderId());
        product.setOrgId(this.salesOrder.getSoldFromId());
        ArrayList<Inventory> salesOrderInventories = new ArrayList<Inventory>();
        for (Map<String, String> inventories : product.inventoryItems(this.connection)) {
            Inventory inventory = new Inventory();
            inventory.setPart_number(inventories.get("Part Number"));
            inventory.setQuantity(DataConverter.toInt(inventories.get("Quantity")));
            inventory.setOrg_id(this.salesOrder.getSoldFromId());
            inventory.setLine_type("LINE");
            inventory.setCust_po_number(this.salesOrder.getPurchaseOrderNumber());
            salesOrderInventories.add(inventory);
        }
        MyLogging.log(Level.INFO, this.getClass().getSimpleName() + ":" + salesOrderInventories.toString());
        return new ARRAY(ArrayDescriptor.createDescriptor((String)"SALES_ITEM", (Connection)this.connection), this.connection, (Object)this.createStructArray(salesOrderInventories, StructDescriptor.createDescriptor((String)"SALES_ITEMS", (Connection)this.connection), this.connection));
    }

    private STRUCT[] createStructArray(List<Inventory> inventory, StructDescriptor structDescriptor, Connection ebsConn) throws SQLException {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for (Inventory inventories : inventory) {
            STRUCT genericStruct;
            Object[] structObj = new Object[]{inventories.getPart_number(), inventories.getOrg_id(), inventories.getQuantity(), inventories.getOrder_number(), inventories.getCust_po_number()};
            structArray[index.intValue()] = genericStruct = new STRUCT(structDescriptor, ebsConn, structObj);
            Integer n = index;
            Integer n2 = index = Integer.valueOf(index + 1);
        }
        return structArray;
    }
}

