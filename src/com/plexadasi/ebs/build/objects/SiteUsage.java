package com.plexadasi.ebs.build.objects;


import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlObj;
import com.siebel.eai.SiebelBusinessServiceException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class SiteUsage extends ASqlObj
{
    private static final String NEXT_LINE = ";\n";
    private static final String NEXT_LINE_COMMA = ",\n";
    
    public SiteUsage() throws SiebelBusinessServiceException
    {
        super();
        HZ_CUST = "HZ_PARTY_SITE_V2PUB.CREATE_PARTY_SITE_USE";
        X_REC = "p_party_site_use_rec";
        X_ID = "x_party_site_use_id";
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        output +=   "\nDECLARE\n" +
                    X_REC + "\t\tHZ_PARTY_SITE_V2PUB.PARTY_SITE_USE_REC_TYPE" + NEXT_LINE +
                    X_ID + "\t\tNUMBER" + NEXT_LINE +
                    X_RETURN + "\tVARCHAR2(2000)" + NEXT_LINE +
                    X_MSG_C + "\tNUMBER" + NEXT_LINE +
                    X_MSG_D + "\tVARCHAR2(2000)" + NEXT_LINE;
    }
    
    /**
     *
     */
    @Override
    public final void thirdCall()
    {
        output += "-- Initializing the Mandatory API parameters" + NEXT_LINE;
        
        output += "p_party_site_use_rec.site_use_type  := upper('" + property.get("use_type") + "')" + NEXT_LINE;
        
        output += "p_party_site_use_rec.party_site_id  := '" + property.get("site_id") + "'" + NEXT_LINE;
        
        output += "p_party_site_use_rec.created_by_module := '" + property.get("module") + "'" + NEXT_LINE;
    }
    
    /**
     *
     */
    @Override
    public final void lastCall()
    {
        output += "?:=" + "p_party_site_use_rec.party_site_id" +NEXT_LINE;
        output += "?:=" + X_ID + NEXT_LINE;
        output += "?:=" + X_RETURN + NEXT_LINE;
        output += "?:=" + X_MSG_C + NEXT_LINE;
        output += "?:=" + X_MSG_D + NEXT_LINE;
        output += END;
    }
}