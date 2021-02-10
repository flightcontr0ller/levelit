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

public class BluetoothException extends Exception {

    // constructors
    public BluetoothException(){
        super("undefined bluetooth error");
    }

    public BluetoothException(String ErrorMessage){
        super(ErrorMessage);
    }

}
