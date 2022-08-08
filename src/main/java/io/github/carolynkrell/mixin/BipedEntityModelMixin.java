package io.github.carolynkrell.mixin;

import io.github.carolynkrell.item.firearm.util.FirearmTypes;
import io.github.carolynkrell.util.PlayerFirearmPose;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart head;

    @Shadow @Final public ModelPart leftArm;

    @Shadow public BipedEntityModel.ArmPose rightArmPose;

    @Shadow public BipedEntityModel.ArmPose leftArmPose;

    @Inject(method = "positionRightArm(Lnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private void poseRightFiringArm(LivingEntity entity, CallbackInfo ci) {
        if (this.rightArmPose == FirearmTypes.ONE_HANDED.getPose(PlayerFirearmPose.ADS)) {
            this.rightArm.pitch = -1.614429f + this.head.pitch;
            this.rightArm.yaw = -0.06108f + this.head.yaw;
            this.leftArm.pitch = this.leftArm.pitch + 0.08726f;
            this.leftArm.yaw = this.head.yaw;
            ci.cancel();
        }
    }

    @Inject(method = "positionLeftArm(Lnet/minecraft/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private void poseLeftFiringArm(LivingEntity entity, CallbackInfo ci) {
        if (this.leftArmPose == FirearmTypes.ONE_HANDED.getPose(PlayerFirearmPose.ADS)) {
            this.leftArm.pitch = -1.614429f + this.head.pitch;
            this.leftArm.yaw = -0.06108f + this.head.yaw;
            this.rightArm.pitch = this.rightArm.pitch + 0.08726f;
            this.rightArm.yaw = this.head.yaw;
            ci.cancel();
        }
    }
}
