/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.plexadasi.connect.build.ApplicationProperties
 *  com.plexadasi.connect.build.MyLogging
 */
package com.plexadasi.helper;

import com.plexadasi.connect.build.ApplicationProperties;
import com.plexadasi.connect.build.MyLogging;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public class HelperAP {
    private static final ApplicationProperties AP = new ApplicationProperties();
    private static final String ENV = "intg_property";
    private static final StringWriter ERRORS = new StringWriter();

    public static String getLogPath() {
        return HelperAP.initializeProperty("log_path", false);
    }

    public static String getInvoiceTemplate() {
        return HelperAP.initializeProperty("waybill_template", true);
    }

    public static String getEbsUserId() {
        return HelperAP.initializeProperty("ebsuserid", false);
    }

    public static String getEbsOrderManagementResp() {
        return HelperAP.initializeProperty("ebssalesresp", false);
    }

    public static String getEbsPurchaseManagerResp() {
        return HelperAP.initializeProperty("ebs_purchasemgr_resp", false);
    }

    public static String getEbsInventoryManagerResp() {
        return HelperAP.initializeProperty("ebsinventorymgr_resp", false);
    }

    public static String getEbsWarehouseManagerResp() {
        return HelperAP.initializeProperty("ebswarehousemgr_rep", false);
    }

    public static String getEbsReceivableManagerResp() {
        return HelperAP.initializeProperty("ebsrecivablemgr_rep", false);
    }

    public static String getEbsAppId() {
        return HelperAP.initializeProperty("ebsrespapplid", false);
    }

    public static String getGeneratedPath() {
        return HelperAP.initializeProperty("generated_path", true);
    }

    public static String getSourceBatchId() {
        return HelperAP.initializeProperty("ebsbatchsourceid", false);
    }

    public static String getLegalEntity() {
        return HelperAP.initializeProperty("ebslegaltrxid", false);
    }

    public static String getPriceListID() {
        return HelperAP.initializeProperty("wst_price_id", false);
    }

    public static String getLineType() {
        return HelperAP.initializeProperty("line_type", false);
    }

    public static String getTemplateName() {
        return HelperAP.initializeProperty("template_name", false);
    }

    public static String getMasterOrganizationCode() {
        return HelperAP.initializeProperty("master_code", false);
    }

    public static String getDefalutWarehouse() {
        return HelperAP.initializeProperty("default_warehouse", false);
    }

    public static String getLagosWarehouseId() {
        return HelperAP.initializeProperty("lagos_warehouse_code", false);
    }

    public static String getAbujaWarehouseId() {
        return HelperAP.initializeProperty("abuja_warehouse_code", false);
    }

    public static String getDeliveryCharges() {
        return HelperAP.initializeProperty("delivery_charges", false);
    }

    public static String getWithholdingTax() {
        return HelperAP.initializeProperty("withholding_tax", false);
    }

    public static String getFreightCharges() {
        return HelperAP.initializeProperty("freight_charges", false);
    }

    public static String getComputerProgramming() {
        return HelperAP.initializeProperty("comp_prog", false);
    }

    public static String getSundries() {
        return HelperAP.initializeProperty("sundries", false);
    }

    public static String getOrderTypeId() {
        return HelperAP.initializeProperty("order_type_id", false);
    }

    private static String initializeProperty(String prop, Boolean suffix) {
        String property = "";
        try {
            AP.loadProp("intg_property").setProperty(prop, suffix);
            property = AP.getProperty();
            if (property == null) {
                property = "";
                MyLogging.log((Level)Level.SEVERE, (String)("HelperClass:Property " + prop + " does not exist"));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));
            MyLogging.log((Level)Level.SEVERE, (String)("HelperClass:" + ERRORS.toString()));
        }
        return property;
    }

    public class HelperProperties {
        public static final String LOG_PATH = "log_path";
        public static final String WAYBILL_TEMPLATE = "waybill_template";
        public static final String OUTPUT_KEY = "generated_path";
        public static final String EBS_USER_ID = "ebsuserid";
        public static final String EBS_ORDER_MGMT_RESP = "ebssalesresp";
        public static final String EBS_RECEIVABLE_MANAGER_RESP = "ebsrecivablemgr_rep";
        public static final String EBS_WAREHOUSE_MANAGER_RESP = "ebswarehousemgr_rep";
        public static final String EBS_INVENTORY_MANAGER_RESP = "ebsinventorymgr_resp";
        public static final String EBS_PURCHASE_MANAGER_RESP = "ebs_purchasemgr_resp";
        public static final String EBS_APP_ID = "ebsrespapplid";
        public static final String EBS_SOURCE_BATCH_ID = "ebsbatchsourceid";
        public static final String EBS_LEGAL_ENTITY = "ebslegaltrxid";
        public static final String PRICE_ID = "wst_price_id";
        public static final String LINE_TYPE = "line_type";
        public static final String TEMPLATE_NAME = "template_name";
        public static final String MASTER_ORG_CODE = "master_code";
        public static final String DELIVERY_CHARGES = "delivery_charges";
        public static final String ABUJA_WAREHOUSE_ID = "abuja_warehouse_code";
        public static final String LAGOS_WAREHOUSE_ID = "lagos_warehouse_code";
        public static final String DEFAULT_WAREHOUSE = "default_warehouse";
        private static final String FREIGHT_CHARGES = "freight_charges";
        private static final String COMPUTER_PROGRAMMING = "comp_prog";
        private static final String SUNDRIES = "sundries";
        private static final String WITHHOLDING_TAX = "withholding_tax";
        private static final String ORDER_TYPE_ID = "order_type_id";
    }

}

