package io.github.carolynkrell.util;

public enum PlayerFirearmPose {
    NONE,
    ADS,
    RELOAD;

    public static PlayerFirearmPose get(Integer ordinal) {
        PlayerFirearmPose[] values = PlayerFirearmPose.values();
        if (ordinal < values.length && ordinal >= 0)
            return PlayerFirearmPose.values()[ordinal];
        return PlayerFirearmPose.NONE;
    }
}
