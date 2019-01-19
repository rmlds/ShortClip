package com.rpaskevicius.shortclip;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TimeDispatcher {

    private List<SequencerActor> listeners;

    private float bpm;

    private long startTime;
    private long runningTime;
    private long previousTime;

    private boolean isRunning;

    private long beatDuration;
    private long sequenceDuration;

    private long sequencePartial;

    public TimeDispatcher(float bpm) {
        this.bpm = bpm;

        recalcDurations();

        listeners = new ArrayList<SequencerActor>();
    }

    public void addListener(SequencerActor listener) {
        listeners.add(listener);
    }

    public void start() {
        runningTime = 0;

        startTime = TimeUtils.millis();

        isRunning = true;
    }

    public void stop() {
        isRunning = false;

        sequencePartial = 0; // comment out to get pause mechanic instead of stop mechanic
    }

    public void update() {
        if (isRunning) {
            previousTime = runningTime;
            runningTime = TimeUtils.timeSinceMillis(startTime);

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
        for (SequencerActor listener : listeners) {
            long stepDuration = sequenceDuration / listener.getStepCount();

            int stepIndex = (int) (sequencePartial / stepDuration);

            if (stepIndex > 15) { stepIndex = 15; }

            //System.out.println(sequencePartial + " " + stepIndex);

            if (stepIndex != listener.getCurrentIndex()) {
                listener.onNextStep(stepIndex); //TODO still sometimes gives ArrayIndexOutOfBoundsException
            }
        }
    }

    public void dispatchMarkerPositions() {
        for (SequencerActor listener : listeners) {
            listener.onNextMarkerPosition(sequencePartial, sequenceDuration);
        }
    }

    public void setSequencePartial(long sequencePartial) {
        this.sequencePartial = sequencePartial;
    }

    public float getBpm() {
        return bpm;
    }

    public void setBpm(float bpm) {
        this.bpm = bpm;
        recalcDurations();
    }

    public void recalcDurations() {
        beatDuration = (long) ((60.0f / bpm) * 1000); //how long does a single beat last in millis.
        sequenceDuration = beatDuration * 4; //how long does entire sequence last in millis. 4 -> 4 beats
    }
}
