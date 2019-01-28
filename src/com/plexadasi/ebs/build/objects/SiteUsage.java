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

public class SiteUsage
extends ASqlObj {
    private static final String NEXT_LINE = ";\n";
    private static final String NEXT_LINE_COMMA = ",\n";

    public SiteUsage() throws SiebelBusinessServiceException {
        HZ_CUST = "HZ_PARTY_SITE_V2PUB.CREATE_PARTY_SITE_USE";
        X_REC = "p_party_site_use_rec";
        X_ID = "x_party_site_use_id";
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "\nDECLARE\n" + X_REC + "\t\tHZ_PARTY_SITE_V2PUB.PARTY_SITE_USE_REC_TYPE" + ";\n" + X_ID + "\t\tNUMBER" + ";\n" + X_RETURN + "\tVARCHAR2(2000)" + ";\n" + X_MSG_C + "\tNUMBER" + ";\n" + X_MSG_D + "\tVARCHAR2(2000)" + ";\n";
    }

    @Override
    public final void thirdCall() {
        output = output + "-- Initializing the Mandatory API parameters;\n";
        output = output + "p_party_site_use_rec.site_use_type  := upper('" + (String)this.property.get("use_type") + "')" + ";\n";
        output = output + "p_party_site_use_rec.party_site_id  := '" + (String)this.property.get("site_id") + "'" + ";\n";
        output = output + "p_party_site_use_rec.created_by_module := '" + (String)this.property.get("module") + "'" + ";\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=p_party_site_use_rec.party_site_id;\n";
        output = output + "?:=" + X_ID + ";\n";
        output = output + "?:=" + X_RETURN + ";\n";
        output = output + "?:=" + X_MSG_C + ";\n";
        output = output + "?:=" + X_MSG_D + ";\n";
        output = output + "END;\n";
    }
}

