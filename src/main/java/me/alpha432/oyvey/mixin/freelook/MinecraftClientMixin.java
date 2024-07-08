package me.alpha432.oyvey.mixin.freelook;

import me.alpha432.oyvey.OyVey;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.alpha432.oyvey.event.impl.freelook.TickEvent;
import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin(value = MinecraftClient.class, priority = 1001)
public abstract class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo info) {
        //OnlinePlayers.update();
        EVENT_BUS.post(TickEvent.Pre.get());
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo info) {
        EVENT_BUS.post(TickEvent.Post.get());
    }
}
