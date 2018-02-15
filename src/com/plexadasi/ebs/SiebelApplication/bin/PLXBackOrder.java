/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;

/**
 *
 * @author SAP Training
 */
public class PLXBackOrder extends SalesOrderItem
{    
    public PLXBackOrder(SiebelDataBean CONN)
    {
        super(CONN);
    }
     
    /* 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException 
    {
        sbBC.setSearchSpec("PLX Release Status", "B");
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}