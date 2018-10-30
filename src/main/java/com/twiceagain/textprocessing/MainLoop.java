/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.textprocessing;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main entry loop to query the model.
 *
 * @author xavier
 */
public class MainLoop {

    /**
     * Nb of results to provide.
     */
    public static int nbresult = 6;

    /**
     * Main loop to query the model.
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        MyWord2VecModel mm = new MyWord2VecModel();

        System.out.println("\nEnter your query."
                + "\nPrefix words with plus(optional) or minus."
                + "\nUse space or newlines as delimiters."
                + "\nType '=' to get the result(s)"
                + "\nType --- to finish \n");

        // This is required, because the terminal is set to this charset (and we need utf-8 inside)
        Scanner scanner = new Scanner(System.in, Charset.forName("ISO-8859-1"));
        List<String> pos = new ArrayList<>();
        List<String> neg = new ArrayList<>();

        while (scanner.hasNext()) {
            String q = scanner.next();
            q = q.trim();

            if (q.startsWith("---")) {
                System.out.println("Bye !\n");
                break;
            }
            if (q.startsWith("+")) {
                pos.add(q.substring(1));
                continue;
            }
            if (q.startsWith("-")) {
                neg.add(q.substring(1));
                continue;
            }
            if (q.startsWith("=")) {
                System.out.printf("+> %s\n-> %s\n=> %s\n",
                        pos.toString(),
                        neg.toString(),
                        mm.q(pos, neg, nbresult));
                pos.clear();
                neg.clear();
                continue;
            }
            if (!q.isEmpty()) {
                pos.add(q);
            }

        }

    }

}
