package me.alpha432.oyvey.mixin.splashprogress;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

import static me.alpha432.oyvey.util.traits.Util.mc;

@Mixin(SplashOverlay.class)
public abstract class MixinSplashOverlay {
    @Final @Shadow private boolean reloading;
    @Shadow private float progress;
    @Shadow private long reloadCompleteTime = -1L;
    @Shadow private long reloadStartTime = -1L;
    @Final @Shadow private ResourceReload reload;
    @Final @Shadow private Consumer<Optional<Throwable>> exceptionHandler;
    private static final Identifier CRACKED_LOGO = new Identifier("textures/logo.png");
    private static final Identifier CRACKED_LOGO_BACKGROUND = new Identifier("textures/cracked-screen.png");

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ci.cancel();
        renderCustom(context, mouseX, mouseY, delta);
    }

    public void renderCustom(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = mc.getWindow().getScaledWidth();
        int j = mc.getWindow().getScaledHeight();
        long l = Util.getMeasuringTimeMs();
        if (reloading && reloadStartTime == -1L) {
            reloadStartTime = l;
        }

        float f = reloadCompleteTime > -1L ? (float) (l - reloadCompleteTime) / 1000.0F : -1.0F;
        float g = reloadStartTime > -1L ? (float) (l - reloadStartTime) / 500.0F : -1.0F;
        float h;
        int k;
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Draw the background image
        //context.drawTexture(CRACKED_LOGO_BACKGROUND, 0, 0, 0, 0, i, j);

        if (f >= 1.0F) {
            if (mc.currentScreen != null)
                mc.currentScreen.render(context, 0, 0, delta);

           k = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            context.drawTexture(CRACKED_LOGO_BACKGROUND, 0, 0, 0, 0, i, j,i,j);
            h = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);

        } else if (reloading) {
            if (mc.currentScreen != null && g < 1.0F)
                mc.currentScreen.render(context, mouseX, mouseY, delta);

                k = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
                context.drawTexture(CRACKED_LOGO_BACKGROUND, 0, 0, 0, 0, i, j,i,j);
                h = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else {
            context.drawTexture(CRACKED_LOGO_BACKGROUND, 0, 0, 0, 0, i, j,i,j);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        k = (int) ((double) context.getScaledWindowWidth() * 0.5);
        int p = (int) ((double) context.getScaledWindowHeight() * 0.5);

        context.drawTexture(CRACKED_LOGO, k - 150, p - 35, 0, 0, 300, 70, 300, 70);
        context.drawTexture(CRACKED_LOGO, k - 150, p - 35, 0, 0, 300, 70, 300, 70);

        if (f >= 2.0F) {
            mc.setOverlay(null);
        }

        if (reloadCompleteTime == -1L && reload.isComplete() && (!reloading || g >= 2.0F)) {
            try {
                reload.throwException();
                exceptionHandler.accept(Optional.empty());
            } catch (Throwable var23) {
                exceptionHandler.accept(Optional.of(var23));
            }

            reloadCompleteTime = Util.getMeasuringTimeMs();
            if (mc.currentScreen != null) {
                mc.currentScreen.init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
            }
        }
    }
}