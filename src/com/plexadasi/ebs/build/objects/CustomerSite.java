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

public class CustomerSite
extends ASqlObj {
    private static final String NEXT_LINE = ";\n";

    public CustomerSite() throws SiebelBusinessServiceException {
        HZ_CUST = "HZ_CUST_ACCOUNT_SITE_V2PUB.CREATE_CUST_ACCT_SITE";
        X_REC = "p_cust_acct_site_rec";
        X_ID = "x_cust_acct_site_id";
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "\nDECLARE\n" + X_REC + "\t\t hz_cust_account_site_v2pub.cust_acct_site_rec_type" + ";\n" + X_ID + "\t\t\t NUMBER" + ";\n" + X_RETURN + "\t VARCHAR2(2000)" + ";\n" + X_MSG_C + "\t\t NUMBER" + ";\n" + X_MSG_D + "\t\t VARCHAR2(2000)" + ";\n";
    }

    @Override
    public final void thirdCall() {
        output = output + "-- Initializing the Mandatory API parameters;\n";
        output = output + "p_cust_acct_site_rec.cust_account_id  := '" + (String)this.property.get("account_id") + "'" + ";\n";
        output = output + "p_cust_acct_site_rec.party_site_id := '" + (String)this.property.get("site_id") + "'" + ";\n";
        output = output + "p_cust_acct_site_rec.created_by_module := '" + (String)this.property.get("module") + "'" + ";\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=" + X_ID + ";\n";
        output = output + "?:=" + X_RETURN + ";\n";
        output = output + "?:=" + X_MSG_C + ";\n";
        output = output + "?:=" + X_MSG_D + ";\n";
        output = output + "END;\n";
    }
}

