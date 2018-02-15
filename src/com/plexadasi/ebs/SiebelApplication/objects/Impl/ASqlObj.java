package com.plexadasi.ebs.SiebelApplication.objects.Impl;




import java.util.HashMap;
import java.util.Map;
import com.plexadasi.Helper.HelperAP;
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
abstract public class ASqlObj implements ImplSql {

    /**
     *
     */
    protected static String output = null;
    protected final Map<String, String> property = new HashMap();

    /**
     *
     */
    protected static String P_INIT          = "p_init_msg_list";
    protected static String FND_API         = "FND_API.G_TRUE";
    protected static String X_REC           = "p_location_rec";
    protected static String X_ID            = "x_location_id";
    protected static String X_RETURN        = "x_return_status";
    protected static String X_MSG_D         = "x_msg_data";
    protected static String X_MSG_C         = "x_msg_count";
    protected static String HZ_CUST         = "HZ_CUST_ACCOUNT_SITE_V2PUB.CREATE_CUST_ACCT_SITE";
    
    protected static final String APPEND      = " => ";
    protected static final String NEXT_LINE   = ",\n";
    protected static final String OPEN_BRACE  = "(\n";
    protected static final String CLOSE_BRACE = ");";
    
    protected static final String BEGIN     = "BEGIN\n";
    protected static final String END       = "END;\n";
    
    protected static String userId = "";
    
    protected static String respId = "";
    
    public ASqlObj() throws SiebelBusinessServiceException
    {
       userId = HelperAP.getEbsUserId();
       respId = HelperAP.getEbsUserResp();
    }

    @Override
    public void firstCall(){};
    
    @Override
    public void secondCall()
    {
        output += BEGIN;
        output += "-- Setting the Context --\n";
        output += "mo_global.init('AR');\n";
        output += "fnd_global.apps_initialize(" + userId + "," + respId + ",222, 0);\n" ;
        output += "mo_global.set_policy_context ('S', 101);\n" ;
        output += "--fnd_global.set_nls_context('AMERICAN');\n" ;
    }
    
    @Override
    abstract public void thirdCall();
    
    @Override
    public void thirdCall(Boolean trueOrFalse) throws SiebelBusinessServiceException{};
    /**
     *
     */
    @Override
    public void fourthCall()
    {
        output += HZ_CUST + "\n";
        output += OPEN_BRACE +
                        P_INIT + APPEND + FND_API + NEXT_LINE +
                        X_REC + APPEND + X_REC + NEXT_LINE +
                        X_ID + APPEND + X_ID + NEXT_LINE +
                        X_RETURN + APPEND + X_RETURN + NEXT_LINE +
                        X_MSG_C + APPEND + X_MSG_C + NEXT_LINE +
                        X_MSG_D + APPEND + X_MSG_D + "\n" +
                  CLOSE_BRACE;
        output += "\n";
    }
    
    @Override
    public void lastCall(){};
    
    
    
    /**
     * 
     * @param K
     * @param V 
     */
    @Override
    public void setProperty(String K, String V)
    {
        property.put(K, V);
    }
    
    @Override
    public String getOutput()
    {
        return output;
    }
}
