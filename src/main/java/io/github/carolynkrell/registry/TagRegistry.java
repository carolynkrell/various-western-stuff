package io.github.carolynkrell.registry;

import io.github.carolynkrell.VariousWesternStuff;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TagRegistry {
    public static final TagKey<Item> VWS_FIREARMS = getItemTagKey("firearms");
    public static final TagKey<Item> COLT_FRONTIER_AMMO = getItemTagKey("colt_frontier.ammo");

    public static TagKey<Item> getItemTagKey(String key) {
        return TagKey.of(Registry.ITEM_KEY, new Identifier(VariousWesternStuff.MOD_ID, key));
    }
}