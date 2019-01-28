/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.build.objects;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlExtObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.invoice.InvoiceObject;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DevSalesOrder
extends ASqlExtObj {
    private static final String NEXT_LINE = ";\n";

    public DevSalesOrder(Connection ebs_conn, Product item) throws SiebelBusinessServiceException, IOException {
        super(ebs_conn, item);
        this.AR_INVOICE_API = "oe_order_pub.Process_Order";
        this.L_TRX_HEADER = "";
        this.L_TRX_LINES = "";
        output = "";
    }

    @Override
    public final void firstCall() {
        try {
            Scanner sc = new Scanner(new File("sql\\salesorderDevHeader.sql"));
            String sqlContext = "";
            while (sc.hasNextLine()) {
                sqlContext = sqlContext + sc.nextLine() + "\n";
            }
            output = output + sqlContext;
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(DevSalesOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void secondCall() {
        output = output + "BEGIN\n";
        output = output + this.globalConn();
        output = output + this.L_TRX_HEADER + " := oe_order_pub.G_MISS_HEADER_REC";
        output = output + this.L_TRX_HEADER + "(1).operation := OE_GLOBALS.G_OPR_CREATE" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).order_type_id := " + DataConverter.toInt(this.property.getBillToId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).sold_to_org_id := " + DataConverter.toInt(this.property.getCustomerTrxTypeId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).ship_to_org_id := " + this.property.getTrxDate() + ";\n";
        output = output + this.L_TRX_HEADER + "(1).invoice_to_org_id := '" + this.property.getTrxCurrency() + "'" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).sold_from_org_id := " + DataConverter.toInt(this.property.getTermId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).salesrep_id := " + DataConverter.toInt(legalEntity) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).transactional_curr_code := " + DataConverter.toInt(this.property.getTermId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).cust_po_number := '" + this.property.getTrxCurrency() + "'" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).order_source_id := '" + this.property.getTrxCurrency() + "'" + ";\n";
        output = output + "l_action_request_tbl(1) := oe_order_pub.G_MISS_REQUEST_REC;\n";
        output = output + "l_action_request_tbl(1).request_type := oe_globals.g_book_order;\n";
        output = output + "l_action_request_tbl(1).entity_code := oe_globals.g_entity_header;\n";
    }

    @Override
    public void thirdCall(Boolean addNum) throws SiebelBusinessServiceException {
        String invoiceItemsBody = "";
        String finvoiceItemsBody = "";
        MyLogging.log(Level.INFO, "" + this.List.size() + this.List.toString());
        if (this.List.size() <= 0) {
            MyLogging.log(Level.SEVERE, "Please add one or more item.");
            throw new SiebelBusinessServiceException("NO_ITEM", "Please add one or more item.");
        }
        for (int i = 0; i < this.List.size(); ++i) {
            int num = addNum == true ? i + 1 : 1;
            Map item = (Map)this.List.get(i);
            int inventoryId = DataConverter.toInt((String)item.get("Product Inventory Item Id"));
            if (inventoryId <= 0) {
                MyLogging.log(Level.SEVERE, "Inventory cannot be empty. Please check and try again.");
                throw new SiebelBusinessServiceException("NO_INVENTORY_ID", "Inventory cannot be empty. Please check and try again.");
            }
            finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + num + ") := oe_order_pub.G_MISS_LINE_REC";
            finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + num + ").operation := OE_GLOBALS.G_OPR_CREATE";
            finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + num + ")" + ".inventory_item_id := " + inventoryId;
            finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + num + ")" + ".ordered_quantity := " + DataConverter.toInt((String)item.get("Quantity"));
            finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + num + ")" + ".ship_to_org_id := " + DataConverter.toInt((String)item.get("Ship To Id"));
            finvoiceItemsBody = finvoiceItemsBody + "--l_line_tbl(1).tax_code := 'Location'";
            finvoiceItemsBody = invoiceItemsBody;
        }
        MyLogging.log(Level.INFO, finvoiceItemsBody);
        output = output + finvoiceItemsBody;
    }

    @Override
    public void fourthCall() {
        output = output + this.AR_INVOICE_API + ";\n";
        output = output + "(\n";
        output = output + "p_api_version => 1.0,\n";
        output = output + "l_line_tbl => l_line_tbl,\n";
        output = output + "l_action_request_tbl => l_action_request_tbl,\n";
        output = output + "x_header_rec => l_header_rec_out,\n";
        output = output + "x_header_val_rec => l_header_val_rec_out,\n";
        output = output + "x_header_adj_tbl => l_header_adj_tbl_out,\n";
        output = output + "x_header_adj_val_tbl => l_header_adj_val_tbl_out,\n";
        output = output + "x_header_price_att_tbl => l_header_price_att_tbl_out,\n";
        output = output + "x_header_adj_att_tbl => l_header_adj_att_tbl_out,\n";
        output = output + "x_header_adj_assoc_tbl => l_header_adj_assoc_tbl_out,\n";
        output = output + "x_header_scredit_tbl => l_header_scredit_tbl_out,\n";
        output = output + "x_header_scredit_val_tbl => l_header_scredit_val_tbl_out,\n";
        output = output + "x_line_tbl => l_line_tbl_out,\n";
        output = output + "x_line_val_tbl => l_line_val_tbl_out,\n";
        output = output + "x_line_adj_tbl => l_line_adj_tbl_out,\n";
        output = output + "x_line_adj_val_tbl => l_line_adj_val_tbl_out,\n";
        output = output + "x_line_price_att_tbl => l_line_price_att_tbl_out,\n";
        output = output + "x_line_adj_att_tbl => l_line_adj_att_tbl_out,\n";
        output = output + "x_line_adj_assoc_tbl => l_line_adj_assoc_tbl_out,\n";
        output = output + "x_line_scredit_tbl => l_line_scredit_tbl_out,\n";
        output = output + "x_line_scredit_val_tbl => l_line_scredit_val_tbl_out,\n";
        output = output + "x_lot_serial_tbl => l_lot_serial_tbl_out,\n";
        output = output + "x_lot_serial_val_tbl => _lot_serial_val_tbl_out,\n";
        output = output + "x_action_request_tbl => l_action_request_tbl_out,\n";
        output = output + "x_return_status => l_return_status,\n";
        output = output + "x_msg_count => l_msg_count,\n";
        output = output + "x_msg_data => l_msg_data;\n";
        output = output + ");";
        output = output + ";\n";
        output = output + "COMMIT;\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=x_location_id;\n";
        output = output + "?:=x_return_status;\n";
        output = output + "?:=x_msg_count;\n";
        output = output + "?:=x_msg_data;\n";
        output = output + "END;\n";
    }
}

