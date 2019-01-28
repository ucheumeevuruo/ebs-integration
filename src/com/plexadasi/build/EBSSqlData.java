/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.build;

import com.plexadasi.build.SqlPreparedStatement;
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection_old;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EBSSqlData {
    static StringWriter errors = new StringWriter();
    private static Connection CONN;
    private static Statement cs;
    private static PreparedStatement preparedStatement;
    private static final String OS;
    private static SqlPreparedStatement stmt;

    public EBSSqlData(Connection connectObj) {
        CONN = connectObj;
        stmt = new SqlPreparedStatement(CONN);
    }

    public String getPartySiteId(String ebs_id) throws SiebelBusinessServiceException {
        String output = "";
        String sql = "SELECT hca.party_id, hp.party_name\nFROM hz_cust_accounts hca, hz_parties hp\nWHERE hca.party_id = hp.party_id\nAND hca.cust_account_id = ?";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Invoice Header :" + sql);
            preparedStatement = CONN.prepareCall(sql);
            preparedStatement.setString(1, ebs_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output = rs.getString("party_id");
                MyLogging.log(Level.INFO, "TRX_HEADER_ID:{0}" + output);
            }
            preparedStatement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        catch (NullPointerException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }

    public String getTrxInvoiceHeader() throws SiebelBusinessServiceException {
        String output = "";
        String sql = "SELECT xx_invoice_header.nextval as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Invoice Header :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num"));
                MyLogging.log(Level.INFO, "TRX_HEADER_ID:{0}" + output);
            }
            cs.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        catch (NullPointerException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }

    public String getTrxLineId() throws SiebelBusinessServiceException {
        String output = "";
        String sql = "SELECT xx_invoice_line.NEXTVAL as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Line Id :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num"));
                MyLogging.log(Level.INFO, "TRX_LINE_ID:{0}" + output);
            }
            cs.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }

    public String getTrxDistId() throws SiebelBusinessServiceException {
        String output = "";
        String sql = "SELECT xx_invoice_dist.NEXTVAL as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Dist Id :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num"));
                MyLogging.log(Level.INFO, "TRX_DIST_ID:{0}" + output);
            }
            cs.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }

    public String getCombinationId(int inventory_id, int org_id) throws SiebelBusinessServiceException {
        String output = "";
        try {
            String sql = "SELECT SALES_ACCOUNT FROM MTL_SYSTEM_ITEMS WHERE INVENTORY_ITEM_ID = ? AND ORGANIZATION_ID = ?";
            MyLogging.log(Level.INFO, "SQL For Combination ID :" + sql);
            preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setInt(1, inventory_id);
            preparedStatement.setInt(2, org_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("SALES_ACCOUNT"));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        MyLogging.log(Level.INFO, "Inventory Id:{0} " + inventory_id + "; Org Id:{2}" + org_id + "; Code Combination Number:{3}" + output);
        return output;
    }

    public String billToAccount(int ebs_id) throws SiebelBusinessServiceException {
        String output = "";
        try {
            stmt.select("SITE_USE_ID").from("HZ_CUST_SITE_USES_ALL").where("CUST_ACCT_SITE_ID", "(SELECT CUST_ACCT_SITE_ID FROM HZ_CUST_ACCT_SITES_ALL WHERE CUST_ACCOUNT_ID = ? AND BILL_TO_FLAG = ?) AND SITE_USE_CODE='BILL_TO'").preparedStatement();
            stmt.setInt(1, ebs_id);
            stmt.setString(2, "P");
            ResultSet rs = stmt.get();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("SITE_USE_ID"));
                MyLogging.log(Level.INFO, "Bill to id:{0}" + output);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }

    public String shipToAccount(int ebs_id) throws SiebelBusinessServiceException {
        String output = "";
        try {
            stmt.select("SITE_USE_ID").from("HZ_CUST_SITE_USES_ALL").where("CUST_ACCT_SITE_ID", "(SELECT CUST_ACCT_SITE_ID FROM HZ_CUST_ACCT_SITES_ALL WHERE CUST_ACCOUNT_ID = ? AND SHIP_TO_FLAG = ?) AND SITE_USE_CODE='SHIP_TO'").preparedStatement();
            stmt.setInt(1, ebs_id);
            stmt.setString(2, "P");
            ResultSet rs = stmt.get();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("SITE_USE_ID"));
                MyLogging.log(Level.INFO, "Ship to id:{0}" + output);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }

    public String getHeaderId(Integer order_id) throws SiebelBusinessServiceException, SQLException {
        String output = "";
        stmt.select("HEADER_ID").from("OE_ORDER_HEADERS_ALL").where("ORDER_NUMBER").preparedStatement();
        stmt.setInt(1, order_id);
        ResultSet get = stmt.get();
        while (get.next()) {
            output = get.getString("HEADER_ID");
        }
        return output;
    }

    public String[] getHeaderLineId(Integer order_id, Integer inventory_id) throws SiebelBusinessServiceException, SQLException {
        String[] output = new String[3];
        stmt.select(new String[]{"a.LINE_ID", "a.HEADER_ID", "a.ORDERED_QUANTITY"}).from("OE_ORDER_LINES_ALL a").join("OE_ORDER_HEADERS_ALL b", "b.header_id = a.header_id", "inner").where("b.order_number").andWhere("a.INVENTORY_ITEM_ID").preparedStatement();
        stmt.setInt(1, order_id);
        stmt.setInt(2, inventory_id);
        ResultSet get = stmt.get();
        while (get.next()) {
            output[0] = get.getString("LINE_ID");
            output[1] = get.getString("HEADER_ID");
            output[2] = get.getString("ORDERED_QUANTITY");
            MyLogging.log(Level.INFO, "LINE_ID:{0}" + output[0]);
            MyLogging.log(Level.INFO, "\n HEADER_ID:{0}" + output[1]);
            MyLogging.log(Level.INFO, "\n ORDERED_QUANTITY:{0}" + output[2]);
        }
        return output;
    }

    public String[] getOrgCode(int orgId) throws SiebelBusinessServiceException, SQLException {
        String[] output = new String[3];
        stmt.select(new String[]{"organization_id", "organization_code", "organization_name"}).from("org_organization_definitions").where("organization_id").preparedStatement();
        stmt.setInt(1, orgId);
        ResultSet get = stmt.get();
        while (get.next()) {
            output[0] = get.getString("organization_code");
            output[1] = get.getString("organization_id");
            output[2] = get.getString("organization_name");
        }
        MyLogging.log(Level.INFO, "Organization Id:{0}" + output[0]);
        MyLogging.log(Level.INFO, "Organization code:{0}" + output[1]);
        MyLogging.log(Level.INFO, "Organization name:{0}" + output[2]);
        get.close();
        stmt.close();
        return output;
    }

    public boolean updatePriceList(Float item_price, int org_id, String part_number, String type) throws SiebelBusinessServiceException, SQLException {
        SqlPreparedStatement stmt = new SqlPreparedStatement(CONN);
        stmt.select("spll.LIST_LINE_ID").from(new String[]{"qp_list_headers_b spl", "qp_list_lines spll", "mtl_system_items_b msi", "qp_pricing_attributes qpa"}).where("msi.organization_id").andWhere("msi.segment1").andWhere("spl.list_header_id", "(SELECT PRICE_LIST_ID " +
"FROM OE_TRANSACTION_TYPES_ALL" +
"  WHERE TRANSACTION_TYPE_ID = (" +
"      SELECT TRANSACTION_TYPE_ID " +
"      FROM oe_transaction_types_all" +
"      WHERE TRANSACTION_TYPE_ID IN (" +
"          SELECT TRANSACTION_TYPE_ID" +
"          FROM OE_TRANSACTION_TYPES_TL" +
"          WHERE NAME = ?" +
"        )" +
"  ))").andWhere("spll.list_header_id", "spl.list_header_id").andWhere("qpa.list_header_id", "spl.list_header_id").andWhere("spll.list_line_id", "qpa.list_line_id").andWhere("qpa.product_attribute_context", "'ITEM'").andWhere("qpa.product_attribute", "'PRICING_ATTRIBUTE1'").andWhere("qpa.product_attr_value", "TO_CHAR (msi.inventory_item_id)").andWhere("qpa.product_uom_code", "msi.primary_uom_code").andIsNull("qpa.pricing_attribute_context").andWhere("qpa.excluder_flag", "'N'").andWhere("qpa.pricing_phase_id", "1").preparedStatement();
        stmt.setInt(1, org_id);
        stmt.setString(2, part_number);
        stmt.setString(3, type);
        String line_id = "";
        ResultSet rs = stmt.get();
        while (rs.next()) {
            line_id = String.valueOf(rs.getInt(1));
        }
        MyLogging.log(Level.INFO, "Line Id: " + line_id);
        stmt.get().close();
        stmt.close();
        stmt = new SqlPreparedStatement(CONN);
        stmt.update("qp_list_lines", new String[]{"OPERAND"}).where("LIST_LINE_ID").preparedStatement();
        stmt.setFloat(1, item_price);
        stmt.setString(2, String.valueOf(line_id));
        boolean output = stmt.executeUpdate();
        stmt.close();
        return output;
    }

    public String getTrxNumber(int cust_trx_id) throws SiebelBusinessServiceException, SQLException {
        String output = null;
        stmt.select("trx_number").from("RA_CUSTOMER_TRX_ALL").where("customer_trx_id").preparedStatement();
        stmt.setInt(1, cust_trx_id);
        ResultSet rs = stmt.get();
        while (rs.next()) {
            output = String.valueOf(rs.getInt("trx_number"));
            MyLogging.log(Level.INFO, "TRX_NUMBER:{0}" + output);
        }
        rs.close();
        stmt.close();
        return output;
    }

    public String getOrderBookingStatus(String param, String table, String column, String where) throws SiebelBusinessServiceException {
        String output = "";
        try {
            stmt.select(param).from(table).where(column).preparedStatement();
            stmt.setString(1, where);
            ResultSet rs = stmt.get();
            while (rs.next()) {
                try {
                    output = rs.getString(param);
                    MyLogging.log(Level.INFO, "Booking status:{0}" + output);
                }
                catch (SQLException ex) {
                    Logger.getLogger(EBSSqlData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(EBSSqlData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    public Boolean setCustReference(int cust_trx_id, String quote_id) throws SiebelBusinessServiceException {
        stmt.update("RA_CUSTOMER_TRX_ALL", new String[]{"CT_REFERENCE"}).where("CUSTOMER_TRX_ID").preparedStatement();
        stmt.setString(1, quote_id);
        stmt.setInt(2, cust_trx_id);
        Boolean output = stmt.executeUpdate();
        stmt.close();
        MyLogging.log(Level.INFO, "Check if update on ct_ref is successful: " + output);
        return output;
    }

    public Boolean vendorExists(Integer orgId) throws SiebelBusinessServiceException {
        Boolean output = false;
        try {
            stmt.select("Count(*)").from("ap_suppliers aps").join("ap_supplier_sites_all apss", "apss.vendor_id = aps.vendor_id", "INNER").where("aps.vendor_id").preparedStatement();
            stmt.setInt(1, orgId);
            ResultSet rs = stmt.get();
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    output = true;
                }
                MyLogging.log(Level.INFO, "Supplier Exists?:{0}" + output);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
        }
        return output;
    }

    public Integer getOnHandQuantity(String partNumber, int warehouseId, String type) throws SiebelBusinessServiceException {
        Integer onHand = 0;
        try {
            SqlPreparedStatement jdbcConnect = new SqlPreparedStatement(CONN);
            jdbcConnect.select("XXMADN_GET_OHQTY(?, ?, ?)").from("dual").preparedStatement();
            jdbcConnect.setString(1, partNumber);
            jdbcConnect.setInt(2, warehouseId);
            jdbcConnect.setString(3, type);
            ResultSet rs = jdbcConnect.get();
            while (rs.next()) {
                onHand = rs.getInt(1);
            }
            rs.close();
            jdbcConnect.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return onHand;
    }

    public Map<String, String> backOrder(String partNumber, String orderNumber) throws SiebelBusinessServiceException {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            SqlPreparedStatement jdbcConnect = new SqlPreparedStatement(CONN);
            jdbcConnect.select("d.PICK_MEANING, b.FLOW_STATUS_CODE, c.RELEASED_STATUS").from("OE_ORDER_HEADERS_ALL a").join("OE_ORDER_LINES_ALL b", "b.header_id = a.header_id", "INNER").join("wsh_delivery_details c", "c.source_line_id=b.line_id", "INNER").join("wsh_delivery_line_status_v d", "d.source_line_id=c.source_line_id", "INNER").join("mtl_system_items_b d", "d.inventory_item_id = c.inventory_item_id and d.organization_id=c.organization_id", "INNER").where("a.CUST_PO_NUMBER").andWhere("d.SEGMENT1").preparedStatement();
            jdbcConnect.setString(1, orderNumber);
            jdbcConnect.setString(2, partNumber);
            ResultSet rs = jdbcConnect.get();
            while (rs.next()) {
                map.put("status", rs.getString("PICK_MEANING"));
                map.put("flow code", rs.getString("FLOW_STATUS_CODE"));
                map.put("release status", rs.getString("RELEASED_STATUS"));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return map;
    }

    public Map<String, String> backOrder(String partNumber, int warehouseId, String orderNumber) throws SiebelBusinessServiceException {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            SqlPreparedStatement jdbcConnect = new SqlPreparedStatement(CONN);
            jdbcConnect.select("wdls.Pick_Meaning,ool.Flow_Status_Code, wdd.Released_Status,ool.Ordered_Quantity Quantity").from("OE_ORDER_HEADERS_ALL ooh").join("OE_ORDER_LINES_ALL OOL", "ool.header_id = ooh.header_id", "INNER").join("wsh_delivery_details WDD", "wdd.source_line_id=ool.line_id", "INNER").join("wsh_delivery_line_status_v WDLS", "wdls.source_line_id=wdd.source_line_id", "INNER").where("OOH.CUST_PO_NUMBER").andWhere("ool.ordered_item").andWhere("ool.ship_from_org_id").preparedStatement();
            jdbcConnect.setString(1, orderNumber);
            jdbcConnect.setString(2, partNumber);
            jdbcConnect.setInt(3, warehouseId);
            ResultSet rs = jdbcConnect.get();
            while (rs.next()) {
                map.put("pick_meaning", rs.getString("PICK_MEANING"));
                map.put("item_status", rs.getString("FLOW_STATUS_CODE"));
                map.put("release_status", rs.getString("RELEASED_STATUS"));
                map.put("back_order_quantity", rs.getString("QUANTITY"));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:" + errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return map;
    }

    public static void main(String[] args) throws SQLException, SiebelBusinessServiceException {
        EBSSqlData ebs = new EBSSqlData(ApplicationsConnection_old.connectToEBSDatabase());
        ebs.setCustReference(26102, OS);
        String status = ebs.getTrxNumber(26102);
        MyLogging.log(Level.INFO, String.valueOf(status));
    }

    static {
        OS = System.getProperty("os.name").toLowerCase();
        stmt = null;
    }
}

