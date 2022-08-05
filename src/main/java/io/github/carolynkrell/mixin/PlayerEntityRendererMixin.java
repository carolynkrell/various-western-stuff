package io.github.carolynkrell.mixin;


import com.chocohead.mm.api.ClassTinkerers;
import io.github.carolynkrell.accessor.PlayerEntityGunAccessor;
import io.github.carolynkrell.item.gun.GunItem;
import io.github.carolynkrell.registry.TagRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getArmPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("HEAD"), cancellable = true)
    private static void getFirearmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (((PlayerEntityGunAccessor) player).isAimingDownSights()) {
            cir.setReturnValue(ClassTinkerers.getEnum(BipedEntityModel.ArmPose.class, "ONE_HANDED_FIREARM"));
        }
    }
}
