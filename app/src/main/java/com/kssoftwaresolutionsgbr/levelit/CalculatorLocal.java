/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        CalculatorLocal
 * dev:          Malte Schoenert
 * created on:   2021-02-08
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

public class CalculatorLocal {

    // fields
    private float angle;
    private Alignment alignment;

    // constructors
    public CalculatorLocal() {
        angle = 0;
        alignment = Alignment.NOTDEFIEND;
    }

    // methods
    public String getAngle(float mx, float my) throws Exception {
        try{
            getAlignment(mx, my);
            calcAngle();
            return String.valueOf(angle);
        } catch (Exception e){
            throw e;
        }
    }

    private void getAlignment(float accX, float accY) throws Exception{
        if (accX >= 0 && accY >= 0) {
            if (accY > accX) {
                alignment = Alignment.UPWARD;
            } else if (accY < accX) {
                alignment = Alignment.LEFTWARD;
            }
        } else if (accX >= 0 && accY < 0) {
            if (accX > Math.abs(accY)) {
                alignment = Alignment.LEFTWARD;
            } else if (accX < Math.abs(accY)) {
                alignment = Alignment.DOWNWARD;
            }
        } else if (accX < 0 && accY < 0) {
            if (accX > accY) {
                alignment = Alignment.DOWNWARD;
            } else if (accX < accY) {
                alignment = Alignment.RIGHTWARD;
            }
        } else if (accX < 0 && accY >= 0) {
            if (Math.abs(accX) > accY) {
                alignment = Alignment.RIGHTWARD;
            } else if (Math.abs(accX) < accY) {
                alignment = Alignment.UPWARD;
            }
        } else{
            alignment = Alignment.NOTDEFIEND;
            throw new Exception();
        }
    }

    private void calcAngle() throws Exception{

        // insert calculation here ----
        if (alignment == Alignment.LEFTWARD){
            angle = 30;
        }
        else if (alignment == Alignment.RIGHTWARD){
            angle = -20;
        }
        else {
            angle = 42;
        }
        // ----------------------------

        if(angle < 45 && angle > -45){}
        else {
            throw new Exception();
        }
    }
}
