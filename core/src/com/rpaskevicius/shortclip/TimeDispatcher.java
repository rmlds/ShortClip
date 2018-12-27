package com.rpaskevicius.shortclip;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TimeDispatcher {

    private List<SequencerActor> listeners;

    private float bpm;

    private long startTime;
    private long runningTime;

    private boolean isRunning;

    private long beatDuration;
    private long sequenceDuration;

    private long sequencePartial;

    public TimeDispatcher(float bpm) {
        this.bpm = bpm;

        beatDuration = (long) ((60.0f / bpm) * 1000); //how long does a single beat last in millis.
        sequenceDuration = beatDuration * 4; //how long does entire sequence last in millis. 4.0f - 4 beats

        listeners = new ArrayList<SequencerActor>();
    }

    public void addListener(SequencerActor listener) {
        listeners.add(listener);
    }

    public void start() {
        startTime = TimeUtils.millis();

        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void update() {
        if (isRunning) {
            long delta = TimeUtils.timeSinceMillis(startTime) - runningTime;

            runningTime = TimeUtils.timeSinceMillis(startTime);

            sequencePartial += delta;
            if (sequencePartial >= sequenceDuration) {
                sequencePartial -= sequenceDuration;
            }

            dispatchStepEvents();
        }
    }

    public void dispatchStepEvents() {
        for (SequencerActor listener : listeners) {
            long stepDuration = sequenceDuration / listener.getStepCount();

            int stepIndex = (int) (sequencePartial / stepDuration);

            //System.out.println(sequencePartial + " " + stepIndex);

            if (stepIndex != listener.getCurrentIndex()) {
                listener.onNextStep(stepIndex); //TODO still sometimes gives ArrayIndexOutOfBoundsException
            }
        }
    }
}
