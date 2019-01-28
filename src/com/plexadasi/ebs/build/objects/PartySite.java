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

public class PartySite
extends ASqlObj {
    private static final String NEXT_LINE = ";\n";
    private static final String NEXT_LINE_COMMA = ",\n";

    public PartySite() throws SiebelBusinessServiceException {
        HZ_CUST = "HZ_PARTY_SITE_V2PUB.CREATE_PARTY_SITE";
        X_REC = "p_party_site_rec";
        X_ID = "x_party_site_id";
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "DECLARE\n";
        output = output + "\n";
        output = output + X_REC + " HZ_PARTY_SITE_V2PUB.PARTY_SITE_REC_TYPE" + ";\n";
        output = output + X_ID + " NUMBER" + ";\n";
        output = output + "x_party_site_number VARCHAR2(2000);\n";
        output = output + X_RETURN + " VARCHAR2(2000)" + ";\n";
        output = output + X_MSG_C + " NUMBER" + ";\n";
        output = output + X_MSG_D + " VARCHAR2(2000)" + ";\n";
    }

    @Override
    public final void thirdCall() {
        output = output + "-- Initializing the Mandatory API parameters;\n";
        output = output + "p_party_site_rec.party_id  := '" + (String)this.property.get("party_id") + "'" + ";\n";
        output = output + "p_party_site_rec.location_id := '" + (String)this.property.get("location_id") + "'" + ";\n";
        output = output + "p_party_site_rec.identifying_address_flag := '" + (String)this.property.get("flag") + "'" + ";\n";
        output = output + "p_party_site_rec.created_by_module := '" + (String)this.property.get("module") + "'" + ";\n";
    }

    @Override
    public void fourthCall() {
        output = output + HZ_CUST + "\n";
        output = output + "(\n";
        output = output + P_INIT + " => " + FND_API + ",\n" + X_REC + " => " + X_REC + ",\n" + X_ID + " => " + X_ID + ",\n" + "x_party_site_number => x_party_site_number" + ",\n" + X_RETURN + " => " + X_RETURN + ",\n" + X_MSG_C + " => " + X_MSG_C + ",\n" + X_MSG_D + " => " + X_MSG_D + "\n";
        output = output + ");";
        output = output + "\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=" + X_ID + ";\n";
        output = output + "?:=x_party_site_number;\n";
        output = output + "?:=" + X_RETURN + ";\n";
        output = output + "?:=" + X_MSG_C + ";\n";
        output = output + "?:=" + X_MSG_D + ";\n";
        output = output + "END;\n";
    }
}

