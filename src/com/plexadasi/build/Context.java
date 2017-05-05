package com.plexadasi.build;


import com.siebel.eai.SiebelBusinessServiceException;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
/**
 *
 * @author SAP Training
 */
public class Context {
    private static ImplSql obj = null;
    
    public static String call(ImplSql objs) throws SiebelBusinessServiceException
    {
        obj = objs;
        obj.firstCall();
        obj.secondCall();
        obj.thirdCall();
        obj.fourthCall();
        obj.lastCall();
       // MyLogging.log(Level.INFO, "Creating Sql Object : " + obj);
        return getOutput();
    }
    
    public static String call(ImplSql objs, Boolean trueOrFalse) throws SiebelBusinessServiceException
    {
        obj = objs;
        obj.firstCall();
        obj.secondCall();
        obj.thirdCall(trueOrFalse);
        obj.fourthCall();
        obj.lastCall();
        //MyLogging.log(Level.INFO, "Creating Sql Object : " + obj);
        return getOutput();
    }
    
    private static String getOutput()
    {
        return obj.getOutput();
    }
}
