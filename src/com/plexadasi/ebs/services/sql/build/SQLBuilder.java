/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.services.sql.build;

import com.plexadasi.build.SqlPreparedStatement;

public class SQLBuilder {
    public SqlPreparedStatement buildBackOrderSql() {
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        jdbcConnect.select("WDLS.PICK_MEANING pickMeaning, OOL.FLOW_STATUS_CODE itemStatus, WDD.RELEASED_STATUS releaseStatus,ool.Ordered_Quantity quantity").from("OE_ORDER_HEADERS_ALL ooh").join("OE_ORDER_LINES_ALL OOL", "ool.header_id = ooh.header_id", "INNER").join("wsh_delivery_details WDD", "wdd.source_line_id=ool.line_id", "INNER").join("wsh_delivery_line_status_v WDLS", "wdls.source_line_id=wdd.source_line_id", "INNER").where("OOH.CUST_PO_NUMBER").andWhere("ooh.order_number").andWhere("ool.ordered_item").andWhere("ool.ship_from_org_id");
        return jdbcConnect;
    }

    public SqlPreparedStatement buildPurchaseOrder() {
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        jdbcConnect.select("phi.PROCESS_CODE processCode, pha.segment1 orderNumber, PHA.AUTHORIZATION_STATUS status, phi.po_header_id id").from("PO_HEADERS_INTERFACE phi").join("PO_HEADERS_ALL pha", "pha.PO_HEADER_ID=phi.PO_HEADER_ID", "LEFT").where("phi.REFERENCE_NUM");
        return jdbcConnect;
    }

    public SqlPreparedStatement buildOnhandQuantitySql() {
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        jdbcConnect.select("SUM(Moqd.Primary_Transaction_Quantity) quantity, msi.organization_id warehouseId").from("Mtl_Onhand_Quantities_Detail Moqd").join("mtl_system_items_b msi", "Msi.Inventory_Item_Id = Moqd.Inventory_Item_Id", "inner").where("msi.segment1").andWhere("(msi.organization_id = ? OR msi.organization_id ", " ?)").groupBy(new String[]{"msi.organization_id", "msi.segment1"});
        return jdbcConnect;
    }

    public String buildBillingSql(String type) {
        SqlPreparedStatement jdbcConnect = new SqlPreparedStatement();
        String billing_type = "";
        if ("s".equals(type)) {
            billing_type = "AND SHIP_TO_FLAG = ?";
        } else if ("b".equals(type)) {
            billing_type = "AND BILL_TO_FLAG = ?";
        }
        jdbcConnect.select("SITE_USE_ID siteUseId, SITE_USE_CODE billingType").from("HZ_CUST_SITE_USES_ALL").where("CUST_ACCT_SITE_ID", "(SELECT CUST_ACCT_SITE_ID FROM HZ_CUST_ACCT_SITES_ALL WHERE CUST_ACCOUNT_ID = ? " + billing_type + " AND STATUS = 'A') AND SITE_USE_CODE = ?");
        return jdbcConnect.getQuery();
    }
}

