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
    private String angle;

    // constructors
    public CalculatorExternal(){

    }

    // methods
    public Float getAngle(String rxData) throws Exception {
        try{
            return checkData(rxData);
        } catch (Exception e){
            throw e;
        }
    }

    private Float checkData(String rxData) throws Exception{
        try{
            Float angle = Float.parseFloat(rxData.trim());
            if (angle <= 180 && angle >= -180){
                return angle;
            }
            else {
                throw new Exception();
            }
        }
        catch (Exception e){
            throw e;
        }
    }
}
