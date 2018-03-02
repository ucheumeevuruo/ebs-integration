/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.Helper;

import com.plexadasi.connect.build.ApplicationProperties;
import com.plexadasi.connect.build.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

/**
 *
 * @author Adeyemi
 * The property values can be found in the integration property files.
 * 
 */
public class HelperAP
{
    private static final ApplicationProperties AP = new ApplicationProperties();
    
    private static final String ENV = "intg_property";
    
    private static final StringWriter ERRORS = new StringWriter();

    public static String getInvoiceTemplate()
    {   
        return initializeProperty(HelperProperties.WAYBILL_TEMPLATE, true);
    }
    
    public static String getEbsUserId() 
    {
        return initializeProperty(HelperProperties.EBS_USER_ID, false);
    }
    
    // Get the responsibility of the user in ebs
    public static String getEbsUserResp()
    {
        return initializeProperty(HelperProperties.EBS_USER_RESP, false);
    }

    public static String getEbsAppId()
    {
        return initializeProperty(HelperProperties.EBS_APP_ID, false);
    }

    public static String getGeneratedPath()
    {
        return initializeProperty(HelperProperties.OUTPUT_KEY, true);
    }
    
    public static String getSourceBatchId()
    {
        return initializeProperty(HelperProperties.EBS_SOURCE_BATCH_ID, false);
    }
    
    public static String getLegalEntity() 
    {
        return initializeProperty(HelperProperties.EBS_LEGAL_ENTITY, false);
    }
    
    public static String getPriceListID() 
    {
        return initializeProperty(HelperProperties.PRICE_ID, false);
    }

    public static String getLineType() 
    {
        return initializeProperty(HelperProperties.LINE_TYPE, false);
    }

    public static String getTemplateName() 
    {
        return initializeProperty(HelperProperties.TEMPLATE_NAME, false);
    }

    public static String getMasterOrganizationCode()
    {
        return initializeProperty(HelperProperties.MASTER_ORG_CODE, false);
    }

    public static String getDefalutWarehouse() 
    {
        return initializeProperty(HelperProperties.DEFAULT_WAREHOUSE, false);
    }
    
    public static String getLagosWarehouseId() 
    {
        return initializeProperty(HelperProperties.LAGOS_WAREHOUSE_ID, false);
    }

    public static String getAbujaWarehouseId() 
    {
        return initializeProperty(HelperProperties.ABUJA_WAREHOUSE_ID, false);
    }

    public static String getDeliveryCharges() 
    {
        return initializeProperty(HelperProperties.DELIVERY_CHARGES, false);
    }
        
    private static String initializeProperty(String prop, Boolean suffix)
    {
        String property = "";
        try{
            AP.loadProp(ENV).setProperty(prop, suffix);
            property = AP.getProperty();
            if(property == null){
                property = "";
                MyLogging.log(Level.SEVERE, "HelperClass:Property "+prop+" does not exist");
            }
        }catch (SiebelBusinessServiceException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));
            MyLogging.log(Level.SEVERE, "HelperClass:" + ERRORS.toString());
        }
        return property;
    }
    
    public class HelperProperties{
        public static final String LOG_FILE = "";
        public static final String WAYBILL_TEMPLATE  = "waybill_template";
        public static final String OUTPUT_KEY = "generated_path";
        public static final String EBS_USER_ID    = "ebsuserid";
        public static final String EBS_USER_RESP  = "ebsuserresp";
        public static final String EBS_APP_ID     = "ebsrespapplid";
        public static final String EBS_SOURCE_BATCH_ID  = "ebsbatchsourceid";
        public static final String EBS_LEGAL_ENTITY  = "ebslegaltrxid";
        public static final String PRICE_ID = "wst_price_id";
        public static final String LINE_TYPE = "line_type";
        public static final String TEMPLATE_NAME = "template_name";
        public static final String MASTER_ORG_CODE = "master_code";
        public static final String DELIVERY_CHARGES = "delivery_charges";
        public static final String ABUJA_WAREHOUSE_ID = "abuja_warehouse_code";
        public static final String LAGOS_WAREHOUSE_ID = "lagos_warehouse_code";
        public static final String DEFAULT_WAREHOUSE = "default_warehouse";
    }
}