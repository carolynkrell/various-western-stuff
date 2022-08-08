package io.github.carolynkrell.item.firearm;

import io.github.carolynkrell.VariousWesternStuff;
import io.github.carolynkrell.accessor.PlayerEntityAccessor;
import io.github.carolynkrell.item.firearm.util.FirearmSettings;
import io.github.carolynkrell.item.firearm.util.FirearmType;
import io.github.carolynkrell.util.PlayerFirearmPose;
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

public abstract class FirearmItem extends Item implements IAnimatable, ISyncable, FabricItem {
    public static final String COCKED_KEY = "IsCocked";
    private static final int ANIM_COCK = 0;
    private static final int ANIM_FIRE = 1;
    private static final int ANIM_RELOAD = 2;
    private final FirearmSettings firearmSettings;

    private final AnimationFactory factory = new AnimationFactory(this);

    public FirearmItem(Settings settings, FirearmSettings firearmSettings) {
        super(settings);
        this.firearmSettings = firearmSettings;
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
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        VariousWesternStuff.log(Level.INFO, "stopped using");
        if (user instanceof PlayerEntity && ((PlayerEntityAccessor) user).isAimingDownSights()) {
            ((PlayerEntityAccessor) user).setFirearmPose(PlayerFirearmPose.NONE);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hand == Hand.MAIN_HAND && ((PlayerEntityAccessor) user).getFirearmPose() == PlayerFirearmPose.NONE) {
            ((PlayerEntityAccessor) user).setFirearmPose(PlayerFirearmPose.ADS);
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
        controller.markNeedsReload();
        if (state == ANIM_COCK) {
            controller.setAnimation(new AnimationBuilder().addAnimation("cock", false).addAnimation("cocked", true));
        }
        else if (state == ANIM_FIRE) {
            controller.setAnimation(new AnimationBuilder().addAnimation("fire", false));
        }
        else if (state == ANIM_RELOAD) {
            //controller.setAnimation(new AnimationBuilder().addAnimation("reload"));
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

    public void onAttackWithFirearm(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean cocked = isCocked(stack);
        if (world.isClient) {
            if (cocked) applyRecoil(user);
        } else {
            int animation = cocked ? ANIM_FIRE : ANIM_COCK;
            setCocked(stack, !cocked);
            syncAnimation(user, stack, world, animation);
        }
    }

    public void applyRecoil(PlayerEntity user) {
        if (user instanceof PlayerEntityAccessor) {
            ((PlayerEntityAccessor) user).applyRecoil(firearmSettings.recoilSettings);
        }
    }

    public boolean isCocked(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean(COCKED_KEY);
    }

    public void setCocked(ItemStack stack, boolean cocked) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean(COCKED_KEY, cocked);
    }

    public void setPoseReloading(PlayerEntity user) {
        ((PlayerEntityAccessor) user).setFirearmPose(PlayerFirearmPose.RELOAD);
    }

    public FirearmType getFirearmType() {
        return this.firearmSettings.firearmType;
    }
}
