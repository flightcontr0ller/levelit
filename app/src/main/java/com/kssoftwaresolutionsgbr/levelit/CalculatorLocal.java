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
    public float angle;
    public Alignment alignment;
    private float accX, accY, accZ;

    // constructors
    public CalculatorLocal() {
        angle = 0;
        alignment = Alignment.NOTDEFIEND;
    }

    // methods
    public void get_data(float mx, float my, float mz){
        accX = mx;
        accY = my;
        accZ = mz;

        get_alignment();
        get_angle();
    }

    private void get_alignment() {
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
        }
    }
    private void get_angle(){
        angle = 42;
    }
}
