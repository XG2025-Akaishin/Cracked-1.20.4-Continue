package me.alpha432.oyvey.mixin.middleclick;

import me.alpha432.oyvey.features.modules.player.middleclick.MiddleClick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow
    @Final
    private Window window;

    @Shadow
    private static MinecraftClient instance;

    @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
    private void doItemPickHook(CallbackInfo ci) {
        if (MiddleClick.getInstance().isEnabled() && MiddleClick.getInstance().antiPickUp.getValue())
            ci.cancel();
    }
}
