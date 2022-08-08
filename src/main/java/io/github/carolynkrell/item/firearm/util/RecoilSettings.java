package io.github.carolynkrell.item.firearm.util;

public class RecoilSettings {
    public float verticalStrength;
    public float horizontalStrength;
    public int duration;
    public float maxVerticalSpread;
    public float maxHorizontalSpread;

    public RecoilSettings(float verticalStrength, float horizontalStrength, int duration, float maxVerticalSpread, float maxHorizontalSpread) {
        this.verticalStrength = verticalStrength;
        this.horizontalStrength = horizontalStrength;
        this.duration = duration;
        this.maxVerticalSpread = maxVerticalSpread;
        this.maxHorizontalSpread = maxHorizontalSpread;
    }
}
