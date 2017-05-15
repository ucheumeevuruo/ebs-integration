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
public class AccountSite extends ASqlObj
{
    private static final String NEXT_LINE = ";\n";
    
    private static final String NEXT_LINE_COMMA = ",\n";

    /**
     * 
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    
    public AccountSite () throws SiebelBusinessServiceException
    {
        super();
        HZ_CUST = "HZ_CUST_ACCOUNT_SITE_V2PUB.CREATE_CUST_SITE_USE";
        X_REC = "p_cust_site_use_rec";
        X_ID = "x_site_use_id";
        output = "";
    }
    
    /**
     */
    @Override
    public final void firstCall()
    {
        output +=   "\nDECLARE\n";
        output +=   X_REC + "\t HZ_CUST_ACCOUNT_SITE_V2PUB.CUST_SITE_USE_REC_TYPE" + NEXT_LINE;
        output +=   "p_customer_profile_rec\t HZ_CUSTOMER_PROFILE_V2PUB.CUSTOMER_PROFILE_REC_TYPE" + NEXT_LINE;
        output +=   X_ID + "\t\t NUMBER" + NEXT_LINE;
        output +=   X_RETURN + "\t\t VARCHAR2(2000)" + NEXT_LINE;
        output +=   X_MSG_C + "\t\t NUMBER" + NEXT_LINE;
        output +=   X_MSG_D + "\t\t VARCHAR2(2000)" + NEXT_LINE;
    }
    
    /**
     *
     */
    @Override
    public final void thirdCall()
    {
        output += "-- Initializing the Mandatory API parameters" + NEXT_LINE;
        
        output += "p_cust_site_use_rec.cust_acct_site_id := " + property.get("account_site_id") + "" + NEXT_LINE;
        
        output += "p_cust_site_use_rec.site_use_code := '" + property.get("usage") + "'" + NEXT_LINE;
        
        output += "p_cust_site_use_rec.created_by_module := '" + property.get("module") + "'" + NEXT_LINE;
    }
    
         /**
     *
     */
    @Override
    public void fourthCall()
    {
        output += HZ_CUST + "\n";
        output += OPEN_BRACE;
        output +=   P_INIT + APPEND + FND_API + NEXT_LINE_COMMA +
                    X_REC + APPEND + X_REC + NEXT_LINE_COMMA +
                    "p_customer_profile_rec" + APPEND + "p_customer_profile_rec" + NEXT_LINE_COMMA +
                    "p_create_profile" + APPEND + "FND_API.G_TRUE" + NEXT_LINE_COMMA +
                    "p_create_profile_amt" + APPEND + "FND_API.G_TRUE" + NEXT_LINE_COMMA +
                    X_ID + APPEND + X_ID + NEXT_LINE_COMMA +
                    X_RETURN + APPEND + X_RETURN + NEXT_LINE_COMMA +
                    X_MSG_C + APPEND + X_MSG_C + NEXT_LINE_COMMA +
                    X_MSG_D + APPEND + X_MSG_D + "\n";
        output += CLOSE_BRACE;
        output += "\n";
    }
    
    /**
     */
    @Override
    public final void lastCall()
    {
        output += "?:=" + X_ID + NEXT_LINE;
        output += "?:=" + X_REC + ".site_use_code" + NEXT_LINE;
        output += "?:=" + X_RETURN + NEXT_LINE;
        output += "?:=" + X_MSG_C + NEXT_LINE;
        output += "?:=" + X_MSG_D + NEXT_LINE;
        output += END;
    }
}