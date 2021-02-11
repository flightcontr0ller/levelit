/*
 ************************************************
 * project:      levelit
 * package name: com.kssoftwaresolutionsgbr.levelit
 * class:        DataProcessingExternal
 * dev:          Malte Schoenert
 * created on:   2021-02-09
 ************************************************
 */

package com.kssoftwaresolutionsgbr.levelit;

public class DataProcessing {
/*
This class checks the values from the external sensor.
 */

    // constructors
    public DataProcessing(){

    }

    // methods
    public Float getAngle(String rxData) throws DataProcessingException {
    /*
    This method checks the values from the external sensor.
     */

        try{
            Float angle = Float.parseFloat(rxData.trim());
            if (angle <= 180 && angle >= -180){
                return angle;
            }
            else {
                throw new DataProcessingException("Error: values from external sensor are faulty");
            }
        } catch (Exception e){
            throw e;
        }
    }

    public Float getAngle(float mx, float my) throws DataProcessingException {
    /*
    This method checks the values from the local sensor.
     */

        try{
            return calcAngle(getAlignment(mx, my));
        } catch (DataProcessingException e){
            throw e;
        }
    }

    private Alignment getAlignment(float accX, float accY) {
        /*
        This method calculate the alignment of the local device.
         */

        if (accX >= 0 && accY >= 0) {
            if (accY > accX) {
                return Alignment.UPWARD;
            } else if (accY < accX) {
                return Alignment.LEFTWARD;
            }
        } else if (accX >= 0 && accY < 0) {
            if (accX > Math.abs(accY)) {
                return Alignment.LEFTWARD;
            } else if (accX < Math.abs(accY)) {
                return Alignment.DOWNWARD;
            }
        } else if (accX < 0 && accY < 0) {
            if (accX > accY) {
                return Alignment.DOWNWARD;
            } else if (accX < accY) {
                return Alignment.RIGHTWARD;
            }
        } else if (accX < 0 && accY >= 0) {
            if (Math.abs(accX) > accY) {
                return Alignment.RIGHTWARD;
            } else if (Math.abs(accX) < accY) {
                return Alignment.UPWARD;
            }
        }

        return Alignment.NOTDEFIEND;
    }

    private Float calcAngle(Alignment currentAlignment) throws DataProcessingException{
    /*
    This method calculate the Angle depending on the alignment of the local device.
     */

        if(currentAlignment == Alignment.NOTDEFIEND){
            throw new DataProcessingException("Error: can't get current alignment");
        }
        else{
            /* insert calculation here
            if(angle < 45 && angle > -45){}
            else {
                throw new DataProcessingException("Error: values of local accelerometer are faulty");
            }
             */
            if (currentAlignment == Alignment.LEFTWARD){
                return Float.valueOf(30);
            }
            else if (currentAlignment == Alignment.RIGHTWARD){
                return Float.valueOf(-20);
            }
            else {
                return Float.valueOf(42);
            }
        }
    }

}
