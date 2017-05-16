/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.Helper;

import com.plexadasi.ebs.SiebelApplication.ApplicationProperties;
import com.plexadasi.ebs.SiebelApplication.IProperties;
import com.siebel.eai.SiebelBusinessServiceException;

/**
 *
 * @author Adeyemi
 * The property values can be found in the integration property files.
 * 
 */
public class HelperAP
{
    private static final ApplicationProperties AP = new ApplicationProperties();

    public static String getInvoiceTemplate() throws SiebelBusinessServiceException
    {   
        AP.setProperties(IProperties.NIX_INPUT_KEY, IProperties.WIN_INPUT_KEY);
        return AP.getProperty();
    }
    
    public static String getEbsUserId() throws SiebelBusinessServiceException 
    {
        AP.setProperties(IProperties.EBS_USER_ID);
        return AP.getProperty();
    }
    
    // Get the responsibility of the user in ebs
    public static String getEbsUserResp() throws SiebelBusinessServiceException
    {
        AP.setProperties(IProperties.EBS_USER_RESP);
        return AP.getProperty();
    }

    public static String getGeneratedPath() throws SiebelBusinessServiceException
    {
        AP.setProperties(IProperties.NIX_OUTPUT_KEY, IProperties.WIN_OUTPUT_KEY);
        return AP.getProperty();
    }
    
    public static String getSourceBatchId() throws SiebelBusinessServiceException
    {
        AP.setProperties(IProperties.EBS_SOURCE_BATCH_ID);
        return AP.getProperty();
    }
    
    public static String getLegalEntity() throws SiebelBusinessServiceException
    {
        AP.setProperties(IProperties.EBS_LEGAL_ENTITY);
        return AP.getProperty();
    }
    
    public static String getPriceListID() throws SiebelBusinessServiceException
    {
        AP.setProperties(IProperties.PRICE_ID);
        return AP.getProperty();
    }
}