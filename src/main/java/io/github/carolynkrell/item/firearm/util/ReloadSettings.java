package io.github.carolynkrell.item.firearm.util;

import io.github.carolynkrell.item.firearm.ammo.AmmoItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;

import java.util.function.Predicate;

public class ReloadSettings {
    public int capacity;
    public Predicate<ItemStack> ammunition;
    public ReloadStyle reloadStyle;
    public int reloadTicks;

    public ReloadSettings(int capacity, TagKey<Item> ammoTag, ReloadStyle reloadStyle, int reloadTicks) {
        this.capacity = capacity;
        this.ammunition = stack -> stack.isIn(ammoTag);
        this.reloadStyle = reloadStyle;
        this.reloadTicks = reloadTicks;
    }

    public enum ReloadStyle {
        SINGLE,
        CLIP
    }
}
