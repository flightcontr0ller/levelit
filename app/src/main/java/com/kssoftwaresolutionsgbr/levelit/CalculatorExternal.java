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
    public String getAngle(String rxData){
        try{
            return checkData(rxData);
        } catch (Exception e){
            return "faulty data";
        }
    }

    private String checkData(String rxData) throws Exception{
        try{
            Float angle = Float.parseFloat(rxData.trim());
            if (angle <= 180 && angle >= -180){
                return angle.toString();
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
