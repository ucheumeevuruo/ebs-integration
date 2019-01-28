/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.SiebelApplication.objects.Impl;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.ImplSql;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.plexadasi.invoice.InvoiceObject;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class ASqlExtObj
implements ImplSql {
    protected static String output = null;
    protected InvoiceObject property = new InvoiceObject();
    protected EBSSqlData ebsSqlData = null;
    protected String AR_INVOICE_API = "AR_INVOICE_API_PUB.create_single_invoice";
    protected static final String L_CUST_TRX_ID = "l_customer_trx_id";
    protected static final String L_RETURN = "l_return_status";
    protected static final String L_MSG_D = "l_msg_data";
    protected static final String L_MSG_C = "l_msg_count";
    protected static final String L_BATCH_ID = "l_batch_id";
    protected static final String L_CNT = "l_cnt";
    protected static final String P_API_VERSION = "1.0";
    protected static final String X_RETURN = "x_return_status";
    protected static final String X_MSG_C = "x_msg_count";
    protected static final String X_MSG_D = "x_msg_data";
    protected String L_TRX_HEADER = "l_trx_header_tbl";
    protected String L_TRX_LINES = "l_trx_lines_tbl";
    protected static final String L_TRX_DIST = "l_trx_dist_tbl";
    protected static final String APPEND = " => ";
    protected static final String NEXT_LINE_COMMA = ",\n";
    protected static final String NEXT_LINE_COL = ";\n";
    protected static final String NEXT_LINE = "\n";
    protected static final String OPEN_BRACE = "(\n";
    protected static final String CLOSE_BRACE = ");";
    protected static final String BEGIN = "BEGIN\n";
    protected static final String END = "END;\n";
    protected static String userId = "";
    protected static String respId = "";
    protected static String batchId = "";
    protected static String legalEntity = "";
    protected List<Map<String, String>> List;
    private final String trxHeaderId;
    private final String trxLineId;
    private final String trxDistId;
    protected Product items = null;
    private final int lineNumber = 1;

    public ASqlExtObj(Connection ebs_conn, Product item) throws SiebelBusinessServiceException {
        userId = HelperAP.getEbsUserId();
        respId = HelperAP.getEbsReceivableManagerResp();
        batchId = HelperAP.getSourceBatchId();
        legalEntity = HelperAP.getLegalEntity();
        this.List = new ArrayList<Map<String, String>>();
        this.items = item;
        item.doTrigger();
        this.List = item.getList();
        this.ebsSqlData = new EBSSqlData(ebs_conn);
        this.trxHeaderId = this.ebsSqlData.getTrxInvoiceHeader();
        this.trxLineId = this.ebsSqlData.getTrxLineId();
        this.trxDistId = this.ebsSqlData.getTrxDistId();
    }

    @Override
    public void secondCall() {
        output = output + "BEGIN\n";
        output = output + this.globalConn();
        output = output + "l_batch_source_rec.batch_source_id := " + batchId + ";\n";
        output = output + this.L_TRX_HEADER + "(1).trx_header_id :=" + DataConverter.toInt(this.trxHeaderId) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).bill_to_customer_id := " + DataConverter.toInt(this.property.getBillToId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).cust_trx_type_id := " + DataConverter.toInt(this.property.getCustomerTrxTypeId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).trx_date := " + this.property.getTrxDate() + ";\n";
        output = output + this.L_TRX_HEADER + "(1).trx_currency := '" + this.property.getTrxCurrency() + "'" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).term_id := " + DataConverter.toInt(this.property.getTermId()) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).legal_entity_id := " + DataConverter.toInt(legalEntity) + ";\n";
        output = output + this.L_TRX_HEADER + "(1).finance_charges := NULL" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).status_trx := 'OP'" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).printing_option := 'PRI'" + ";\n";
        output = output + this.L_TRX_HEADER + "(1).primary_salesrep_id := " + DataConverter.toInt(this.property.getPrimarySalesId()) + ";\n";
    }

    @Override
    public void thirdCall() {
    }

    @Override
    public void thirdCall(Boolean addNum) throws SiebelBusinessServiceException {
        String invoiceItemsBody = "";
        String finvoiceItemsBody = "";
        MyLogging.log(Level.INFO, "" + this.List.size() + this.List.toString());
        int trx_header_id = DataConverter.toInt(this.trxHeaderId);
        int trx_line_id = DataConverter.toInt(this.trxLineId);
        int trx_dist_id = DataConverter.toInt(this.trxDistId);
        int link_to_trx_line_id = trx_line_id;
        int line_number = 1;
        int length = this.List.size();
        if (length <= 0) {
            MyLogging.log(Level.SEVERE, "Please add one or more item.");
            throw new SiebelBusinessServiceException("NO_ITEM", "Please add one or more item.");
        }
        for (int i = 0; i < length; ++i) {
            int num = addNum == true ? i + 1 : 1;
            Map<String, String> item = this.List.get(i);
            int inventoryId = DataConverter.toInt(item.get("Product Inventory Item Id"));
            if (inventoryId <= 0) {
                MyLogging.log(Level.SEVERE, "Inventory cannot be empty. Please check and try again.");
                throw new SiebelBusinessServiceException("NO_INVENTORY_ID", "Inventory cannot be empty. Please check and try again.");
            }
            int org_id = DataConverter.toInt(item.get("PLX Lot#"));
            int codeCombinationId = DataConverter.toInt(this.ebsSqlData.getCombinationId(inventoryId, org_id));
            invoiceItemsBody = invoiceItemsBody + "-- Initializing the Mandatory API parameters;\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").trx_header_id := " + trx_header_id + ";\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").trx_line_id := " + trx_line_id + ";\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").line_number := " + line_number + ";\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").inventory_item_id := " + inventoryId + ";\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").quantity_invoiced := " + DataConverter.toInt(item.get("Quantity")) + ";\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").unit_selling_price := " + Float.parseFloat(item.get("Net Price")) + ";\n";
            invoiceItemsBody = invoiceItemsBody + this.L_TRX_LINES + "(" + num + ").line_type := 'LINE'" + ";\n";
            invoiceItemsBody = invoiceItemsBody + "l_trx_dist_tbl(" + num + ").trx_dist_id := " + trx_dist_id + ";\n";
            invoiceItemsBody = invoiceItemsBody + "l_trx_dist_tbl(" + num + ").trx_line_id := " + trx_line_id + ";\n";
            invoiceItemsBody = invoiceItemsBody + "l_trx_dist_tbl(" + num + ").account_class := 'REV'" + ";\n";
            invoiceItemsBody = invoiceItemsBody + "l_trx_dist_tbl (" + num + ").PERCENT := 100" + ";\n";
            finvoiceItemsBody = invoiceItemsBody = invoiceItemsBody + "l_trx_dist_tbl (" + num + ").code_combination_id := " + codeCombinationId + ";\n";
            ++trx_line_id;
            ++trx_dist_id;
            ++line_number;
        }
        SiebelPropertySet prop = this.items.getProperties();
        String freight_amount = prop.getProperty("Freight Total");
        String tax_rate = prop.getProperty("PLX Calculate VAT");
        String vat = prop.getProperty("PLX Vat");
        String ldc = prop.getProperty("PLX Local Delivery Charges");
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + ++length + ").trx_header_id := " + trx_header_id + ";\n";
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + length + ").trx_line_id := " + trx_line_id + ";\n";
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + length + ").line_number := " + line_number + ";\n";
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + length + ").inventory_item_id := " + HelperAP.getDeliveryCharges() + ";\n";
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + length + ").quantity_invoiced := 1" + ";\n";
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + length + ").unit_selling_price := " + Float.parseFloat(ldc) + ";\n";
        finvoiceItemsBody = finvoiceItemsBody + this.L_TRX_LINES + "(" + length + ").line_type := 'LINE'" + ";\n";
        MyLogging.log(Level.INFO, finvoiceItemsBody);
        output = output + finvoiceItemsBody;
    }

    @Override
    public void fourthCall() {
        output = output + this.AR_INVOICE_API + "\n";
        output = output + "(\n";
        output = output + "p_api_version => 1.0,\n";
        output = output + "p_batch_source_rec => l_batch_source_rec,\n";
        output = output + "p_trx_header_tbl => l_trx_header_tbl,\n";
        output = output + "p_trx_lines_tbl => l_trx_lines_tbl,\n";
        output = output + "p_trx_dist_tbl => l_trx_dist_tbl,\n";
        output = output + "p_trx_salescredits_tbl => l_trx_salescredits_tbl,\n";
        output = output + "x_customer_trx_id => l_customer_trx_id,\n";
        output = output + "x_return_status => l_return_status,\n";
        output = output + "x_msg_count => l_msg_count,\n";
        output = output + "x_msg_data => l_msg_data\n";
        output = output + ");";
        output = output + "\n";
        output = output + "COMMIT;\n";
    }

    public final void setProperty(InvoiceObject helper) {
        this.property = helper;
    }

    @Override
    public final void setProperty(String K, String V) {
    }

    @Override
    public final String getOutput() {
        return output;
    }

    protected String globalConn() {
        String stringOutput = "";
        stringOutput = stringOutput + "-- Setting the Context --\n";
        stringOutput = stringOutput + "fnd_global.apps_initialize(" + userId + "," + respId + ",222, 0)" + ";\n";
        stringOutput = stringOutput + "mo_global.init('AR');\n";
        stringOutput = stringOutput + "mo_global.set_policy_context ('S', 101);\n";
        stringOutput = stringOutput + "xla_security_pkg.set_security_context (222);\n";
        return stringOutput;
    }
}

