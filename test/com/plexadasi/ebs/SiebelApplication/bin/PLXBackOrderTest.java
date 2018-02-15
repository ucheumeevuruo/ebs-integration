/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.SiebelApplication.bin;

import com.siebel.data.SiebelBusComp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SAP Training
 */
public class PLXBackOrderTest {
    
    public PLXBackOrderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of searchSpec method, of class PLXBackOrder.
     */
    @Test
    public void testSearchSpec() throws Exception {
        System.out.println("searchSpec");
        SiebelBusComp sbBC = null;
        PLXBackOrder instance = null;
        instance.searchSpec(sbBC);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExtraParam method, of class PLXBackOrder.
     */
    @Test
    public void testGetExtraParam() {
        System.out.println("getExtraParam");
        SiebelBusComp sbBC = null;
        PLXBackOrder instance = null;
        instance.getExtraParam(sbBC);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
