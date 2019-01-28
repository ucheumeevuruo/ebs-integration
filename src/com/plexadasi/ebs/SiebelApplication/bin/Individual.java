/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.SiebelSearch;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Impl;
import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.logging.Level;

public class Individual
extends Account
implements Impl {
    private static final String ACC_COUNTRY_CODE_FIELD = "CountryCode";
    private static final String ACC_ADDRESS_FIELD = "Street Address";
    private static final String ACC_POSTAL_FIELD = "Postal Code";
    private static final String ACC_CITY_FIELD = "City";
    private static final String ACC_STATE_FIELD = "State";
    private static final String BUS_COMP = "Contact";
    private static final String BUS_OBJ = "Contact";

    public Individual(SiebelDataBean CONN) {
        super(CONN);
    }

    @Override
    public void doTrigger() throws SiebelBusinessServiceException {
        SiebelSearch s = new SiebelSearch(CONN);
        s.setSField("CountryCode", "");
        s.setSField("Street Address", "");
        s.setSField("City", "");
        s.setSField("Postal Code", "");
        s.setSField("State", "");
        this.set = s.getSField("Contact", "Contact", this);
        MyLogging.log(Level.INFO, this.set.toString());
        this.country_code = this.set.getProperty("CountryCode");
        this.address = this.set.getProperty("Street Address");
        this.city = this.set.getProperty("City");
        this.postal = this.set.getProperty("Postal Code");
        this.state = this.set.getProperty("State");
        this.module = "BO_API";
    }

    @Override
    public void searchSpec(SiebelBusComp sbBC) throws SiebelException {
        sbBC.setSearchSpec("Id", this.siebelAccountId);
    }

    @Override
    public void getExtraParam(SiebelBusComp sbBC) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

