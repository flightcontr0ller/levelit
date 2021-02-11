/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        DataProcessingException
 * dev:          Malte Schoenert
 * created on:   2021-02-11
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

public class DataProcessingException extends Exception {
/*
Exception for DataProcessing errors.
 */

    // constructors
    public DataProcessingException(){
        super("undefined DataProcessing error");
    }

    //methods
    public DataProcessingException(String ErrorMessage) {
        super(ErrorMessage);
    }
}
