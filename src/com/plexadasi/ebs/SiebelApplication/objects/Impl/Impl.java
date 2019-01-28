/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelBusComp
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelException;
import com.siebel.eai.SiebelBusinessServiceException;

public interface Impl {
    public void searchSpec(SiebelBusComp var1) throws SiebelException;

    public void doTrigger() throws SiebelBusinessServiceException;

    public void getExtraParam(SiebelBusComp var1);
}

