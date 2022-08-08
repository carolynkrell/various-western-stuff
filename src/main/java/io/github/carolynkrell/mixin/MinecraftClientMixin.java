package io.github.carolynkrell.mixin;

import io.github.carolynkrell.accessor.ClientPlayerInteractionManagerAccessor;
import io.github.carolynkrell.accessor.PlayerEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;
    @Shadow public ClientPlayerEntity player;
    @Shadow public ClientWorld world;

    @Inject(method = "doAttack()Z", at = @At("HEAD"), cancellable = true)
    public void shootGun(CallbackInfoReturnable<Boolean> cir) {
        if (((PlayerEntityAccessor) this.player).isAimingDownSights()) {
            ((ClientPlayerInteractionManagerAccessor) this.interactionManager).interactGun(this.player, player.getActiveHand());
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "handleInputEvents()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean isUsingNotADS(ClientPlayerEntity clientPlayerEntity) {
        if (((PlayerEntityAccessor) clientPlayerEntity).isAimingDownSights()) {
            if (!((MinecraftClient) (Object) this).options.useKey.isPressed()) {
                this.interactionManager.stopUsingItem(this.player);
            }
            return false;
        }
        return clientPlayerEntity.isUsingItem();
    }
}
