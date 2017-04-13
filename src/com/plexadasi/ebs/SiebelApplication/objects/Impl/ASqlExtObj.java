package com.plexadasi.ebs.SiebelApplication.objects.Impl;




import com.plexadasi.ebs.Helper.DataConverter;
import java.util.Map;
import com.plexadasi.ebs.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.bin.InvoiceObject;
import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
abstract public class ASqlExtObj implements ImplSql
{

    /**
     *
     */
    protected static String output = null;
    protected InvoiceObject property = new InvoiceObject();

    /**
     *
     */
    protected static final String AR_INVOICE_API = "AR_INVOICE_API_PUB.create_single_invoice";
    protected static final String L_CUST_TRX_ID = "l_customer_trx_id";
    protected static final String L_RETURN    = "l_return_status";
    protected static final String L_MSG_D     = "l_msg_data";
    protected static final String L_MSG_C     = "l_msg_count";
    protected static final String L_BATCH_ID  = "l_batch_id";
    protected static final String L_CNT       = "l_cnt";
    protected static final String P_API_VERSION = "1.0";
    protected static final String X_RETURN    = "x_return_status";
    protected static final String X_MSG_C     = "x_msg_count";
    protected static final String X_MSG_D     = "x_msg_data";
    protected static final String L_TRX_HEADER = "l_trx_header_tbl";
    protected static final String L_TRX_LINES  = "l_trx_lines_tbl";
    
    protected static final String APPEND      = " => ";
    protected static final String NEXT_LINE_COMMA   = ",\n";
    protected static final String NEXT_LINE_COL = ";\n";
    protected static final String NEXT_LINE   = "\n";
    protected static final String OPEN_BRACE  = "(\n";
    protected static final String CLOSE_BRACE = ");";
    
    protected static final String BEGIN     = "BEGIN\n";
    protected static final String END       = "END;\n";
    
    protected static String userId = "";
    
    protected static String respId = "";
    
    protected static String batchId = "";
    
    protected static String legalEntity = "";
    
    protected List<Map<String, String>> List;
    private String trxHeaderId;
    private String trxLineId;
    private String trxDistId;
    private final int lineNumber = 1;
    
    public ASqlExtObj(Product item) throws SiebelBusinessServiceException
    {
       userId = HelperAP.getEbsUserId();
       respId = HelperAP.getEbsUserResp();
       batchId = HelperAP.getSourceBatchId();
       legalEntity = HelperAP.getLegalEntity();
       List = new ArrayList();
       item.doTrigger();
       List = item.getList();
    }
    
    @Override
    public void secondCall()
    {
       trxHeaderId = property.getTrxHeader();
       trxLineId = property.geTrxLineId();
       trxDistId = property.getTrxDistId();
        output += BEGIN;
        output += "-- Setting the Context --\n";
        output += "fnd_global.apps_initialize(" + userId + "," + respId + ",222, 0)" + NEXT_LINE_COL;
        output += "mo_global.init('AR')" + NEXT_LINE_COL;
        output += "mo_global.set_policy_context ('S', 101);\n" ;
        output += "xla_security_pkg.set_security_context (222);\n" ;
        
        output += "l_batch_source_rec.batch_source_id := " + batchId + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).trx_header_id :=" + DataConverter.toInt(trxHeaderId) + NEXT_LINE_COL;
        //output += L_TRX_HEADER + "(1).trx_number :=" + trxHeaderId + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).bill_to_customer_id := " + DataConverter.toInt(property.getBillToId()) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).cust_trx_type_id := " + DataConverter.toInt(property.getCustomerTrxTypeId()) + NEXT_LINE_COL; 
        output += L_TRX_HEADER + "(1).trx_date := " + property.getTrxDate() + NEXT_LINE_COL;        
        output += L_TRX_HEADER + "(1).trx_currency := '" + property.getTrxCurrency() + "'" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).term_id := " + DataConverter.toInt(property.getTermId()) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).legal_entity_id := " + DataConverter.toInt(legalEntity) + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).finance_charges := NULL" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).status_trx := 'OP'" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).printing_option := 'PRI'" + NEXT_LINE_COL;
        output += L_TRX_HEADER + "(1).primary_salesrep_id := " + DataConverter.toInt(property.getPrimarySalesId()) + NEXT_LINE_COL;
        //output += L_TRX_HEADER + "(1).ct_reference := '" + property.getCtRef() + "'" + NEXT_LINE_COL;
        //output += L_TRX_HEADER + "(1).complete_flag := 'N'" + NEXT_LINE_COL;
    }
    
    @Override
    public void thirdCall(){}
    
    /**
     * @param addNum
     */
    @Override
    public final void thirdCall(Boolean addNum)
    {
        
        String invoiceItemsBody = "", finvoiceItemsBody = "";
        MyLogging.log(Level.INFO, List.size() + List.toString());
        int trx_header_id = DataConverter.toInt(trxHeaderId);
        int trx_line_id = DataConverter.toInt(trxLineId);
        int trx_dist_id = DataConverter.toInt(trxDistId);
        int line_number = lineNumber;
        for(int i = 0; i < List.size(); i++)
        {
            
            int num = addNum == true ? i + 1 : 1;
            Map<String, String> item = List.get(i);
            
            if(DataConverter.toInt(item.get("Inventory Id")) <= 0)
            {
                //throw new SiebelBusinessServiceException("NO_INVENTORY", "Inventory cannot be empty. Please check and try again.");
            }
            invoiceItemsBody += "-- Initializing the Mandatory API parameters" + NEXT_LINE_COL;
            
            invoiceItemsBody += "l_trx_lines_tbl(" + num + ").trx_header_id := " + trx_header_id + NEXT_LINE_COL;

            invoiceItemsBody += L_TRX_LINES + "(" + num + ").trx_line_id := " + trx_line_id + NEXT_LINE_COL;

            invoiceItemsBody += L_TRX_LINES + "(" + num + ").line_number := " + line_number + NEXT_LINE_COL;

            invoiceItemsBody += L_TRX_LINES + "(" + num + ").inventory_item_id := " + /*DataConverter.toInt(item.get("Inventory Id"))*/14 + NEXT_LINE_COL;

            invoiceItemsBody += L_TRX_LINES + "(" + num + ").quantity_invoiced := " + DataConverter.toInt(item.get("Quantity")) + NEXT_LINE_COL;

            invoiceItemsBody += L_TRX_LINES + "(" + num + ").unit_selling_price := " + DataConverter.toInt(item.get("Item Price")) + NEXT_LINE_COL;

            invoiceItemsBody += L_TRX_LINES + "(" + num + ").line_type := 'LINE'" + NEXT_LINE_COL;
            
            invoiceItemsBody += L_TRX_LINES + "(" + num + ").TAX_RATE := " + DataConverter.toInt(item.get("Discount Amount")) + NEXT_LINE_COL;
            
            invoiceItemsBody += "l_trx_dist_tbl (" + num + ").trx_dist_id := " + trx_dist_id + NEXT_LINE_COL;

            invoiceItemsBody += "l_trx_dist_tbl(" + num + ").trx_line_id := " + trx_line_id + NEXT_LINE_COL;
            
            invoiceItemsBody += "l_trx_dist_tbl (" + num + ").account_class := 'REV'" + NEXT_LINE_COL;
            
            invoiceItemsBody += "l_trx_dist_tbl (" + num + ").PERCENT := 100" + NEXT_LINE_COL;
            
            invoiceItemsBody += "l_trx_dist_tbl (" + num + ").code_combination_id := " + property.getCodeCombinationId() + NEXT_LINE_COL;
            
           // if(i == 0){
                finvoiceItemsBody = invoiceItemsBody;                   
            //}else{
               // finvoiceItemsBody = finvoiceItemsBody.concat(invoiceItemsBody);
           // }
            trx_line_id++;
            trx_dist_id++;
            line_number++;
        }
        output += finvoiceItemsBody;
    }
    
    /**
     *
     */
    @Override
    public void fourthCall()
    {
        output += AR_INVOICE_API + NEXT_LINE;
        output += OPEN_BRACE;
        output += "p_api_version" + APPEND + P_API_VERSION + NEXT_LINE_COMMA;
        output += "p_batch_source_rec" + APPEND + "l_batch_source_rec" + NEXT_LINE_COMMA;
        output += "p_trx_header_tbl" + APPEND + "l_trx_header_tbl" + NEXT_LINE_COMMA;
        output += "p_trx_lines_tbl" + APPEND + "l_trx_lines_tbl" + NEXT_LINE_COMMA;
        output += "p_trx_dist_tbl" + APPEND + "l_trx_dist_tbl" + NEXT_LINE_COMMA;
        output += "p_trx_salescredits_tbl" + APPEND + "l_trx_salescredits_tbl" + NEXT_LINE_COMMA;
        output += "x_customer_trx_id" + APPEND + "l_customer_trx_id" + NEXT_LINE_COMMA;
        output += X_RETURN + APPEND + L_RETURN + NEXT_LINE_COMMA;
        output += X_MSG_C + APPEND + L_MSG_C + NEXT_LINE_COMMA;
        output += X_MSG_D + APPEND + L_MSG_D + NEXT_LINE;
        output += CLOSE_BRACE;
        output += NEXT_LINE;
    }
    
    
    /**
     * 
     * @param helper
     */
    final public void setProperty(InvoiceObject helper)
    {
        property = helper;
    }
    
    @Override
    final public void setProperty(String K, String V){}
    
    @Override
    final public String getOutput()
    {
        return output;
    }
    
}
