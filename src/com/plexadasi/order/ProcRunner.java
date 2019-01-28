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
import com.plexadasi.ebs.services.SiebelToEbsService;
import com.siebel.data.SiebelPropertySet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;
import oracle.xdb.XMLType;

public class ProcRunner {
    protected final Connection connection;
    protected BeanProcessor convert = new BeanProcessor();
    private final SiebelPropertySet input;
    private final SiebelPropertySet output;
    private final String PROCEDURE_NAME = "PROCEDURE_NAME";

    /**
     * 
     * @param connection
     * @param input
     * @param output 
     */
    public ProcRunner(Connection connection, SiebelPropertySet input, SiebelPropertySet output) {
        this.connection = connection;
        this.input = input;
        this.output = output;
    }    
    
    /**
     * 
     * @throws SQLException
     * @throws IOException 
     */
    public void queryProc() throws SQLException, IOException{
        
        String procedureName = this.input.getProperty(PROCEDURE_NAME);
        
        SiebelToEbsService stes = 
                new SiebelToEbsService(
                        connection, procedureName, new Object[]{
                            createXMLFromString(this.input.getValue())
                        }
                );
        
        // Convert clob to string and return to siebel
        StringBuilder stream = clobToString(
                (CLOB)stes.queryProc()
        );
        output.setValue(stream.toString());
        
    }
    
    /**
     * 
     * @param xmlString
     * @return
     * @throws SQLException 
     */
    private XMLType createXMLFromString(String xmlString) throws SQLException{
        
        
        OracleConnection oraCon = this.connection.unwrap(OracleConnection.class);
        return XMLType.createXML(oraCon, xmlString);
    }
    
    /**
     * 
     * @param stream
     * @return
     * @throws IOException
     * @throws SQLException 
     */
    private StringBuilder clobToString(CLOB stream) throws IOException, SQLException{
        Reader reader = stream.getCharacterStream();
        final StringBuilder sb = new StringBuilder();
        final BufferedReader br = new BufferedReader(reader);

        int b;
        while(-1 != (b = br.read()))
        {
            sb.append((char)b);
        }

        br.close();
        
        return sb;
    }

}

