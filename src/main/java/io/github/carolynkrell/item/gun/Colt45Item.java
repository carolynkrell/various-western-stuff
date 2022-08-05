package io.github.carolynkrell.item.gun;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class Colt45Item extends GunItem {

    private static final RecoilSettings RECOIL_SETTINGS = new RecoilSettings(25.0f, 0f, 5, 5.0f, 1.5f);

    public Colt45Item() {
        super(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1), new GunSettings(ReloadStyle.SINGLE, 5, CockStyle.EVERY_SHOT, RECOIL_SETTINGS));
    }
}
