/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services.sql.build;

import com.plexadasi.build.SqlPreparedStatement;

/**
 *private String pickMeaning;
    private String itemStatus;
    private String releaseStatus;
    private Integer quantity;
 * @author SAP Training
 */
public class SQLBuilder {
    public SqlPreparedStatement buildBackOrderSql(){
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        
        jdbcConnect.select("WDLS.PICK_MEANING pickMeaning, OOL.FLOW_STATUS_CODE itemStatus, WDD.RELEASED_STATUS releaseStatus,ool.Ordered_Quantity quantity")
            .from("OE_ORDER_HEADERS_ALL ooh")
            .join("OE_ORDER_LINES_ALL OOL", "ool.header_id = ooh.header_id", "INNER")
            .join("wsh_delivery_details WDD", "wdd.source_line_id=ool.line_id", "INNER")
            .join("wsh_delivery_line_status_v WDLS", "wdls.source_line_id=wdd.source_line_id", "INNER")
            .where("OOH.CUST_PO_NUMBER").andWhere("ool.ordered_item").andWhere("ool.ship_from_org_id");
        
        return jdbcConnect;
    }
    
    public SqlPreparedStatement buildOnhandQuantitySql(){
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        
        jdbcConnect.select("SUM(Moqd.Primary_Transaction_Quantity) quantity, msi.organization_id warehouseId")
            .from("Mtl_Onhand_Quantities_Detail Moqd")
             .join("mtl_system_items_b msi", "Msi.Inventory_Item_Id = Moqd.Inventory_Item_Id", "inner")
            .where("msi.segment1")
            .andWhere("(msi.organization_id = ? OR msi.organization_id ", " ?)")
            .groupBy(new String[]{
                "msi.organization_id",
                "msi.segment1"
            });
        
        return jdbcConnect;
    }
    
    public String buildBillingSql(String type)
    {
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        
        String billing_type = "";
        
        if("s".equals(type)){
            billing_type = "AND SHIP_TO_FLAG = ?";
        }else if("b".equals(type)){
            billing_type = "AND BILL_TO_FLAG = ?";
        }
        jdbcConnect.select("SITE_USE_ID siteUseId, SITE_USE_CODE billingType").from("HZ_CUST_SITE_USES_ALL")
           .where(
               "CUST_ACCT_SITE_ID",
               "(SELECT CUST_ACCT_SITE_ID FROM HZ_CUST_ACCT_SITES_ALL WHERE CUST_ACCOUNT_ID = ? "+billing_type+") AND SITE_USE_CODE = ?"
           );
        return jdbcConnect.getQuery();
    }
    
    /*
    SELECT spll.LIST_LINE_ID 
    FROM qp_list_headers_b spl,qp_list_lines spll,mtl_system_items_b msi,qp_pricing_attributes qpa 
    WHERE msi.organization_id=? AND msi.segment1=? 
    AND spl.list_header_id=? 
    AND spll.list_header_id=spl.list_header_id 
    AND qpa.list_header_id=spl.list_header_id 
    AND spll.list_line_id=qpa.list_line_id 
    AND qpa.product_attribute_context='ITEM' 
    AND qpa.product_attribute='PRICING_ATTRIBUTE1' 
    AND qpa.product_attr_value=TO_CHAR (msi.inventory_item_id) 
    AND qpa.product_uom_code=msi.primary_uom_code
    AND qpa.pricing_attribute_context IS NULL`
    AND qpa.excluder_flag='N'
    AND qpa.pricing_phase_id=1

    */
}
