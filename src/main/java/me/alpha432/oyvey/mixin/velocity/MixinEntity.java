package me.alpha432.oyvey.mixin.velocity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import me.alpha432.oyvey.features.modules.player.velocity.Velocity;
import me.alpha432.oyvey.mixin.portalchat.IEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;

import java.util.ArrayList;
import java.util.List;


@Mixin(Entity.class)
public abstract class MixinEntity implements IEntity {

    @Shadow
    protected abstract BlockPos getVelocityAffectingPos();

    @Shadow
    private Box boundingBox;

    //@Override
    public BlockPos cracked_ve$getVelocityBP() {
        return getVelocityAffectingPos();
    }

    @ModifyArgs(method = "pushAwayFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void pushAwayFromHook(Args args) {

        //Condition '...' is always 'false' is a lie!!! do not delete
        if ((Object) this == MinecraftClient.getInstance().player && Velocity.getInstance().isEnabled() && Velocity.getInstance().players.getValue()) {
            args.set(0, 0.);
            args.set(1, 0.);
            args.set(2, 0.);
        }
    }
}