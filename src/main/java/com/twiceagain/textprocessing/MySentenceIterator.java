/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.textprocessing;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;

/**
 * Read sentences from a directory of files. Sentences are defined as ending
 * with a 'dot'. Lines are ignored. Key methods are synchronized.
 *
 * @author xavier
 */
public final class MySentenceIterator implements SentenceIterator {

    protected SentencePreProcessor proc;
    protected Path dirPath;
    protected Scanner scanner;
    private static final Logger LOG = Logger.getLogger(MySentenceIterator.class.getName());

    public MySentenceIterator(String dir) throws IOException {
        LOG.info("Constructing sentence iterator");
        dirPath = Paths.get(dir);
        proc = null;
        reset();
        LOG.info("Sentence interator constructed for " + dirPath.toAbsolutePath().toString());
    }

    @Override
    public synchronized String nextSentence() {
        return (proc == null) ? _nextSentence() : proc.preProcess(_nextSentence());        
    }

    protected String _nextSentence() {
        if (scanner.hasNext()) {
            return scanner.next() + ".";
        }
        return null;
    }

    @Override
    public synchronized boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public void finish() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }

    }

    @Override
    public SentencePreProcessor getPreProcessor() {
        return proc;
    }

    @Override
    public void setPreProcessor(SentencePreProcessor arg0) {
        proc = arg0;
    }

    @Override
    public synchronized void  reset() {
        if (scanner != null) {
            scanner.close();
        }

        if (Files.isDirectory(dirPath)) {

            List<InputStream> lis;
            try {
                lis = Files.list(dirPath)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".utf8"))
                        .map((Path p) -> {
                            try {
                                LOG.log(Level.INFO, "Processing file {0}", p.toString());
                                return Files.newInputStream(p);
                            } catch (IOException ex) {
                                Logger.getLogger(MySentenceIterator.class.getName()).log(Level.SEVERE, null, ex);
                                return null;
                            }
                        })
                        .collect(Collectors.toList());
            } catch (IOException ex) {
                Logger.getLogger(MySentenceIterator.class.getName()).log(Level.SEVERE, null, ex);
                lis = new ArrayList<>(); // empty list
            }
            InputStream is = new SequenceInputStream(Collections.enumeration(lis));
            scanner = new Scanner(is).useDelimiter("\\.");

        } else {
            try {
                scanner = new Scanner(dirPath).useDelimiter("\\.");
            } catch (IOException ex) {
                Logger.getLogger(MySentenceIterator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
