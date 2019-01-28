/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.order;

import com.plexadasi.siebel.BeanProcessor;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class OrderManagement_OLD {
    protected final Connection connection;
    protected BeanProcessor convert = new BeanProcessor();

    public OrderManagement_OLD(Connection connection) {
        this.connection = connection;
    }

    public abstract void create(SiebelPropertySet var1, SiebelPropertySet var2) throws SQLException, SiebelBusinessServiceException, SiebelException;

    public abstract Boolean match(String var1);

    protected <T> List<T> propertyChildToBeanList(SiebelPropertySet propertySet, Class<? extends T> type) throws SiebelException {
        ArrayList<T> inventory = new ArrayList<T>();
        for (int i = 0; i < propertySet.getChildCount(); ++i) {
            SiebelPropertySet columnToProperty = propertySet.getChild(i);
//            inventory.add(this.propertyToBean(columnToProperty, type));
        }
        return inventory;
    }

//    protected <T> T propertyToBean(SiebelPropertySet propertySet, Class<? extends T> type) throws SiebelException {
//        T bean = this.convert.newInstance(type);
//        return this.convert.populateBean(bean, propertySet, type);
//    }
}

