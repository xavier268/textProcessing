/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.textprocessing;

import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;

/**
 *
 * @author xavier
 */
public class MySentencePreProcessor implements SentencePreProcessor {

    @Override
    public synchronized String preProcess(String sentence) {
        return sentence
                .replaceAll("--+|[\\r\\n#\\$\\\\%\\-%&@«°*+»'`_=–)(\\[\\]\\{\\}\\<\\>]+", " ");
    }

}
