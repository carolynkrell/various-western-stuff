package io.github.carolynkrell.mixin;

import io.github.carolynkrell.accessor.PlayerEntityGunAccessor;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Redirect(method = "tickMovement()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/Input;movementSideways:F", opcode = Opcodes.PUTFIELD))
    private void adsSidewaysMovement(Input input, float value) {
        ClientPlayerEntity player = ((ClientPlayerEntity) (Object) this);
        if (!((PlayerEntityGunAccessor) player).isAimingDownSights()) {
            input.movementSideways *= 0.2f;
        }
    }

    @Redirect(method = "tickMovement()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/Input;movementForward:F", opcode = Opcodes.PUTFIELD))
    private void adsForewardsMovement(Input input, float value) {
        ClientPlayerEntity player = ((ClientPlayerEntity) (Object) this);
        if (!((PlayerEntityGunAccessor) player).isAimingDownSights()) {
            input.movementForward *= 0.2f;
        }
    }

    @Redirect(method = "tickMovement()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;ticksLeftToDoubleTapSprint:I", opcode = Opcodes.PUTFIELD))
    private void adsSprint(ClientPlayerEntity clientPlayerEntity, int value) {
        if (!((PlayerEntityGunAccessor) clientPlayerEntity).isAimingDownSights()) {
            ((ClientPlayerEntityAccessor) clientPlayerEntity).setTicksLeftToDoubleTapSprint(0);
        }
    }
}