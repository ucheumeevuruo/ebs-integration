/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.order.SalesOrderInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 *
 * @author SAP Training
 */
public class QuoteInventory {
    private SiebelDataBean siebConn = null;
    SalesOrderInventory so = null;
    private Connection connection = null;
    private final String quoteId;
    
    public QuoteInventory(SiebelDataBean sb, Connection ebs, String quoteId)
    {
        this.siebConn = sb;
        this.connection = ebs;
        this.quoteId = quoteId;
    }
    
    /**
     * @return the inventoryItem
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.sql.SQLException
     * @throws com.siebel.data.SiebelException
     */
    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException, SiebelException 
    {
        Quote product = new Quote(this.siebConn);
        product.setSiebelAccountId(this.quoteId);
        return new ARRAY(
            ArrayDescriptor.createDescriptor("ITEM", this.connection), 
            this.connection, 
            createStructArray(
                product.getInventories(this.connection), 
                StructDescriptor.createDescriptor("ITEMS", this.connection), 
                this.connection
            )
        );
    }
    
    private STRUCT[] createStructArray(List<Inventory> inventory, StructDescriptor structDescriptor, Connection ebsConn) throws SQLException
    {
        STRUCT[] structArray = new STRUCT[inventory.size()];
        Integer index = 0;
        for(Inventory inventories : inventory)
        {
            Object[] structObj = new Object[]{
                inventories.getPart_number(),
                inventories.getOrg_id(),
                inventories.getQuantity(),
                inventories.getAmount(),
                inventories.getLine_type()
            };
            STRUCT genericStruct = new STRUCT(structDescriptor, ebsConn, structObj);
            structArray[index] = genericStruct;
            index++;
        }
        return structArray;
    }
}