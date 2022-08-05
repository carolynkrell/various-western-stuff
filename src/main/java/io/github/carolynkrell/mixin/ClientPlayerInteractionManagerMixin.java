package io.github.carolynkrell.mixin;

import io.github.carolynkrell.accessor.ClientPlayerInteractionManagerAccessor;
import io.github.carolynkrell.item.gun.GunItem;
import io.github.carolynkrell.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements ClientPlayerInteractionManagerAccessor {
    @Shadow protected abstract void syncSelectedSlot();

    @Shadow @Final private ClientPlayNetworkHandler networkHandler;
    @Shadow private GameMode gameMode;

    public void interactGun(PlayerEntity player, Hand hand) {
        this.syncSelectedSlot();
        ClientPlayNetworking.send(PacketRegistry.GUN_INTERACT_PACKET_ID, PacketByteBufs.create().writeEnumConstant(hand));
        if (this.gameMode != GameMode.SPECTATOR) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof GunItem) {
                ((GunItem) stack.getItem()).attackGun(player.getWorld(), player, hand);
            }
        }
    }
}
