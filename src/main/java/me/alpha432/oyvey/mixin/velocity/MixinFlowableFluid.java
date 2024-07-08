package me.alpha432.oyvey.mixin.velocity;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.alpha432.oyvey.features.modules.player.velocity.Velocity;

import java.util.Iterator;

@Mixin(FlowableFluid.class)
public class MixinFlowableFluid {
    @Redirect(method = "getVelocity", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 0))
    private boolean getVelocityHook(Iterator<Direction> var9) {
        if (Velocity.getInstance().isEnabled() && Velocity.getInstance().water.getValue()) {
            return false;
        }
        return var9.hasNext();
    }
}