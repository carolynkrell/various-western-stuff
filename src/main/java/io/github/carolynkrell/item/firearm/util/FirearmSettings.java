package io.github.carolynkrell.item.firearm.util;

public class FirearmSettings {
    public ReloadSettings reloadSettings;
    public CockStyle cockStyle;
    public RecoilSettings recoilSettings;
    public FirearmType firearmType;

    public FirearmSettings(ReloadSettings reloadSettings, RecoilSettings recoilSettings, CockStyle cockStyle, FirearmType firearmType) {
        this.reloadSettings = reloadSettings;
        this.cockStyle = cockStyle;
        this.recoilSettings = recoilSettings;
        this.firearmType = firearmType;
    }

    public enum CockStyle {
        EVERY_SHOT,
        AFTER_RELOAD
    }
}


