package io.github.carolynkrell.accessor;

import io.github.carolynkrell.util.PlayerFirearmPose;
import io.github.carolynkrell.item.firearm.util.RecoilSettings;

public interface PlayerEntityAccessor {
    PlayerFirearmPose getFirearmPose();
    void setFirearmPose(PlayerFirearmPose pose);
    void applyRecoil(RecoilSettings recoilSettings);
    boolean isAimingDownSights();
    void triggerReload();
}
