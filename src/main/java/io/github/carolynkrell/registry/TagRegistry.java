package io.github.carolynkrell.registry;

import io.github.carolynkrell.VariousWesternStuff;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TagRegistry {
    public static final TagKey<Item> VWS_GUNS = TagKey.of(Registry.ITEM_KEY, new Identifier(VariousWesternStuff.MOD_ID, "guns"));
}