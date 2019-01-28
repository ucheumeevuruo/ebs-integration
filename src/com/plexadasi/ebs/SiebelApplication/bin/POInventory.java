/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
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
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrder;
import com.plexadasi.order.PurchaseOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public class POInventory {
    private PurchaseOrderInventory poOrder = null;
    private SiebelDataBean siebConn = null;
    private Connection connection = null;

    public POInventory(SiebelDataBean sb, Connection ebs, PurchaseOrderInventory PO) {
        this.siebConn = sb;
        this.connection = ebs;
        this.poOrder = PO;
    }

    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException {
        PurchaseOrder.PartListItems purchaseOrder = new PurchaseOrder(this.siebConn).getPartQuoteItem();
        purchaseOrder.setSiebelAccountId(this.poOrder.getOrderNumber());
        //purchaseOrder.setWarehouse(DataConverter.toInt(this.poOrder.getShipToLocation()));
        List<Inventory> inventory = purchaseOrder.getInventories(this.connection);
        MyLogging.log(Level.INFO, this.getClass().getSimpleName() + ":" + inventory);
        return new ARRAY(ArrayDescriptor.createDescriptor((String)"PURCHASE_ORDER_ITEM", (Connection)this.connection), this.connection, (Object)this.createStructArray(inventory, StructDescriptor.createDescriptor((String)"PURCHASE_ORDER_ITEMS", (Connection)this.connection), this.connection));
    }

    private STRUCT[] createStructArray(List<Inventory> inventory, StructDescriptor structDescriptor, Connection connection) throws SQLException {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for (Inventory inventories : inventory) {
            STRUCT genericStruct;
            Object[] structObj = new Object[]{inventories.getPart_number(), inventories.getOrg_id(), inventories.getQuantity(), inventories.getAmount(), inventories.getLine_type(), this.poOrder.getPromiseDate()};
            genericStruct = new STRUCT(structDescriptor, connection, structObj);
            structArray[index] = genericStruct;
            index = index + 1;
            //Integer n2 = index = index + 1;
        }
        return structArray;
    }
}

