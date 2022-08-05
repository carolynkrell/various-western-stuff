package io.github.carolynkrell.accessor;

import io.github.carolynkrell.item.gun.GunItem;

public interface PlayerEntityGunAccessor {
    void applyRecoil(GunItem.RecoilSettings recoilSettings);
    boolean isAimingDownSights();
    void triggerReload();
}
