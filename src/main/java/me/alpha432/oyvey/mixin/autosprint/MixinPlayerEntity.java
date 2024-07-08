package me.alpha432.oyvey.mixin.autosprint;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.alpha432.oyvey.features.modules.movement.autosprint.AutoSprint;

import static me.alpha432.oyvey.features.modules.movement.autosprint.util.Util.mc;

@Mixin(value = PlayerEntity.class, priority = 800)
public class MixinPlayerEntity {
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", shift = At.Shift.AFTER))
    public void attackAHook(CallbackInfo callbackInfo) {
        if (AutoSprint.getInstance().isEnabled() && AutoSprint.getInstance().sprint.getValue()) {
            final float multiplier = 0.6f + 0.4f * AutoSprint.getInstance().motion.getValue();
            mc.player.setVelocity(mc.player.getVelocity().x / 0.6 * multiplier, mc.player.getVelocity().y, mc.player.getVelocity().z / 0.6 * multiplier);
            mc.player.setSprinting(true);
        }
    }
}

