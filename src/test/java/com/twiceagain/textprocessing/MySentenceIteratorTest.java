/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.textprocessing;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author xavier
 */
public class MySentenceIteratorTest {

    private static final String FILEPATH = "data";

    
    @Ignore
    @org.junit.Test
    public void testA() throws IOException {

        MySentenceIterator it = new MySentenceIterator(FILEPATH);
        while (it.hasNext()) {
            System.out.println("\n============\n" + it.nextSentence());
        }
    }

}
