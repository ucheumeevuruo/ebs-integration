/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  oracle.sql.ARRAY
 *  oracle.sql.ArrayDescriptor
 *  oracle.sql.STRUCT
 *  oracle.sql.StructDescriptor
 */
package com.plexadasi.ebs;

import com.plexadasi.siebel.Iinventory;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public final class PurchaseInventory {
    private final Connection connection;
    private final List<Iinventory> inventory;
    private final String tableName;
    private final String objectName;
    private static final String DEFAULT_TABLE_NAME = "PURCHASE_ORDER_ITEM";
    private static final String DEFAULT_OBJECT_NAME = "PURCHASE_ORDER_ITEMS";

    public PurchaseInventory(Connection connection, List<Iinventory> inventory) {
        this(connection, inventory, DEFAULT_TABLE_NAME, DEFAULT_OBJECT_NAME);
    }

    public PurchaseInventory(Connection connection, List<Iinventory> inventory, String tableName, String objectName) {
        this.connection = connection;
        this.inventory = inventory;
        this.tableName = tableName;
        this.objectName = objectName;
    }

    public Array getInventoryItems() throws SQLException {
        return new ARRAY(
            ArrayDescriptor.createDescriptor((String)this.tableName, (Connection)this.connection), 
            this.connection, 
            (Object)this.createStructArray(
                this.inventory, 
                StructDescriptor.createDescriptor((String)this.objectName, (Connection)this.connection)
            )
        );
    }

    private STRUCT[] createStructArray(List<Iinventory> inventory, StructDescriptor structDescriptor) throws SQLException {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for (Iinventory inventories : inventory) {
            STRUCT genericStruct;
            Object[] structObj = new Object[]{
                inventories.getPartNumber(), 
                inventories.getWarehouseId(), 
                inventories.getQuantity(), 
                inventories.getAmount(), 
                inventories.getLineType(),
                inventories.getDueDate()
            };
            genericStruct = new STRUCT(structDescriptor, this.connection, structObj);
            structArray[index] = genericStruct;
            //Integer n = index;
            index = index + 1;
        }
        return structArray;
    }
}

