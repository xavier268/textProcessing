/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.textprocessing;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Builds (or load if it exists) a word2vec model. Provide high level query
 * capabilities.
 *
 * @author xavier
 */
public class MyWord2VecModel {

    private static final Logger LOG = Logger.getLogger(MyWord2VecModel.class.getName());
    private static final String DATADIR = "data";
    private static final String MODELPATH = "myModel.model";
    private static final String WORDLISTPATH = "vocab.words";

    private static final List<String> STOPLIST = Arrays.asList(
            "copyright",
            "legifrance",
            "lalouette",
            "eyssette",
            "ier",
            "of",
            "the"
    );
    
    protected Word2Vec vec = null;

    public MyWord2VecModel() throws IOException {  
        

        if (Files.exists(Paths.get(MODELPATH))) {
            LOG.info("Loading existing model from file ...");
            vec = WordVectorSerializer.readWord2VecModel(MODELPATH);
        } else {
            vec = buildModel();
        }

        printModelStat();

    }

    private void printModelStat() {

        LOG.info("Stats for model " + MODELPATH);
        LOG.log(Level.INFO, "Vocabulary size : {0} words", vec.vocab().numWords());

    }

    /**
     * Build, fit and save model.
     *
     * @return
     * @throws FileNotFoundException
     */
    private Word2Vec buildModel() throws FileNotFoundException, IOException {

        // Here, a sentence is defined as a single line.            
        // SentenceIterator iter = new BasicLineIterator(DATADIR);        
        SentenceIterator iter = new MySentenceIterator(DATADIR);

        iter.setPreProcessor(new MySentencePreProcessor());

        // Tokennize by splitting on white spaces.
        TokenizerFactory t = new DefaultTokenizerFactory();

        // Use a preprocessor that will just lowercase everything and strip numbers and punctuation.
        t.setTokenPreProcessor(new CommonPreprocessor());

        LOG.info("Building model ...");
        // Build MODELPATH
        vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(150)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .stopWords(STOPLIST)
                .build();

        LOG.info("Fitting model ...");
        vec.fit();

        LOG.info("Saving model ... ");
        WordVectorSerializer.writeWord2VecModel(vec, MODELPATH);

        LOG.info("Saving sorted word list");
        try (BufferedWriter wr = Files.newBufferedWriter(Paths.get(WORDLISTPATH))) {
            List<String> sorted = new ArrayList<>(vec.vocab().words());
            Collections.sort(sorted);
            for (String w : sorted) {
                wr.write(w);
                wr.write("\n");
            }
        }
        return vec;
    }
    
    /**
     * Return a string representation of the result.
     * @param pos
     * @param neg
     * @param nb
     * @return 
     */
    public String q(List<String> pos, List<String> neg, int nb) { 
        if(pos.isEmpty() && neg.isEmpty()) return "You need at least a word ?";        
        return String.join(", ", vec.wordsNearest(pos, neg, nb)) + " ?";
    }
}