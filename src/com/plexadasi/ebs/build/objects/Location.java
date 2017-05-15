package com.plexadasi.ebs.build.objects;


import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class Location extends ASqlObj
{
    private static final String NEXT_LINE = ";\n";
    
    protected Account property;

    /**
     *
     * @param account
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.io.IOException
     */
    
    public Location (Account account) throws SiebelBusinessServiceException, IOException
    {
        super();
        account.doTrigger();
        property = account;
        X_REC = "p_location_rec";
        HZ_CUST = "HZ_LOCATION_V2PUB.CREATE_LOCATION";
        X_ID = "x_location_id";
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        output +=   "DECLARE\n" +
                    X_REC       + " HZ_LOCATION_V2PUB.LOCATION_REC_TYPE" + NEXT_LINE +
                    " x_location_id NUMBER" + NEXT_LINE +
                    X_RETURN    + " VARCHAR2(2000)" + NEXT_LINE +
                    X_MSG_C     + " NUMBER" + NEXT_LINE +
                    X_MSG_D     + " VARCHAR2(2000)" + NEXT_LINE;
    }
    
    @Override
    public final void secondCall(){} 
    /**
     *
     */
    @Override
    public final void thirdCall()
    {
        output += BEGIN;
        
        output += "-- Initializing the Mandatory API parameters" + NEXT_LINE;
        
        output += "p_location_rec.country := '" + property.getCountryCode() + "'" + NEXT_LINE;
        
        output += "p_location_rec.address1 := '" + property.getAddress() + "'" + NEXT_LINE;
        
        output += "p_location_rec.city := '" + property.getCity() + "'" + NEXT_LINE;
        
        output += "p_location_rec.postal_code := '" + property.getPostal() + "'" + NEXT_LINE;
        
        output += "p_location_rec.state := '" + property.getState() + "'" + NEXT_LINE;
        
        output += "p_location_rec.created_by_module := '" + property.getModule() + "'" + NEXT_LINE;
    }
    
    
    /**
     *
     */
    @Override
    public final void lastCall()
    {
        output += "?:=x_location_id;\n";
        output += "?:=" + X_RETURN + NEXT_LINE;
        output += "?:=" + X_MSG_C + NEXT_LINE;
        output += "?:=" + X_MSG_D + NEXT_LINE; 
        output += END;
    }
}