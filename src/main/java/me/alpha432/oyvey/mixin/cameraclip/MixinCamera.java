package me.alpha432.oyvey.mixin.cameraclip;

import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.cameraclip.NoCameraClip;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    protected abstract double clipToSpace(double desiredCameraDistance);

    @Shadow
    private boolean thirdPerson;

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(DDD)V", ordinal = 0))
    private void modifyCameraDistance(Args args) {
        if (NoCameraClip.getInstance().isEnabled()) {
            args.set(0, -clipToSpace(NoCameraClip.getInstance().getDistance()));
        }
    }

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
        if (NoCameraClip.getInstance().isEnabled()) {
            info.setReturnValue(NoCameraClip.getInstance().getDistance());
        }
    }
}