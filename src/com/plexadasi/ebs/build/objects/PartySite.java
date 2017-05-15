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
public class PartySite extends ASqlObj
{
    private static final String NEXT_LINE = ";\n";
    private static final String NEXT_LINE_COMMA = ",\n";
    
    public PartySite() throws SiebelBusinessServiceException
    {
        super();
        HZ_CUST = "HZ_PARTY_SITE_V2PUB.CREATE_PARTY_SITE";
        X_REC = "p_party_site_rec";
        X_ID = "x_party_site_id";
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        output += "DECLARE\n";
        output += "\n";
        output += X_REC     + " HZ_PARTY_SITE_V2PUB.PARTY_SITE_REC_TYPE" + NEXT_LINE;
        output += X_ID      + " NUMBER" + NEXT_LINE;
        output += "x_party_site_number VARCHAR2(2000)" + NEXT_LINE;
        output += X_RETURN  + " VARCHAR2(2000)" + NEXT_LINE;
        output += X_MSG_C   + " NUMBER" + NEXT_LINE;
        output += X_MSG_D   + " VARCHAR2(2000)" + NEXT_LINE;
    }
    
    /**
     *
     */
    @Override
    public final void thirdCall()
    {
        
        output += "-- Initializing the Mandatory API parameters" + NEXT_LINE;
        
        output += "p_party_site_rec.party_id  := '" + property.get("party_id") + "'" + NEXT_LINE;
        
        output += "p_party_site_rec.location_id := '" + property.get("location_id") + "'" + NEXT_LINE;
        
        output += "p_party_site_rec.identifying_address_flag := '" + property.get("flag") + "'"  + NEXT_LINE;
        
        output += "p_party_site_rec.created_by_module := '" + property.get("module") + "'" + NEXT_LINE;
    }
    
     /**
     *
     */
    @Override
    public void fourthCall()
    {
        output += HZ_CUST + "\n";
        output += OPEN_BRACE;
        output +=   P_INIT + APPEND + FND_API + NEXT_LINE_COMMA +
                    X_REC + APPEND + X_REC + NEXT_LINE_COMMA +
                    X_ID + APPEND + X_ID + NEXT_LINE_COMMA +
                    "x_party_site_number => x_party_site_number" + NEXT_LINE_COMMA +
                    X_RETURN + APPEND + X_RETURN + NEXT_LINE_COMMA +
                    X_MSG_C + APPEND + X_MSG_C + NEXT_LINE_COMMA +
                    X_MSG_D + APPEND + X_MSG_D + "\n";
        output += CLOSE_BRACE;
        output += "\n";
    }
    
    /**
     *
     */
    @Override
    public final void lastCall()
    {
        output += "?:=" + X_ID +NEXT_LINE;
        output += "?:=x_party_site_number" + NEXT_LINE;
        output += "?:=" + X_RETURN + NEXT_LINE;
        output += "?:=" + X_MSG_C + NEXT_LINE;
        output += "?:=" + X_MSG_D + NEXT_LINE;
        output += END;
    }
}