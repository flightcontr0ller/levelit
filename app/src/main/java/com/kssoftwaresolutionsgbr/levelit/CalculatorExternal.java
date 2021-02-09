/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        CalculatorExternal
 * dev:          Malte Schoenert
 * created on:   2021-02-09
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

public class CalculatorExternal {

    // fields
    public float angle;

    // constructors
    public CalculatorExternal(){
        angle = 0;
    }

    // methods
    public void get_data(String rxData) throws Exception{
        try{
            angle = Integer.parseInt(rxData);
        }
        catch (Exception e){
            angle = 0;
        }
    }
}
