/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.account;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.util.logging.Level;

public class EBSAccount {
    private Integer billToId;
    private Integer shipToId;

    public void doInvoke(String acc_id, String ebs_id, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException {
        Integer doInvoke;
        Integer doInvokeShipTo;
        CreateAccountInEbs ebsAccount = new CreateAccountInEbs();
        ebsAccount.setSiteUseType("BILL_TO");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        MyLogging.log(Level.INFO, "-------------------------- Begin execution for bill to account ---------------");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        this.billToId = doInvoke = ebsAccount.create(acc_id, ebs_id, type, siebelConn, ebsConn);
        MyLogging.log(Level.INFO, "Bill to account Done: " + this.billToId);
        MyLogging.log(Level.INFO, "---------------------------------- END EXECUTION -----------------------------------");
        CreateAccountInEbs ebsAccountShipTo = new CreateAccountInEbs();
        ebsAccountShipTo.setSiteUseType("SHIP_TO");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        MyLogging.log(Level.INFO, "-------------------------- Begin execution for ship to account ---------------");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        this.shipToId = doInvokeShipTo = ebsAccountShipTo.create(acc_id, ebs_id, type, siebelConn, ebsConn);
        MyLogging.log(Level.INFO, "Ship to account Done: " + this.shipToId);
        MyLogging.log(Level.INFO, "---------------------------------- END EXECUTION -----------------------------------");
    }

    public Integer getBillToId() {
        return this.billToId;
    }

    public Integer getShipToId() {
        return this.shipToId;
    }
}

