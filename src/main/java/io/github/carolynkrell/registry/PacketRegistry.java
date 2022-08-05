package io.github.carolynkrell.registry;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.item.gun.GunItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class PacketRegistry {

    public static final Identifier GUN_INTERACT_PACKET_ID = new Identifier(VariousWesternStuff.MOD_ID, "gun_interact");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(GUN_INTERACT_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            Hand hand = buf.readEnumConstant(Hand.class);
            server.execute (() -> {
                ((GunItem) player.getStackInHand(hand).getItem()).attackGun(player.getWorld(), player, hand);
            });
        });
    }

    public static void registerS2CPackets() {

    }
}
