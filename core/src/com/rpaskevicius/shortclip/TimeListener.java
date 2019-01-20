package com.rpaskevicius.shortclip;

public interface TimeListener {

    public void onNextStep(int stepIndex);
    public void onNextMarkerPosition(float ratio);

    public int getStepCount();
    public int getCurrentIndex();

}
