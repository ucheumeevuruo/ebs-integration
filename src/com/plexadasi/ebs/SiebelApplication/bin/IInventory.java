/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.siebel.Iinventory;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

interface IInventory {
    public Array getInventoryItem(List<Iinventory> var1) throws SiebelBusinessServiceException, SQLException, SiebelException;
}

