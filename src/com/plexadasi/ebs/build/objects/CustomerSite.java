package com.plexadasi.ebs.build.objects;


import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlObj;
import com.siebel.eai.SiebelBusinessServiceException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class CustomerSite extends ASqlObj
{
    private static final String NEXT_LINE = ";\n";

    /**
     *
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    
    public CustomerSite () throws SiebelBusinessServiceException
    {
        super();
        HZ_CUST = "HZ_CUST_ACCOUNT_SITE_V2PUB.CREATE_CUST_ACCT_SITE";
        X_REC = "p_cust_acct_site_rec";
        X_ID = "x_cust_acct_site_id";
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        output +=   "\nDECLARE\n" +
                    X_REC + "\t\t hz_cust_account_site_v2pub.cust_acct_site_rec_type" + NEXT_LINE +
                    X_ID + "\t\t\t NUMBER" + NEXT_LINE +
                    X_RETURN + "\t VARCHAR2(2000)" + NEXT_LINE +
                    X_MSG_C + "\t\t NUMBER" + NEXT_LINE +
                    X_MSG_D + "\t\t VARCHAR2(2000)" + NEXT_LINE;
    }
    
    /**
     *
     */
    @Override
    public final void thirdCall()
    {
        output += "-- Initializing the Mandatory API parameters" + NEXT_LINE;
        
        output += "p_cust_acct_site_rec.cust_account_id  := '" + property.get("account_id") + "'" + NEXT_LINE;
        
        output += "p_cust_acct_site_rec.party_site_id := '" + property.get("site_id") + "'" + NEXT_LINE;
        
        output += "p_cust_acct_site_rec.created_by_module := '" + property.get("module") + "'" + NEXT_LINE;
    }
    
    
    /**
     *
     */
    @Override
    public final void lastCall()
    {
        output += "?:=" + X_ID + NEXT_LINE;
        output += END;
    }
}