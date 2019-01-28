/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.helper;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;

public class FileToText {
    private String stringOutput = "";
    private final StringWriter errors = new StringWriter();
    private Scanner sc = null;

    public FileToText convertFileToText(String fileSource) throws SiebelBusinessServiceException {
        try {
            this.sc = new Scanner(new File(fileSource));
            while (this.sc.hasNextLine()) {
                this.stringOutput = this.stringOutput + this.sc.nextLine() + "\n";
            }
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace(new PrintWriter(this.errors));
            MyLogging.log(Level.SEVERE, "Caught File Not Found Exception:" + this.errors.toString());
            throw new SiebelBusinessServiceException("FILE_NOT_FOUND_EXCPT", ex.getMessage());
        }
        finally {
            this.sc.close();
        }
        return this;
    }

    public String getStringOutput() {
        return this.stringOutput;
    }
}

