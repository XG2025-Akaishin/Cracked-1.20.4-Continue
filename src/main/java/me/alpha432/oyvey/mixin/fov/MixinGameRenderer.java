package me.alpha432.oyvey.mixin.fov;

import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.alpha432.oyvey.features.modules.render.fov.FOV;
import net.minecraft.client.render.GameRenderer;

import static me.alpha432.oyvey.features.modules.render.fov.util.Util.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    public abstract void render(float tickDelta, long startTime, boolean tick);

    @Shadow
    private float zoom;

    @Shadow
    private float zoomX;

    @Shadow
    private float zoomY;

    @Shadow
    private float viewDistance;

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("TAIL"), cancellable = true)
    public void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cb) {
        if (FOV.getInstance().isEnabled()) {
            if (cb.getReturnValue() == 70
                    && !FOV.getInstance().itemFov.getValue()
                    && mc.options.getPerspective() != Perspective.FIRST_PERSON) return;
            else if (FOV.getInstance().itemFov.getValue() && cb.getReturnValue() == 70) {
                cb.setReturnValue(FOV.getInstance().itemFovModifier.getValue().doubleValue());
                return;
            }

            if (mc.player.isSubmergedInWater()) return;
            cb.setReturnValue(FOV.getInstance().fovModifier.getValue().doubleValue());
        }
    }
}
