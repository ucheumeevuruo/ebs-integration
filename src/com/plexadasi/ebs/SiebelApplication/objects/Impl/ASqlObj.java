/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.plexadasi.helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.HashMap;
import java.util.Map;

public abstract class ASqlObj
implements ImplSql {
    protected static String output = null;
    protected final Map<String, String> property = new HashMap<String, String>();
    protected static String P_INIT = "p_init_msg_list";
    protected static String FND_API = "FND_API.G_TRUE";
    protected static String X_REC = "p_location_rec";
    protected static String X_ID = "x_location_id";
    protected static String X_RETURN = "x_return_status";
    protected static String X_MSG_D = "x_msg_data";
    protected static String X_MSG_C = "x_msg_count";
    protected static String HZ_CUST = "HZ_CUST_ACCOUNT_SITE_V2PUB.CREATE_CUST_ACCT_SITE";
    protected static final String APPEND = " => ";
    protected static final String NEXT_LINE = ",\n";
    protected static final String OPEN_BRACE = "(\n";
    protected static final String CLOSE_BRACE = ");";
    protected static final String BEGIN = "BEGIN\n";
    protected static final String END = "END;\n";
    protected static String userId = "";
    protected static String respId = "";

    public ASqlObj() throws SiebelBusinessServiceException {
        userId = HelperAP.getEbsUserId();
        respId = HelperAP.getEbsReceivableManagerResp();
    }

    @Override
    public void firstCall() {
    }

    @Override
    public void secondCall() {
        output = output + "BEGIN\n";
        output = output + "-- Setting the Context --\n";
        output = output + "mo_global.init('AR');\n";
        output = output + "fnd_global.apps_initialize(" + userId + "," + respId + ",222, 0);\n";
        output = output + "mo_global.set_policy_context ('S', 101);\n";
        output = output + "--fnd_global.set_nls_context('AMERICAN');\n";
    }

    @Override
    public abstract void thirdCall();

    @Override
    public void thirdCall(Boolean trueOrFalse) throws SiebelBusinessServiceException {
    }

    @Override
    public void fourthCall() {
        output = output + HZ_CUST + "\n";
        output = output + "(\n" + P_INIT + " => " + FND_API + ",\n" + X_REC + " => " + X_REC + ",\n" + X_ID + " => " + X_ID + ",\n" + X_RETURN + " => " + X_RETURN + ",\n" + X_MSG_C + " => " + X_MSG_C + ",\n" + X_MSG_D + " => " + X_MSG_D + "\n" + ");";
        output = output + "\n";
    }

    @Override
    public void lastCall() {
    }

    @Override
    public void setProperty(String K, String V) {
        this.property.put(K, V);
    }

    @Override
    public String getOutput() {
        return output;
    }
}

