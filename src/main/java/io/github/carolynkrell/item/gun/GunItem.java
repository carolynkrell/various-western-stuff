package io.github.carolynkrell.item.gun;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.accessor.PlayerEntityGunAccessor;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class GunItem extends Item implements IAnimatable, ISyncable, FabricItem {
    public static final FirearmPose COCKED_POSE = new FirearmPose("IsCocked");
    public static final FirearmPose ADS_POSE = new FirearmPose("IsADS");
    public static final FirearmPose RELOAD_POSE = new FirearmPose("IsReload");
    private static final int ANIM_COCK = 0;
    private static final int ANIM_FIRE = 1;
    private final GunSettings gunSettings;

    private final AnimationFactory factory = new AnimationFactory(this);

    public GunItem(Settings settings, GunSettings gunSettings) {
        super(settings);
        this.gunSettings = gunSettings;
        GeckoLibNetwork.registerSyncable(this);
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected) {
            if (RELOAD_POSE.isStackPosed(stack)) {

            }
        }
        else {
            if (ADS_POSE.isStackPosed(stack)) ADS_POSE.unposeStack(stack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        VariousWesternStuff.log(Level.INFO, "stopped using");
        if (user instanceof PlayerEntity && ((PlayerEntityGunAccessor) user).isAimingDownSights()) {
            ADS_POSE.unposeStack(stack);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hand == Hand.MAIN_HAND && !ADS_POSE.isStackPosed(stack)) {
            ADS_POSE.poseStack(stack);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 5;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, "controller");
        if (state == ANIM_COCK) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("cock", false).addAnimation("cocked", true));
        }
        else if (state == ANIM_FIRE) {
            controller.markNeedsReload();
            controller.setAnimation(new AnimationBuilder().addAnimation("fire", false));
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }

    private void syncAnimation(PlayerEntity user, ItemStack stack, World world, int animation) {
        final int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerWorld) world);
        GeckoLibNetwork.syncAnimation(user, this, id, animation);
        for (PlayerEntity otherPlayer : PlayerLookup.tracking(user)) {
            GeckoLibNetwork.syncAnimation(otherPlayer, this, id, animation);
        }
    }

    public void attackGun(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            if (COCKED_POSE.isStackPosed(stack)) applyRecoil(user);
        } else {
            int animation = ANIM_COCK;
            if (COCKED_POSE.isStackPosed(stack)) animation = ANIM_FIRE;
            COCKED_POSE.setPose(stack, !COCKED_POSE.isStackPosed(stack));
            syncAnimation(user, stack, world, animation);
        }
    }

    public void applyRecoil(PlayerEntity user) {
        if (user instanceof PlayerEntityGunAccessor) {
            ((PlayerEntityGunAccessor) user).applyRecoil(gunSettings.recoilSettings);
        }
    }

    public void setReloading(ItemStack stack) {
        RELOAD_POSE.poseStack(stack);
        ADS_POSE.unposeStack(stack);
        COCKED_POSE.unposeStack(stack);
    }

    public static class GunSettings {
        public ReloadStyle reloadStyle;
        public int reloadTicks;
        public CockStyle cockStyle;
        public RecoilSettings recoilSettings;


        public GunSettings(ReloadStyle reloadStyle, int reloadTicks, CockStyle cockStyle, RecoilSettings recoilSettings) {
            this.reloadStyle = reloadStyle;
            this.reloadTicks = reloadTicks;
            this.cockStyle = cockStyle;
            this.recoilSettings = recoilSettings;
        }
    }

    public static class RecoilSettings {
        public float verticalStrength;
        public float horizontalStrength;
        public int duration;
        public float maxVerticalSpread;
        public float maxHorizontalSpread;

        public RecoilSettings(float verticalStrength, float horizontalStrength, int duration, float maxVerticalSpread, float maxHorizontalSpread) {
            this.verticalStrength = verticalStrength;
            this.horizontalStrength = horizontalStrength;
            this.duration = duration;
            this.maxVerticalSpread = maxVerticalSpread;
            this.maxHorizontalSpread = maxHorizontalSpread;
        }
    }

    public record FirearmPose(String key) {
        public void poseStack(ItemStack stack) {
            setPose(stack, true);
        }

        public void unposeStack(ItemStack stack) {
            setPose(stack, false);
        }

        public boolean isStackPosed(ItemStack stack) {
            NbtCompound nbt = stack.getNbt();
            return nbt != null && nbt.getBoolean(key);
        }

        public void setPose(ItemStack stack, boolean posed) {
            NbtCompound nbt = stack.getOrCreateNbt();
            nbt.putBoolean(key, posed);
        }
    }

    public enum ReloadStyle {
        SINGLE,
        CLIP
    }

    public enum CockStyle {
        EVERY_SHOT,
        AFTER_RELOAD
    }
}
