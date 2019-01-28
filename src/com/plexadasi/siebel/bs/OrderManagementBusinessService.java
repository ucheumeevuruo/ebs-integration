/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.plexadasi.connect.ebs.EbsConnect
 *  com.siebel.data.SiebelPropertySet
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.siebel.bs;

import com.plexadasi.connect.ebs.EbsConnect;
import com.plexadasi.siebel.controller.OrderManagement;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderManagementBusinessService extends BusinessService {
    @Override
    public void doInvokeMethod(String MethodName, SiebelPropertySet input, SiebelPropertySet output) 
            throws SiebelBusinessServiceException {
        Connection connection = EbsConnect.connectToEBSDatabase();
        try {
            this.init(MethodName, input);
                Method method = OrderManagement.class.getMethod(MethodName, new Class[0]);
                method.invoke(new OrderManagement(connection, input, output), new Object[0]);
            }
        catch (NoSuchMethodException e) {
            this.logger(e);
            throw new RuntimeException("Method does not exist or has not yet been implemented");
        }
        catch (SecurityException e) {
            this.logger(e);
        }
        catch (IllegalAccessException e) {
            this.logger(e);
        }
        catch (IllegalArgumentException e) {
            this.logger(e);
        }
        catch (InvocationTargetException e) {
            //this.logger(e);
            throw this.rethrow(e.getMessage());
        }
        finally {
            try {
                this.close(connection, true);
            }
            catch (SQLException e) {
                this.logger(e);
            }
            this.destroy(MethodName, output);
        }
    }
}

