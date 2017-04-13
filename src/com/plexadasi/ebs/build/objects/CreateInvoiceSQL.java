package com.plexadasi.ebs.build.objects;


import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlExtObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SAP Training
 */
public class CreateInvoiceSQL extends ASqlExtObj
{
    
    /**
     *
     * @param item
     * @throws com.siebel.eai.SiebelBusinessServiceException
     * @throws java.io.IOException
     */
    
    public CreateInvoiceSQL (Product item) throws SiebelBusinessServiceException, IOException
    {
        super(item);
        output = "";
    }
    
    /**
     *
     */
    @Override
    public final void firstCall()
    {
        output += "\nDECLARE" + NEXT_LINE;
        output += L_RETURN + " VARCHAR2(1)" + NEXT_LINE_COL;
        output += L_MSG_C  + " NUMBER" + NEXT_LINE_COL;
        output += L_MSG_D  + " VARCHAR2(2000)" + NEXT_LINE_COL;
        output += L_BATCH_ID  + " NUMBER" + NEXT_LINE_COL;
        output += "l_cnt               number := 0" + NEXT_LINE_COL;
        output += "l_batch_source_rec        ar_invoice_api_pub.batch_source_rec_type" + NEXT_LINE_COL;
        output += "l_trx_header_tbl          ar_invoice_api_pub.trx_header_tbl_type" + NEXT_LINE_COL;
        output += "l_trx_lines_tbl           ar_invoice_api_pub.trx_line_tbl_type" + NEXT_LINE_COL;
        output += "l_trx_dist_tbl            ar_invoice_api_pub.trx_dist_tbl_type" + NEXT_LINE_COL;
        output += "l_trx_salescredits_tbl    ar_invoice_api_pub.trx_salescredits_tbl_type" + NEXT_LINE_COL;
        output += "l_trx_contingencies_tbl   ar_invoice_api_pub.trx_contingencies_tbl_type" + NEXT_LINE_COL;
        output += "trx_header_id_v           NUMBER" + NEXT_LINE_COL;
        output += "trx_line_id_v             NUMBER" + NEXT_LINE_COL;
        output += "trx_dist_id_v             NUMBER" + NEXT_LINE_COL;
        output += "l_customer_trx_id           number" + NEXT_LINE_COL;
    }
    
    
    
    
    /**
     *
     */
    @Override
    public final void lastCall()
    {
        output += "?:=l_customer_trx_id" + NEXT_LINE_COL;
        output += END;
    }
}