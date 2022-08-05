package io.github.carolynkrell.mixin;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.accessor.PlayerEntityGunAccessor;
import io.github.carolynkrell.registry.TagRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);
    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow @Final private MinecraftClient client;

    @Inject(method= "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD,cancellable = true)
    public void renderAimDownSights(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, boolean bl, Arm arm) {
        if (((PlayerEntityGunAccessor) player).isAimingDownSights()) {
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            float animation = 1.0f;
            if (player.getItemUseTimeLeft() > 0) {
                float t = ((float) item.getMaxUseTime() - ((float) this.client.player.getItemUseTimeLeft() - tickDelta + 1.1f));
                float p = t / (float) item.getMaxUseTime();
                animation = easeOutExpo(p);
            }
            matrices.translate(i * -0.31f * animation, 0, 0);
            this.applySwingOffset(matrices, arm, swingProgress);
            this.applyEquipOffset(matrices, arm, 0f);
            this.renderItem(player, item, bl3 ? ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND : ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND, !bl3, matrices, vertexConsumers, light);
            matrices.pop();
            ci.cancel();
        }
    }

    @Inject(method = "getHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;", at = @At("HEAD"), cancellable = true)
    private static void VWS$CheckIfGun(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir) {
        Hand hand = player.getActiveHand();
        if (((PlayerEntityGunAccessor) player).isAimingDownSights()) {
            cir.setReturnValue(HeldItemRenderer.HandRenderType.shouldOnlyRender(hand));
        }
    }


    // https://easings.net/#easeOutBack
    private static float easeOutBack(float progress) {
        final float c1 = 1.70158f;
        return (progress -= 1.0f) * progress * ((c1 + 1.0f) * progress + c1) + 1f;
    }

    private static float easeOutExpo(float progress) {
        return progress == 1.0f ? 1.0f : (float) (1.0f - Math.pow(2.0f, -10.0f * progress));
    }

}
