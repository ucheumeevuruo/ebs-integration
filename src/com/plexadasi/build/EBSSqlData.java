package com.plexadasi.build;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
import com.plexadasi.ebs.SiebelApplication.ApplicationsConnection;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EBSSqlData 
{
    static StringWriter errors = new StringWriter();
    private static Connection CONN;
    private static Statement cs;
    private static PreparedStatement preparedStatement;
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static SqlPreparedStatement stmt = null;
    
    public EBSSqlData(Connection connectObj)
    {
        CONN = connectObj;
        stmt = new SqlPreparedStatement(CONN);
    }
    
    public String getPartySiteId(String ebs_id) throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT hca.party_id, hp.party_name\n" +
                    "FROM hz_cust_accounts hca, hz_parties hp\n" +
                    "WHERE hca.party_id = hp.party_id\n" +
                    "AND hca.cust_account_id = ?";
        try 
        {
            MyLogging.log(Level.INFO, "SQL For Trx Invoice Header :" + sql);
            preparedStatement = CONN.prepareCall(sql);
            preparedStatement.setString(1, ebs_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                output = rs.getString("party_id"); 
                MyLogging.log(Level.INFO, "TRX_HEADER_ID:{0}"+ output);
            } 
            preparedStatement.close();
        }  
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String getTrxInvoiceHeader() throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT xx_invoice_header.nextval as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Invoice Header :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num")); 
                MyLogging.log(Level.INFO, "TRX_HEADER_ID:{0}"+ output);
            } 
            cs.close();
        } 
        catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String getTrxLineId() throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT xx_invoice_line.NEXTVAL as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Line Id :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num")); 
                MyLogging.log(Level.INFO, "TRX_LINE_ID:{0}"+ output);
            } 
            cs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String getTrxDistId() throws SiebelBusinessServiceException
    {
        String output = "";
        String sql = "SELECT xx_invoice_dist.NEXTVAL as num FROM DUAL";
        try {
            MyLogging.log(Level.INFO, "SQL For Trx Dist Id :" + sql);
            cs = CONN.createStatement();
            ResultSet rs = cs.executeQuery(sql);
            while (rs.next()) {
                output = String.valueOf(rs.getInt("num")); 
                MyLogging.log(Level.INFO, "TRX_DIST_ID:{0}"+ output);
            } 
            cs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String getCombinationId(int inventory_id, int org_id) throws SiebelBusinessServiceException
    {
        String output = "";
        try 
        {
            String sql = "SELECT SALES_ACCOUNT FROM MTL_SYSTEM_ITEMS WHERE INVENTORY_ITEM_ID = ? AND ORGANIZATION_ID = ?";
            MyLogging.log(Level.INFO, "SQL For Combination ID :" + sql);
            preparedStatement = CONN.prepareStatement(sql);
            preparedStatement.setInt(1, inventory_id);
            preparedStatement.setInt(2, org_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) 
            {
                output = String.valueOf(rs.getInt("SALES_ACCOUNT"));
                MyLogging.log(Level.INFO, "Code Combination Number:{0}"+ output);
            }
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String billToAccount(int ebs_id) throws SiebelBusinessServiceException
    {
        String output = "";
        try {
            
            stmt.select("SITE_USE_ID").from("HZ_CUST_SITE_USES_ALL")
            .where(
                "CUST_ACCT_SITE_ID",
                "(SELECT CUST_ACCT_SITE_ID FROM HZ_CUST_ACCT_SITES_ALL WHERE CUST_ACCOUNT_ID = ? AND BILL_TO_FLAG = ?)"
            ).preparedStatement();
            stmt.setInt(1, ebs_id);
            stmt.setString(2, "P");
            ResultSet rs = stmt.get();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("SITE_USE_ID"));
                MyLogging.log(Level.INFO, "Bill to id:{0}"+ output);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String shipToAccount(int ebs_id) throws SiebelBusinessServiceException{
        String output = "";
        try 
        {
            stmt.select("SITE_USE_ID").from("HZ_CUST_SITE_USES_ALL")
            .where(
                "CUST_ACCT_SITE_ID",
                "(SELECT CUST_ACCT_SITE_ID FROM HZ_CUST_ACCT_SITES_ALL WHERE CUST_ACCOUNT_ID = ? AND SHIP_TO_FLAG = ?)"
            ).preparedStatement();
            stmt.setInt(1, ebs_id);
            stmt.setString(2, "P");
            ResultSet rs = stmt.get();
            while (rs.next()) {
                output = String.valueOf(rs.getInt("SITE_USE_ID"));
                MyLogging.log(Level.INFO, "Ship to id:{0}"+ output);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught Sql Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("SQL_EXCEPT", ex.getMessage());
        }
        return output;
    }
    
    public String getHeaderId(Integer order_id) throws SiebelBusinessServiceException, SQLException{
        String output = "";
        stmt.select("HEADER_ID").from("OE_ORDER_HEADERS_ALL").where("ORDER_NUMBER").preparedStatement();
        stmt.setInt(1,  order_id);
        ResultSet get = stmt.get();
        while(get.next())
        {
            output = get.getString("HEADER_ID");
        }
        return output;
    }
    
    public String[] getHeaderLineId(Integer order_id, Integer inventory_id) throws SiebelBusinessServiceException, SQLException{
        String[] output = new String[3];
        stmt.select(new String[]{"a.LINE_ID", "a.HEADER_ID", "a.ORDERED_QUANTITY"})
        .from("OE_ORDER_LINES_ALL a")
        .join("OE_ORDER_HEADERS_ALL b", "b.header_id = a.header_id", "inner")
        .where("b.order_number")
        .andWhere("a.INVENTORY_ITEM_ID")
        .preparedStatement();
        stmt.setInt(1, order_id);
        stmt.setInt(2, inventory_id);
        ResultSet get = stmt.get();
        while(get.next())
        {
            output[0] = get.getString("LINE_ID");
            output[1] = get.getString("HEADER_ID");
            output[2] = get.getString("ORDERED_QUANTITY");
            MyLogging.log(Level.INFO, "LINE_ID:{0}"+ output[0]);
            MyLogging.log(Level.INFO, "\n HEADER_ID:{0}"+ output[1]);
            MyLogging.log(Level.INFO, "\n ORDERED_QUANTITY:{0}"+ output[2]);
        }
        return output;
    }
    
    public String[] orgCode(int ebs_id) throws SiebelBusinessServiceException, SQLException
    {
        String[] output = new String[2];
        stmt.select(new String[]{"a.org_id", "b.organization_code"})
        .from("HZ_CUST_ACCT_SITES_ALL a")
        .join("org_organization_definitions b", "b.organization_id = a.org_id", "left")
        .where("CUST_ACCOUNT_ID")
        .preparedStatement();
        stmt.setInt(1, ebs_id);
        ResultSet get = stmt.get();
        while (get.next()) {
            output[0] = get.getString("org_id");
            output[1] = get.getString("organization_code");
        }
        MyLogging.log(Level.INFO, "Organization Id:{0}"+ output[0]);
        MyLogging.log(Level.INFO, "Organization code:{0}"+ output[1]);
        get.close();
        stmt.close();
        return output;
    }
    
    public boolean updatePriceList(int item_price, int org_id, int inventory_id, int price_list) throws SiebelBusinessServiceException, SQLException
    {
        boolean output;
        stmt.select("spll.LIST_LINE_ID").
        from(new String[]{
            "qp_list_headers_b spl",
            "qp_list_lines spll", 
            "mtl_system_items_b msi",
            "qp_pricing_attributes qpa"
        }).
        where("msi.organization_id").
        andWhere("msi.inventory_item_id").
        andWhere("spl.list_header_id").
        andWhere("spll.list_header_id", "spl.list_header_id").
        andWhere("qpa.list_header_id", "spl.list_header_id").
        andWhere("spll.list_line_id", "qpa.list_line_id").
        andWhere("qpa.product_attribute_context", "'ITEM'").
        andWhere("qpa.product_attribute", "'PRICING_ATTRIBUTE1'").
        andWhere("qpa.product_attr_value", "TO_CHAR (msi.inventory_item_id)").
        andWhere("qpa.product_uom_code", "msi.primary_uom_code").
        andIsNull("qpa.pricing_attribute_context").
        andWhere("qpa.excluder_flag", "'N'").
        andWhere("qpa.pricing_phase_id", "1").
        preparedStatement();
        stmt.setInt(1, 123);
        stmt.setInt(2, 18);
        stmt.setInt(3, 9013);
        String line_id = "";
        ResultSet rs = stmt.get();
        while (rs.next()) {
            line_id = String.valueOf(rs.getInt(1));
        }
        stmt.get().close();
        stmt.close();
        //
        stmt.update("qp_list_lines", new String[]{"OPERAND"})
        .where("LIST_LINE_ID")
        .preparedStatement();
        stmt.setInt(1, item_price);
        stmt.setString(2, String.valueOf(line_id));
        output = stmt.executeUpdate();
        stmt.close();
        return output;
    }
    
    public String getTrxNumber(int cust_trx_id) throws SiebelBusinessServiceException, SQLException
    {
        String output = null;
        stmt.select("trx_number").from("RA_CUSTOMER_TRX_ALL").where("customer_trx_id").preparedStatement();
        stmt.setInt(1, cust_trx_id);
        ResultSet rs = stmt.get();
        while (rs.next()) 
        {
            output = String.valueOf(rs.getInt("trx_number"));
            MyLogging.log(Level.INFO, "TRX_NUMBER:{0}"+ output);
        }
        rs.close();
        stmt.close();
        return output;
    }
    
    public String getOrderBookingStatus(String param, String table, String column, String where) throws SiebelBusinessServiceException
    {
        String output = "";
        try {
            stmt.select(param).from(table).where(column).preparedStatement();
            stmt.setString(1, where);
            ResultSet rs = stmt.get();
            while (rs.next())
            {
                try {
                    output = rs.getString(param);
                    MyLogging.log(Level.INFO, "Booking status:{0}"+ output);
                } catch (SQLException ex) {
                    Logger.getLogger(EBSSqlData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EBSSqlData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    public Boolean setCustReference(int cust_trx_id, String quote_id) throws SiebelBusinessServiceException
    {
        Boolean output;
        stmt.update("RA_CUSTOMER_TRX_ALL", new String[]{"CT_REFERENCE"}).where("CUSTOMER_TRX_ID").preparedStatement();
        stmt.setString(1, quote_id);
        stmt.setInt(2, cust_trx_id);
        output = stmt.executeUpdate();
        stmt.close();
        MyLogging.log(Level.INFO, "Check if update on ct_ref is successful: " + output);
        return output;
    }
    
    public static void main(String[] args) throws SQLException, SiebelBusinessServiceException 
    {
        EBSSqlData ebs = new EBSSqlData(ApplicationsConnection.connectToEBSDatabase());
        //boolean updatePriceList = ebs.updatePriceList(0, 123, 18, 9013);
        //String[] orgId = ebs.orgCode(35113);//35113 35125
        //String status = ebs.getOrderBookingStatus("FLOW_STATUS_CODE", "OE_ORDER_HEADERS_ALL", "ORDER_NUMBER", "156");
        //String status = ebs.shipToAccount(35113); 
        ebs.setCustReference(26102, OS);
        String status = ebs.getTrxNumber(26102);
        MyLogging.log(Level.INFO, String.valueOf(status));
    }
}