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

public class BO_MOD_DataProcessingException extends Exception {
/*
Exception for DataProcessing errors.
 */

    // constructors
    public BO_MOD_DataProcessingException(){
        super("undefined DataProcessing error");
    }

    //methods
    public BO_MOD_DataProcessingException(String ErrorMessage) {
        super(ErrorMessage);
    }
}
