/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.services;

import com.plexadasi.Helper.HelperAP;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.CallableRunner;
import com.plexadasi.ebs.model.Product;
import com.plexadasi.ebs.services.sql.build.SQLBuilder;
import com.siebel.data.SiebelDataBean;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.dbutils.OutParameter;
import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author SAP Training
 */
public class ProductService {private final QueryRunner jdbcTemplateObject;
    private final SQLBuilder sqlBuilder = new SQLBuilder();
    private String query;
    private final Connection connection;
    private final SiebelDataBean siebelDataBean;
    
    public ProductService(SiebelDataBean siebelDataBean, Connection connection)
    {
        this.siebelDataBean = siebelDataBean;
        this.connection = connection;
        this.jdbcTemplateObject = new QueryRunner();
    }
    
    public Product createProduct(Product product) throws SQLException{
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL CREATE_UPDATE_ITEM(?,?,?,?,?,?,?,?,?)}";
        Map<Integer, Object> item = prun.queryProc(this.connection, this.query,
            new Object[]{
                HelperAP.getEbsUserId(),
                HelperAP.getEbsUserResp(),
                HelperAP.getEbsAppId(),
                product.getPartNumber(),
                product.getDescription(),
                product.getOrganizationCode(),
                HelperAP.getTemplateName(),
                new OutParameter(java.sql.Types.INTEGER, Integer.class),
                new OutParameter(java.sql.Types.INTEGER, Integer.class)
            }
        );
        product.setId((Integer) item.get(9));
        product.setOrganizationId((Integer) item.get(8));
        return product;
    }
    
    public void assignProduct(Product product) throws SQLException{
        CallableRunner prun = new CallableRunner();
        this.query = "{CALL ITEM_ASSIGN(?,?,?,?,?)}";
        prun.queryProc(this.connection, this.query, 
            new Object[]{
                HelperAP.getEbsUserId(),
                HelperAP.getEbsUserResp(),
                HelperAP.getEbsAppId(),
                product.getId(),
                product.getOrganizationId()
            }
        );
    }
}