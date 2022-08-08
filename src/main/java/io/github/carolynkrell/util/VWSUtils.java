package io.github.carolynkrell.util;

import io.github.carolynkrell.registry.TagRegistry;
import net.minecraft.item.ItemStack;

public class VWSUtils {
    public static boolean isVWSFirearm(ItemStack stack) {
        return stack.isIn(TagRegistry.VWS_FIREARMS);
    }
}
