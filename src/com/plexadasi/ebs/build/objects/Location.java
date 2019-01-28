/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.build.objects;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;

public class Location
extends ASqlObj {
    private static final String NEXT_LINE = ";\n";
    protected Account property;

    public Location(Account account) throws SiebelBusinessServiceException, IOException {
        account.doTrigger();
        this.property = account;
        X_REC = "p_location_rec";
        HZ_CUST = "HZ_LOCATION_V2PUB.CREATE_LOCATION";
        X_ID = "x_location_id";
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "DECLARE\n" + X_REC + " HZ_LOCATION_V2PUB.LOCATION_REC_TYPE" + ";\n" + " x_location_id NUMBER" + ";\n" + X_RETURN + " VARCHAR2(2000)" + ";\n" + X_MSG_C + " NUMBER" + ";\n" + X_MSG_D + " VARCHAR2(2000)" + ";\n";
    }

    @Override
    public final void secondCall() {
    }

    @Override
    public final void thirdCall() {
        output = output + "BEGIN\n";
        output = output + "-- Initializing the Mandatory API parameters;\n";
        output = output + "p_location_rec.country := '" + this.property.getCountryCode() + "'" + ";\n";
        output = output + "p_location_rec.address1 := '" + this.property.getAddress() + "'" + ";\n";
        output = output + "p_location_rec.city := '" + this.property.getCity() + "'" + ";\n";
        output = output + "p_location_rec.postal_code := '" + this.property.getPostal() + "'" + ";\n";
        output = output + "p_location_rec.state := '" + this.property.getState() + "'" + ";\n";
        output = output + "p_location_rec.created_by_module := '" + this.property.getModule() + "'" + ";\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=x_location_id;\n";
        output = output + "?:=" + X_RETURN + ";\n";
        output = output + "?:=" + X_MSG_C + ";\n";
        output = output + "?:=" + X_MSG_D + ";\n";
        output = output + "END;\n";
    }
}

