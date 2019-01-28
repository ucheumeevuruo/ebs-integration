/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.ebs.build.objects;

import com.plexadasi.ebs.SiebelApplication.objects.Impl.ASqlExtObj;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Product;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.IOException;
import java.sql.Connection;

public class CreateInvoiceSQL
extends ASqlExtObj {
    public CreateInvoiceSQL(Connection ebs_conn, Product item) throws SiebelBusinessServiceException, IOException {
        super(ebs_conn, item);
        output = "";
    }

    @Override
    public final void firstCall() {
        output = output + "\nDECLARE\n";
        output = output + "l_return_status VARCHAR2(1);\n";
        output = output + "l_msg_count NUMBER;\n";
        output = output + "l_msg_data VARCHAR2(2000);\n";
        output = output + "l_batch_id NUMBER;\n";
        output = output + "l_cnt               number := 0;\n";
        output = output + "l_batch_source_rec        ar_invoice_api_pub.batch_source_rec_type;\n";
        output = output + "l_trx_header_tbl          ar_invoice_api_pub.trx_header_tbl_type;\n";
        output = output + "l_trx_lines_tbl           ar_invoice_api_pub.trx_line_tbl_type;\n";
        output = output + "l_trx_dist_tbl            ar_invoice_api_pub.trx_dist_tbl_type;\n";
        output = output + "l_trx_salescredits_tbl    ar_invoice_api_pub.trx_salescredits_tbl_type;\n";
        output = output + "l_trx_contingencies_tbl   ar_invoice_api_pub.trx_contingencies_tbl_type;\n";
        output = output + "trx_header_id_v           NUMBER;\n";
        output = output + "trx_line_id_v             NUMBER;\n";
        output = output + "trx_dist_id_v             NUMBER;\n";
        output = output + "l_customer_trx_id           number;\n";
    }

    @Override
    public final void lastCall() {
        output = output + "?:=l_customer_trx_id;\n";
        output = output + "END;\n";
    }
}

