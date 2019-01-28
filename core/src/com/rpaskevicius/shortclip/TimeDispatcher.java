package com.rpaskevicius.shortclip;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TimeDispatcher {

    private List<TimeListener> listeners;

    private double bpm;

    private long startTime;
    private long runningTime;
    private long previousTime;

    private boolean isRunning;

    private long beatDuration;
    private long sequenceDuration;

    private long sequencePartial;

    public TimeDispatcher(float bpm) {
        this.bpm = (double)bpm;

        recalcDurations();

        listeners = new ArrayList<TimeListener>();
    }

    public void addListener(TimeListener listener) {
        listeners.add(listener);
    }

    public void start() {
        runningTime = 0;

        startTime = TimeUtils.nanoTime();

        isRunning = true;
    }

    public void stop() {
        isRunning = false;

        sequencePartial = 0; // comment out to get pause mechanic instead of stop mechanic
    }

    public void update() {
        if (isRunning) {
            previousTime = runningTime;
            runningTime = TimeUtils.timeSinceNanos(startTime);

            long delta = runningTime - previousTime;

            sequencePartial += delta;
            if (sequencePartial >= sequenceDuration) {
                sequencePartial -= sequenceDuration;
            }

            dispatchStepEvents();
            dispatchMarkerPositions();
        }
    }

    public void dispatchStepEvents() {
        for (TimeListener listener : listeners) {
            //long stepDuration = sequenceDuration / listener.getStepCount();
            //int stepIndex = (int) (sequencePartial / stepDuration);

            double stepDuration = (double)sequenceDuration / (double)listener.getStepCount();
            int stepIndex = (int) ((double)sequencePartial / stepDuration);

            if (stepIndex > 15) { stepIndex = 15; }

            //System.out.println(sequencePartial + " " + stepIndex);

            if (stepIndex != listener.getCurrentIndex()) {
                listener.onNextStep(stepIndex); //TODO still sometimes gives ArrayIndexOutOfBoundsException
            }
        }
    }

    public void dispatchMarkerPositions() {
        for (TimeListener listener : listeners) {
            float ratio = (float) sequencePartial / (float) sequenceDuration;

            listener.onNextMarkerPosition(ratio);
        }
    }

    public void setSequencePartial(long sequencePartial) {
        this.sequencePartial = sequencePartial;
    }

    public float getBpm() {
        return (float)bpm;
    }

    public void setBpm(float bpm) {
        this.bpm = (double)bpm;
        recalcDurations();
    }

    public void recalcDurations() {
        //beatDuration = (long) (60000.0f / bpm); //how long does a single beat last in millis.
        //sequenceDuration = beatDuration * 4; //how long does entire sequence last in millis. 4 -> 4 beats

        beatDuration = (long) (60000000000.0 / bpm); //how long does a single beat last in nanos.
        sequenceDuration = beatDuration * 4; //how long does entire sequence last in nanos. 4 -> 4 beats
    }
}
