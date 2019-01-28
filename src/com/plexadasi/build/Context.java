/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.build;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.siebel.eai.SiebelBusinessServiceException;

public class Context {
    private static ImplSql obj = null;

    public static String call(ImplSql objs) throws SiebelBusinessServiceException {
        obj = objs;
        obj.firstCall();
        obj.secondCall();
        obj.thirdCall();
        obj.fourthCall();
        obj.lastCall();
        return Context.getOutput();
    }

    public static String call(ImplSql objs, Boolean trueOrFalse) throws SiebelBusinessServiceException {
        obj = objs;
        obj.firstCall();
        obj.secondCall();
        obj.thirdCall(trueOrFalse);
        obj.fourthCall();
        obj.lastCall();
        return Context.getOutput();
    }

    private static String getOutput() {
        return obj.getOutput();
    }
}

