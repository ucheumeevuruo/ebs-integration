/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.Helper;

import com.plexadasi.ebs.SiebelApplication.MyLogging;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SAP Training
 */
public class FileToText {
    private String stringOutput = "";
    private final StringWriter errors = new StringWriter();
    private Scanner sc = null;
    
    public FileToText convertFileToText(String fileSource) throws SiebelBusinessServiceException
    {
        try 
        {
            sc = new Scanner(new File(fileSource));
            while(sc.hasNextLine()){
                stringOutput += (sc.nextLine()) + "\n";
            }
        } 
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace(new PrintWriter(errors));
            MyLogging.log(Level.SEVERE, "Caught File Not Found Exception:"+errors.toString());
            throw new SiebelBusinessServiceException("FILE_NOT_FOUND_EXCPT", ex.getMessage());
        }
        finally
        {
            sc.close();
        }
        return this;
    }
    
    public String getStringOutput()
    {
        return stringOutput;
    }
    
    
}
