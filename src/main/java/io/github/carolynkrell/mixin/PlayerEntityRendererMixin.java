package io.github.carolynkrell.mixin;


import io.github.carolynkrell.accessor.PlayerEntityAccessor;
import io.github.carolynkrell.item.firearm.FirearmItem;
import io.github.carolynkrell.item.firearm.util.FirearmSettings;
import io.github.carolynkrell.item.firearm.util.FirearmType;
import io.github.carolynkrell.util.PlayerFirearmPose;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getArmPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("HEAD"), cancellable = true)
    private static void getFirearmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        PlayerFirearmPose firearmPose = ((PlayerEntityAccessor) player).getFirearmPose();
        if (firearmPose != PlayerFirearmPose.NONE) {
            FirearmType firearmType = ((FirearmItem) player.getMainHandStack().getItem()).getFirearmType();
            cir.setReturnValue(firearmType.getPose(firearmPose));
        }

    }
}
