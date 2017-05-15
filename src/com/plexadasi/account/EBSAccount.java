/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.account;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.util.logging.Level;

/**
 *
 * @author SAP Training
 */
public class EBSAccount {
    private Integer billToId, shipToId;
    
    public void doInvoke(String acc_id, String ebs_id, String type, SiebelDataBean siebelConn, Connection ebsConn) throws SiebelBusinessServiceException
    {
        CreateAccountInEbs ebsAccount = new CreateAccountInEbs();
        ebsAccount.setSiteUseType("BILL_TO");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        MyLogging.log(Level.INFO, "-------------------------- Begin execution for bill to account ---------------");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        Integer doInvoke = ebsAccount.create(acc_id, ebs_id, type, siebelConn, ebsConn);
        billToId = doInvoke;
        MyLogging.log(Level.INFO, "Bill to account Done: " + billToId);
        MyLogging.log(Level.INFO, "---------------------------------- END EXECUTION -----------------------------------");
        CreateAccountInEbs ebsAccountShipTo = new CreateAccountInEbs();
        ebsAccountShipTo.setSiteUseType("SHIP_TO");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        MyLogging.log(Level.INFO, "-------------------------- Begin execution for ship to account ---------------");
        MyLogging.log(Level.INFO, "------------------------------------------------------------------------------");
        Integer doInvokeShipTo = ebsAccountShipTo.create(acc_id, ebs_id, type, siebelConn, ebsConn);
        shipToId = doInvokeShipTo;
        MyLogging.log(Level.INFO, "Ship to account Done: " + shipToId);
        MyLogging.log(Level.INFO, "---------------------------------- END EXECUTION -----------------------------------");
    }
    
    
    public Integer getBillToId()
    {
        return billToId;
    }
    
    public Integer getShipToId()
    {
        return shipToId;
    }
}
