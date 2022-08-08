package io.github.carolynkrell.registry;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.item.firearm.ColtFrontierItem;
import io.github.carolynkrell.item.armor.CowboyHatArmorItem;
import io.github.carolynkrell.item.firearm.ammo.Winchester44CartridgeItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ItemRegistry {
    public static final ColtFrontierItem COLT_FRONTIER = new ColtFrontierItem();
    public static final CowboyHatArmorItem COWBOY_HAT = new CowboyHatArmorItem(ArmorMaterials.CHAIN, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Winchester44CartridgeItem WINCHESTER_44_CARTRIDGE = new Winchester44CartridgeItem(new Item.Settings());

    public static void registerItems() {
        register("colt_frontier", COLT_FRONTIER);
        register("cowboy_hat", COWBOY_HAT);
        register("winchester_44_cartridge", WINCHESTER_44_CARTRIDGE);
    }

    private static void register(String path, Item item) {
        Registry.register(Registry.ITEM, new Identifier(VariousWesternStuff.MOD_ID, path), item);
    }
}
