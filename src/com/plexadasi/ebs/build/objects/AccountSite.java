/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.build.objects;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlObj;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.Map;

public class AccountSite
extends ASqlObj {
    private static final String NEXT_LINE = ";\n";
    private static final String NEXT_LINE_COMMA = ",\n";

    public AccountSite() throws SiebelBusinessServiceException {
        HZ_CUST = "HZ_CUST_ACCOUNT_SITE_V2PUB.CREATE_CUST_SITE_USE";
        X_REC = "p_cust_site_use_rec";
        X_ID = "x_site_use_id";
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "\nDECLARE\n";
        output = output + X_REC + "\t HZ_CUST_ACCOUNT_SITE_V2PUB.CUST_SITE_USE_REC_TYPE" + ";\n";
        output = output + "p_customer_profile_rec\t HZ_CUSTOMER_PROFILE_V2PUB.CUSTOMER_PROFILE_REC_TYPE;\n";
        output = output + X_ID + "\t\t NUMBER" + ";\n";
        output = output + X_RETURN + "\t\t VARCHAR2(2000)" + ";\n";
        output = output + X_MSG_C + "\t\t NUMBER" + ";\n";
        output = output + X_MSG_D + "\t\t VARCHAR2(2000)" + ";\n";
    }

    @Override
    public final void thirdCall() {
        output = output + "-- Initializing the Mandatory API parameters;\n";
        output = output + "p_cust_site_use_rec.cust_acct_site_id := " + (String)this.property.get("account_site_id") + "" + ";\n";
        output = output + "p_cust_site_use_rec.site_use_code := '" + (String)this.property.get("usage") + "'" + ";\n";
        output = output + "p_cust_site_use_rec.created_by_module := '" + (String)this.property.get("module") + "'" + ";\n";
    }

    @Override
    public void fourthCall() {
        output = output + HZ_CUST + "\n";
        output = output + "(\n";
        output = output + P_INIT + " => " + FND_API + ",\n" + X_REC + " => " + X_REC + ",\n" + "p_customer_profile_rec" + " => " + "p_customer_profile_rec" + ",\n" + "p_create_profile" + " => " + "FND_API.G_TRUE" + ",\n" + "p_create_profile_amt" + " => " + "FND_API.G_TRUE" + ",\n" + X_ID + " => " + X_ID + ",\n" + X_RETURN + " => " + X_RETURN + ",\n" + X_MSG_C + " => " + X_MSG_C + ",\n" + X_MSG_D + " => " + X_MSG_D + "\n";
        output = output + ");";
        output = output + "\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=" + X_ID + ";\n";
        output = output + "?:=" + X_REC + ".site_use_code" + ";\n";
        output = output + "?:=" + X_RETURN + ";\n";
        output = output + "?:=" + X_MSG_C + ";\n";
        output = output + "?:=" + X_MSG_D + ";\n";
        output = output + "END;\n";
    }
}

