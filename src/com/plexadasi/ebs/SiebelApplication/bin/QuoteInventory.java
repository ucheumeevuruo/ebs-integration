/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 *  oracle.sql.ARRAY
 *  oracle.sql.ArrayDescriptor
 *  oracle.sql.STRUCT
 *  oracle.sql.StructDescriptor
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.bin.IInventory;
import com.plexadasi.order.SalesOrderInventory;
import com.plexadasi.siebel.Iinventory;
import com.siebel.data.SiebelException;
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

public class QuoteInventory
implements IInventory {
    SalesOrderInventory so = null;
    private Connection connection = null;

    public QuoteInventory(Connection ebs) {
        this.connection = ebs;
    }

    @Override
    public Array getInventoryItem(List<Iinventory> inventory) throws SiebelBusinessServiceException, SQLException, SiebelException {
        MyLogging.log(Level.INFO, this.getClass().getSimpleName() + ":" + inventory);
        return new ARRAY(ArrayDescriptor.createDescriptor((String)"ITEM", (Connection)this.connection), this.connection, (Object)this.createStructArray(inventory, StructDescriptor.createDescriptor((String)"ITEMS", (Connection)this.connection), this.connection));
    }

    private STRUCT[] createStructArray(List<Iinventory> inventory, StructDescriptor structDescriptor, Connection ebsConn) throws SQLException {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for (Iinventory inventories : inventory) {
            STRUCT genericStruct;
            Object[] structObj = new Object[]{inventories.getPartNumber(), inventories.getWarehouseId(), inventories.getQuantity(), inventories.getAmount(), inventories.getLineType()};
            structArray[index.intValue()] = genericStruct = new STRUCT(structDescriptor, ebsConn, structObj);
            Integer n = index;
            Integer n2 = index = Integer.valueOf(index + 1);
        }
        return structArray;
    }
}

