package io.github.carolynkrell.mixin;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.accessor.PlayerEntityGunAccessor;
import io.github.carolynkrell.item.gun.GunItem;
import io.github.carolynkrell.registry.TagRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityGunAccessor {
    private GunItem.RecoilSettings currentRecoil;
    private int currentRecoilTick = 0;
    private float prevTickVerticalRecoil;
    private float prevTickHorizontalRecoil;
    private float currentVerticalSpread;
    private float currentHorizontalSpread;

    @Inject(method = "tickMovement()V", at = @At("HEAD"))
    public void doRecoil(CallbackInfo ci) {
        float recoilDuration;
        if (currentRecoil != null && (recoilDuration = currentRecoil.duration) > 0) {
            if (++currentRecoilTick > recoilDuration) {
                clearRecoil();
            }
            else {
                if (currentRecoilTick == 1) {
                    currentVerticalSpread = getVariance(currentRecoil.maxVerticalSpread);
                    this.prevTickVerticalRecoil = currentRecoil.verticalStrength + currentVerticalSpread;
                    currentHorizontalSpread = getVariance(currentRecoil.maxHorizontalSpread);
                    this.prevTickHorizontalRecoil = currentRecoil.horizontalStrength + currentHorizontalSpread;
                }
                PlayerEntity thisEntity = ((PlayerEntity) (Object) this);
                float verticalRecoil = getCurrentVerticalRecoil();
                float horizontalRecoil = getCurrentHorizontalRecoil();
                thisEntity.setPitch(thisEntity.getPitch() - verticalRecoil);
                thisEntity.setYaw(thisEntity.getYaw() + horizontalRecoil);
            }
        }
    }

    private float getVariance(float variance) {
        float random = ((PlayerEntity) (Object) this).getRandom().nextFloat() - 0.5f;
        VariousWesternStuff.log(Level.INFO, "variance: " + variance * random);
        return variance * random;
    }

    // https://gizma.com/easing/#expo2
    private float getCurrentVerticalRecoil() {
        float strength = currentRecoil.verticalStrength + currentVerticalSpread;
        float currentTickRecoil = getCurrentTickRecoil(strength, currentRecoil.duration);
        float baseRecoil = prevTickVerticalRecoil - currentTickRecoil;
        prevTickVerticalRecoil = currentTickRecoil;
        return baseRecoil;
    }

    private float getCurrentHorizontalRecoil() {
        float strength = currentRecoil.horizontalStrength + currentHorizontalSpread;
        float currentTickRecoil = getCurrentTickRecoil(strength, currentRecoil.duration);
        float baseRecoil = prevTickHorizontalRecoil - currentTickRecoil;
        prevTickHorizontalRecoil = currentTickRecoil;
        return baseRecoil;
    }

    private float getCurrentTickRecoil(float strength, float duration) {
        return (-strength * ((float) -Math.pow(2, -10 * ((float) currentRecoilTick / duration)) + 1) + strength);
    }

    @Override
    public void applyRecoil(GunItem.RecoilSettings recoilSettings) {
        this.currentRecoil = recoilSettings;
    }

    private void clearRecoil() {
        currentRecoilTick = 0;
        currentRecoil = null;
        prevTickVerticalRecoil = 0;
        prevTickHorizontalRecoil = 0;
        currentVerticalSpread = 0;
        currentHorizontalSpread = 0;
    }


    public boolean isVWSGun(ItemStack stack) {
        return stack.isIn(TagRegistry.VWS_GUNS);
    }

    public boolean isHoldingVWSGun() {
        return isVWSGun(this.getMainHandStack());
    }

    @Override
    public boolean isAimingDownSights() {
        ItemStack stack = this.getMainHandStack();
        return (isVWSGun(stack) && GunItem.ADS_POSE.isStackPosed(stack));
    }

    @Override
    public void triggerReload() {
        ItemStack stack = this.getMainHandStack();
        if (isVWSGun(stack))
            if (!GunItem.RELOAD_POSE.isStackPosed(stack))
                startReload(stack);
            else
                flagEndReload(stack);
    }

    private void startReload(ItemStack stack) {
        GunItem.RELOAD_POSE.poseStack(stack);
    }

    private void flagEndReload(ItemStack stack) {
        GunItem.RELOAD_POSE.unposeStack(stack);
    }

    private ItemStack getMainHandStack() {
        return ((PlayerEntity) (Object) this).getMainHandStack();
    }
}
