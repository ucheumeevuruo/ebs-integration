/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.siebel.eai.SiebelBusinessServiceException;

public interface ImplSql {
    public void firstCall();

    public void secondCall();

    public void thirdCall();

    public void thirdCall(Boolean var1) throws SiebelBusinessServiceException;

    public void fourthCall();

    public void lastCall();

    public void setProperty(String var1, String var2);

    public String getOutput();
}

