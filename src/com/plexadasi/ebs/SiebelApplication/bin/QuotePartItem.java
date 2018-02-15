/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;

/**
 *
 * @author SAP Training
 */
public class QuotePartItem extends SalesOrderItem
{
    private static final String BUS_OBJ = "Quote";
    private static final String BUS_COMP = "Quote Item";
    
    public QuotePartItem(SiebelDataBean CONN)
    {
        super(CONN);
        this.busComp = BUS_COMP;
        this.busObj = BUS_OBJ;
        this.field = "Quote Number";
    }
    
    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    
    /**
     * 
     * @param sbBC
     * @throws SiebelException 
     */
    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException 
    {
        sbBC.setSearchSpec("Quote Id", this.siebelAccountId);  
        sbBC.setSearchSpec(PRODUCT_TYPE, "Equipment"); 
        sbBC.setSearchSpec(Product.LOT_ID, String.valueOf(this.soldToOrgId)); 
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {}
}