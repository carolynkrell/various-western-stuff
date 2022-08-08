package io.github.carolynkrell.item.firearm;

import io.github.carolynkrell.item.firearm.ammo.Winchester44CartridgeItem;
import io.github.carolynkrell.item.firearm.util.FirearmSettings;
import io.github.carolynkrell.item.firearm.util.FirearmTypes;
import io.github.carolynkrell.item.firearm.util.RecoilSettings;
import io.github.carolynkrell.item.firearm.util.ReloadSettings;
import io.github.carolynkrell.registry.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ColtFrontierItem extends FirearmItem {

    private static final RecoilSettings RECOIL_SETTINGS = new RecoilSettings(25.0f, 0f, 5, 5.0f, 1.5f);
    private static final ReloadSettings RELOAD_SETTINGS = new ReloadSettings(6, TagRegistry.COLT_FRONTIER_AMMO, ReloadSettings.ReloadStyle.SINGLE, 5);

    public ColtFrontierItem() {
        super(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1),
                new FirearmSettings(RELOAD_SETTINGS, RECOIL_SETTINGS, FirearmSettings.CockStyle.EVERY_SHOT, FirearmTypes.ONE_HANDED));
    }
}
