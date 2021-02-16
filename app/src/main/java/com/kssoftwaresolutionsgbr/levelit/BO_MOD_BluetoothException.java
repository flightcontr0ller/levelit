/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        BluetoothException
 * dev:          Malte Schoenert
 * created on:   2021-02-10
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

public class BO_MOD_BluetoothException extends Exception {
/*
Exception for bluetooth errors.
 */

    // constructors
    public BO_MOD_BluetoothException(){
        super("undefined bluetooth error");
    }

    //methods
    public BO_MOD_BluetoothException(String ErrorMessage) {
        super(ErrorMessage);
    }
}
