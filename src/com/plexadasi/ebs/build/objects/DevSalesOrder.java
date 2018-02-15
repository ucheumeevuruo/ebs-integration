package com.plexadasi.ebs.build.objects;


import com.plexadasi.Helper.DataConverter;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlExtObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class DevSalesOrder extends ASqlExtObj
{
    private static final String NEXT_LINE = ";\n";
    
    //protected InvoiceObject property;

    /**
     *
     * @param ebs_conn
     * @param item
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.io.IOException
     */
    
    public DevSalesOrder (Connection ebs_conn, Product item) throws SiebelBusinessServiceException, IOException
    {
        super(ebs_conn, item);
        AR_INVOICE_API = "oe_order_pub.Process_Order";
        L_TRX_HEADER = "";
        L_TRX_LINES = "";
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        try {
            Scanner sc=new Scanner(new File("sql\\salesorderDevHeader.sql"));
            String sqlContext = "";
            while(sc.hasNextLine()){
                sqlContext += (sc.nextLine()) + "\n";
            }
            output += sqlContext;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DevSalesOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void secondCall()
    {
        output += BEGIN;
        output += globalConn();
        /*****************INITIALIZE HEADER RECORD******************************/
        output += L_TRX_HEADER + " := oe_order_pub.G_MISS_HEADER_REC";
        output += L_TRX_HEADER + "(1).operation := OE_GLOBALS.G_OPR_CREATE" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).order_type_id := " + DataConverter.toInt(property.getBillToId()) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).sold_to_org_id := " + DataConverter.toInt(property.getCustomerTrxTypeId()) + NEXT_LINE_COL; 
        output += L_TRX_HEADER + "(1).ship_to_org_id := " + property.getTrxDate() + NEXT_LINE_COL;        
        output += L_TRX_HEADER + "(1).invoice_to_org_id := '" + property.getTrxCurrency() + "'" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).sold_from_org_id := " + DataConverter.toInt(property.getTermId()) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).salesrep_id := " + DataConverter.toInt(legalEntity) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).transactional_curr_code := " + DataConverter.toInt(property.getTermId()) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).cust_po_number := '" + property.getTrxCurrency() + "'" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).order_source_id := '" + property.getTrxCurrency() + "'" + NEXT_LINE_COL;
        
        output += "l_action_request_tbl(1) := oe_order_pub.G_MISS_REQUEST_REC" + NEXT_LINE_COL;
        output += "l_action_request_tbl(1).request_type := oe_globals.g_book_order" + NEXT_LINE_COL;
        output += "l_action_request_tbl(1).entity_code := oe_globals.g_entity_header" + NEXT_LINE_COL;
    }
    /**
     *
     * @throws com.siebel.eai.SiebelBusinessServiceException
     */
    @Override
    public void thirdCall(Boolean addNum) throws SiebelBusinessServiceException
    {
        
        String invoiceItemsBody = "", finvoiceItemsBody = "";
        MyLogging.log(Level.INFO, List.size() + List.toString());
        if(List.size()<= 0)
        {
             MyLogging.log(Level.SEVERE, "Please add one or more item.");
             throw new SiebelBusinessServiceException("NO_ITEM", "Please add one or more item.");
        }
        for(int i = 0; i < List.size(); i++)
        {
            int num = addNum == true ? i + 1 : 1;
            Map<String, String> item = List.get(i);
            int inventoryId = DataConverter.toInt(item.get("Product Inventory Item Id"));
            if(inventoryId <= 0)
            {
                MyLogging.log(Level.SEVERE, "Inventory cannot be empty. Please check and try again.");
                throw new SiebelBusinessServiceException("NO_INVENTORY_ID", "Inventory cannot be empty. Please check and try again.");
            }
            
            /*****************INITIALIZE LINE RECORD********************************/
            finvoiceItemsBody += L_TRX_LINES + "(" + num + ") := oe_order_pub.G_MISS_LINE_REC";
            finvoiceItemsBody += L_TRX_LINES + "(" + num + ").operation := OE_GLOBALS.G_OPR_CREATE";
            finvoiceItemsBody += L_TRX_LINES + "(" + num + ")" + ".inventory_item_id := " + inventoryId;
            finvoiceItemsBody += L_TRX_LINES + "(" + num + ")" + ".ordered_quantity := " + DataConverter.toInt(item.get("Quantity"));
            finvoiceItemsBody += L_TRX_LINES + "(" + num + ")" + ".ship_to_org_id := " + DataConverter.toInt(item.get("Ship To Id"));
            finvoiceItemsBody += "--l_line_tbl(1).tax_code := 'Location'";

            finvoiceItemsBody = invoiceItemsBody;
        }
        MyLogging.log(Level.INFO, finvoiceItemsBody);
        output += finvoiceItemsBody;
    }
    
    @Override
    public void fourthCall()
    {
        output += AR_INVOICE_API + NEXT_LINE;
        output += OPEN_BRACE;
        output += "p_api_version" + APPEND + P_API_VERSION + NEXT_LINE_COMMA;
        output += "l_line_tbl" + APPEND + "l_line_tbl" + NEXT_LINE_COMMA;
        output += "l_action_request_tbl" + APPEND + "l_action_request_tbl" + NEXT_LINE_COMMA;
        output += "x_header_rec" + APPEND + "l_header_rec_out" + NEXT_LINE_COMMA;
        output += "x_header_val_rec" + APPEND + "l_header_val_rec_out" + NEXT_LINE_COMMA;
        output += "x_header_adj_tbl" + APPEND + "l_header_adj_tbl_out" + NEXT_LINE_COMMA;
        output += "x_header_adj_val_tbl" + APPEND + "l_header_adj_val_tbl_out" + NEXT_LINE_COMMA;
        output += "x_header_price_att_tbl" + APPEND + "l_header_price_att_tbl_out" + NEXT_LINE_COMMA;
        output += "x_header_adj_att_tbl" + APPEND + "l_header_adj_att_tbl_out" + NEXT_LINE_COMMA;
        output += "x_header_adj_assoc_tbl" + APPEND + "l_header_adj_assoc_tbl_out" + NEXT_LINE_COMMA;
        output += "x_header_scredit_tbl" + APPEND + "l_header_scredit_tbl_out" + NEXT_LINE_COMMA;
        output += "x_header_scredit_val_tbl" + APPEND + "l_header_scredit_val_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_tbl" + APPEND + "l_line_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_val_tbl" + APPEND + "l_line_val_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_adj_tbl" + APPEND + "l_line_adj_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_adj_val_tbl" + APPEND + "l_line_adj_val_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_price_att_tbl" + APPEND + "l_line_price_att_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_adj_att_tbl" + APPEND + "l_line_adj_att_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_adj_assoc_tbl" + APPEND + "l_line_adj_assoc_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_scredit_tbl" + APPEND + "l_line_scredit_tbl_out" + NEXT_LINE_COMMA;
        output += "x_line_scredit_val_tbl" + APPEND + "l_line_scredit_val_tbl_out" + NEXT_LINE_COMMA;
        output += "x_lot_serial_tbl" + APPEND + "l_lot_serial_tbl_out" + NEXT_LINE_COMMA;
        output += "x_lot_serial_val_tbl" + APPEND + "_lot_serial_val_tbl_out" + NEXT_LINE_COMMA;
        output += "x_action_request_tbl" + APPEND + "l_action_request_tbl_out" + NEXT_LINE_COMMA;
        output += X_RETURN + APPEND + L_RETURN + NEXT_LINE_COMMA;
        output += X_MSG_C + APPEND + L_MSG_C + NEXT_LINE_COMMA;
        output += X_MSG_D + APPEND + L_MSG_D + NEXT_LINE;
        output += CLOSE_BRACE;
        output += NEXT_LINE;
        output += "COMMIT" + NEXT_LINE_COL;
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