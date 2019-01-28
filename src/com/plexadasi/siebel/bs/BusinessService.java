/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelException
 *  com.siebel.eai.SiebelBusinessService
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.siebel.bs;

import com.plexadasi.build.Logging;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessService;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public abstract class BusinessService
extends SiebelBusinessService {
    protected SiebelBusinessServiceException rethrow(String msg) {
        SiebelException e = new SiebelException(173015039, 675839, msg, msg);
        return this.rethrow((Exception)e);
    }

    protected SiebelBusinessServiceException rethrow(Exception e) {
        this.logger(e);
        String msg = e.getMessage();
        if (msg == null || "".equals(msg)) {
            msg = "An unexpected error has occurred. Please contact your administrator.";
        }
        return new SiebelBusinessServiceException(e.getClass().getSimpleName(), msg);
    }

    protected void logger(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        Logging.logger(this.getClass()).log(Level.SEVERE, stringWriter.toString());
    }

    protected void close(Connection connection, Boolean closeConn) throws SQLException {
        if (!connection.isClosed() && closeConn) {
            connection.close();
        }
    }

    protected void init(String methodName, SiebelPropertySet input) {
        String xtrix = "";
        for (int i = 0; i < 20; ++i) {
            xtrix = xtrix + "#";
        }
        
        Logging.logger(this.getClass()).log(
            Level.INFO, 
            "{0} </ {1} : {2} /- {3}", 
            new Object[]{xtrix, this.getClass().getSimpleName().toUpperCase(), methodName, xtrix}
        );
    }

    protected void destroy(String methodName, SiebelPropertySet output) {
        String xtrix = "";
        for (int i = 0; i < 20; ++i) {
            xtrix = xtrix + "*";
        }
        this.destroy();
        Logging.logger(this.getClass()).log(
            Level.INFO, 
            "{0} -/ {1} : {2} /> {3} \n {4}", 
            new Object[]{xtrix, "End".toUpperCase(), methodName, xtrix, output}
        );
    }
}

