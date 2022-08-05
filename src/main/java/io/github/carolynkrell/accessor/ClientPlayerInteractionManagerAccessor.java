package io.github.carolynkrell.accessor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ClientPlayerInteractionManagerAccessor {
    void interactGun(PlayerEntity player, Hand hand);
}
