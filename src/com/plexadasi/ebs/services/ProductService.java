/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  org.apache.commons.dbutils.OutParameter
 *  org.apache.commons.dbutils.QueryRunner
 */
package com.plexadasi.ebs.services;

import com.plexadasi.connect.build.MyLogging;
import com.plexadasi.helper.HelperAP;
import com.plexadasi.ebs.CallableRunner;
import com.plexadasi.ebs.model.Product;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.plexadasi.helper.DataConverter;
import com.siebel.data.SiebelDataBean;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.QueryRunner;

public class ProductService {
    private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;
    private final SiebelDataBean siebelDataBean;

    public ProductService(SiebelDataBean siebelDataBean, Connection connection) {
        this.siebelDataBean = siebelDataBean;
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }

    public Product createProduct(Product product) throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CREATE_UPDATE_ITEM(?,?,?,?,?,?,?,?,?)}";
        Map<String, Object> item = 
                prun.queryProc(this.connection, 
                this.query, 
                new Object[]{
                    HelperAP.getEbsUserId(), 
                    HelperAP.getEbsInventoryManagerResp(), 
                    HelperAP.getEbsAppId(), 
                    product.getPartNumber(), 
                    product.getDescription(), 
                    product.getOrganizationCode(), 
                    HelperAP.getTemplateName(), 
                    new OutParameter(Types.VARCHAR, String.class, "OrgID"),
                    new OutParameter(Types.VARCHAR, String.class, "InvId")
                });
        MyLogging.log(Level.INFO, item.toString());
        product.setId(DataConverter.toInt((String)item.get("OrgID")));
        product.setOrganizationId(DataConverter.toInt((String)item.get("InvId")));
        return product;
    }

    public void assignProduct(Product product) throws SQLException {
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL ITEM_ASSIGN(?,?,?,?,?)}";
        prun.queryProc(this.connection, this.query, HelperAP.getEbsUserId(), HelperAP.getEbsInventoryManagerResp(), HelperAP.getEbsAppId(), product.getId(), product.getOrganizationId());
    }
}

