package io.github.carolynkrell.mixin;

import io.github.carolynkrell.accessor.PlayerEntityAccessor;
import io.github.carolynkrell.registry.TagRegistry;
import io.github.carolynkrell.util.PlayerFirearmPose;
import io.github.carolynkrell.item.firearm.util.RecoilSettings;
import io.github.carolynkrell.util.VWSUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.carolynkrell.util.VWSUtils.isVWSFirearm;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
    private RecoilSettings recoilSettings;
    private int recoilTick = 0;
    private float prevTickVerticalRecoil;
    private float prevTickHorizontalRecoil;
    private float currentVerticalStrength;
    private float currentHorizontalStrength;
    private static final TrackedData<Integer> FIREARM_POSE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method= "initDataTracker()V", at = @At("TAIL"))
    public void injectAnimationPose(CallbackInfo ci) {
        this.dataTracker.startTracking(FIREARM_POSE, PlayerFirearmPose.NONE.ordinal());
    }

    public PlayerFirearmPose getFirearmPose() {
        return PlayerFirearmPose.get(this.dataTracker.get(FIREARM_POSE));
    }

    public void setFirearmPose(PlayerFirearmPose pose) {
        this.dataTracker.set(FIREARM_POSE, pose.ordinal());
    }

    @Inject(method="tick()V", at = @At("TAIL"))
    public void updateFirearmPose(CallbackInfo ci) {
        if (this.getFirearmPose() != PlayerFirearmPose.NONE && !this.isHoldingVWSGun()) {
            this.setFirearmPose(PlayerFirearmPose.NONE);
        }
    }

    // region Recoil methods

    @Inject(method = "tickMovement()V", at = @At("HEAD"))
    public void doRecoil(CallbackInfo ci) {
        float recoilDuration;
        if (recoilSettings != null && (recoilDuration = recoilSettings.duration) > 0) {
            if (++recoilTick > recoilDuration) {
                clearRecoil();
            }
            else {
                if (recoilTick == 1) {
                    this.currentVerticalStrength = getVariance(this.recoilSettings.maxVerticalSpread) + this.recoilSettings.verticalStrength;
                    this.prevTickVerticalRecoil = this.currentVerticalStrength;
                    this.currentHorizontalStrength = getVariance(this.recoilSettings.maxHorizontalSpread) + this.recoilSettings.horizontalStrength;
                    this.prevTickHorizontalRecoil = this.currentHorizontalStrength;
                }
                float verticalRecoil = getTickRecoil(false);
                float horizontalRecoil = getTickRecoil(true);
                this.setPitch(this.getPitch() - verticalRecoil);
                this.setYaw(this.getYaw() + horizontalRecoil);
            }
        }
    }

    private float getVariance(float variance) {
        float random = this.random.nextFloat() - 0.5f;
        return variance * random;
    }

    private float getTickRecoil(boolean horizontal) {
        float strength = horizontal ? this.currentHorizontalStrength : this.currentVerticalStrength;
        float tickRecoil = getEasing(strength, recoilSettings.duration);
        float delta = (horizontal ? prevTickHorizontalRecoil : prevTickVerticalRecoil) - tickRecoil;
        if (horizontal) prevTickHorizontalRecoil = tickRecoil;
        else prevTickVerticalRecoil = tickRecoil;
        return delta;
    }

    // https://gizma.com/easing/#expo2
    private float getEasing(float strength, float duration) {
        return (-strength * ((float) -Math.pow(2, -10 * ((float) recoilTick / duration)) + 1) + strength);
    }

    @Override
    public void applyRecoil(RecoilSettings recoilSettings) {
        this.recoilSettings = recoilSettings;
    }

    private void clearRecoil() {
        recoilTick = 0;
        recoilSettings = null;
        prevTickVerticalRecoil = 0;
        prevTickHorizontalRecoil = 0;
    }

    // endregion

    public boolean isHoldingVWSGun() {
        return VWSUtils.isVWSFirearm(this.getMainHandStack());
    }

    @Override
    public boolean isAimingDownSights() {
        ItemStack stack = this.getMainHandStack();
        return (VWSUtils.isVWSFirearm(stack) && this.getFirearmPose() == PlayerFirearmPose.ADS);
    }

    @Override
    public void triggerReload() {
        ItemStack stack = this.getMainHandStack();
        if (VWSUtils.isVWSFirearm(stack))
            if (this.getFirearmPose() != PlayerFirearmPose.RELOAD)
                this.startReload(stack);
            else
                this.flagEndReload(stack);
    }

    private void startReload(ItemStack stack) {
        //((FirearmItem) stack.getItem()).startReload();
    }

    private void flagEndReload(ItemStack stack) {
        //FirearmItem.RELOAD_POSE.unposeStack(stack);
    }

    // region LivingEntity ignored methods

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return null;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    // endregion
}
