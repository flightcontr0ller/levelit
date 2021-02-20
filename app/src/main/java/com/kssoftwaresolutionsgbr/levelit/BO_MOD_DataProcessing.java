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

import java.lang.Math;

public class BO_MOD_DataProcessing {
/*
This class checks the values from the external sensor.
 */

    // constructors
    public BO_MOD_DataProcessing() {

    }

    // methods
    public Float getAngle(String rxData) throws BO_MOD_DataProcessingException {
    /*
    This method checks the values from the external sensor.
     */

        try {
            Float angle = Float.parseFloat(rxData.trim());
            if (angle <= 180 && angle >= -180) {
                return angle;
            } else {
                throw new BO_MOD_DataProcessingException("values from external sensor are faulty");
            }
        } catch (Exception e) {
            throw new BO_MOD_DataProcessingException("values from external sensor are faulty");
        }

    }

    public float getAngle(float mx, float my) throws BO_MOD_DataProcessingException {
    /*
    This method checks the values from the local sensor.
     */

        try {
            return calcAngle(getAlignment(mx, my), mx, my);
        } catch (BO_MOD_DataProcessingException e) {
            throw e;
        }
    }

    private BO_MOD_Alignment getAlignment(float accX, float accY) {
        /*
        This method calculate the alignment of the local device.
         */

        if (accX >= 0 && accY >= 0) {
            if (accY > accX) {
                return BO_MOD_Alignment.UPWARD;
            } else if (accY < accX) {
                return BO_MOD_Alignment.LEFTWARD;
            }
        } else if (accX >= 0 && accY < 0) {
            if (accX > Math.abs(accY)) {
                return BO_MOD_Alignment.LEFTWARD;
            } else if (accX < Math.abs(accY)) {
                return BO_MOD_Alignment.DOWNWARD;
            }
        } else if (accX < 0 && accY < 0) {
            if (accX > accY) {
                return BO_MOD_Alignment.DOWNWARD;
            } else if (accX < accY) {
                return BO_MOD_Alignment.RIGHTWARD;
            }
        } else if (accX < 0 && accY >= 0) {
            if (Math.abs(accX) > accY) {
                return BO_MOD_Alignment.RIGHTWARD;
            } else if (Math.abs(accX) < accY) {
                return BO_MOD_Alignment.UPWARD;
            }
        }

        return BO_MOD_Alignment.NOTDEFIEND;
    }

    private float calcAngle(BO_MOD_Alignment Alignment, float mx, float my) throws BO_MOD_DataProcessingException {
    /*
    This method calculate the Angle depending on the alignment of the local device.
     */
        Double angle = null;
        final double gravity = 9.81; //standard acceleration on earth

        if (Alignment == BO_MOD_Alignment.NOTDEFIEND) {
            throw new BO_MOD_DataProcessingException("can't get current alignment");
        } else {
            if (Alignment == BO_MOD_Alignment.UPWARD) {
                angle = Math.toDegrees(Math.asin(mx / gravity));
                angle = -angle; //correction for left rotation
            } else if (Alignment == BO_MOD_Alignment.LEFTWARD | Alignment == BO_MOD_Alignment.DOWNWARD) {
                angle = Math.toDegrees(Math.asin(my / gravity));
            } else if (Alignment == BO_MOD_Alignment.RIGHTWARD | Alignment == BO_MOD_Alignment.DOWNWARD) {
                angle = Math.toDegrees(Math.asin(my / gravity));
                angle = -angle; //correction for left rotation
            }
            if (angle < 45 && angle > -45) {
                return (float)Math.round(angle);
            } else {
                throw new BO_MOD_DataProcessingException("Error: values of local accelerometer are faulty");
            }
        }

    }
}
