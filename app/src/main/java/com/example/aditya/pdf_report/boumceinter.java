package com.example.aditya.pdf_report;

import android.view.animation.Interpolator;

public class boumceinter implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    boumceinter(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
