package io.github.carolynkrell.registry;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.item.gun.Colt45Item;
import io.github.carolynkrell.item.armor.CowboyHatArmorItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ItemRegistry {
    public static final CowboyHatArmorItem COWBOY_HAT = new CowboyHatArmorItem(ArmorMaterials.CHAIN, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Colt45Item COLT_45 = new Colt45Item();

    public static void registerItems() {
        register("cowboy_hat", COWBOY_HAT);
        register("colt_45", COLT_45);
    }

    private static void register(String path, Item item) {
        Registry.register(Registry.ITEM, new Identifier(VariousWesternStuff.MOD_ID, path), item);
    }
}
