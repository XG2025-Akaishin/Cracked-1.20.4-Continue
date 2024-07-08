package me.alpha432.oyvey.mixin.cape;

import me.alpha432.oyvey.features.modules.client.Cape;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    private static final Identifier Future = new Identifier("minecraft", "textures/capes/future.png");
    private static final Identifier Rusherhack = new Identifier("minecraft", "textures/capes/rusherhack.png");
    private static final Identifier Hyper = new Identifier("minecraft", "textures/capes/hyper.png");

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        if (!Cape.getInstance().isOn()) return;

        SkinTextures oldTextures = cir.getReturnValue();
        Identifier capeTexture;

        if (Cape.getInstance().cape.getValue() == Cape.CapeMode.FUTURE) {
            capeTexture = Future;
            SkinTextures Textures = new SkinTextures(oldTextures.texture(), oldTextures.textureUrl(), capeTexture, capeTexture, oldTextures.model(), oldTextures.secure());
            cir.setReturnValue(Textures);
        }
        if (Cape.getInstance().cape.getValue() == Cape.CapeMode.RUSHERHACK) {
            capeTexture = Rusherhack;
            SkinTextures Textures = new SkinTextures(oldTextures.texture(), oldTextures.textureUrl(), capeTexture, capeTexture, oldTextures.model(), oldTextures.secure());
            cir.setReturnValue(Textures);
        }
        if (Cape.getInstance().cape.getValue() == Cape.CapeMode.HYPER) {
            capeTexture = Hyper;
            SkinTextures Textures = new SkinTextures(oldTextures.texture(), oldTextures.textureUrl(), capeTexture, capeTexture, oldTextures.model(), oldTextures.secure());
            cir.setReturnValue(Textures);
        }

    }
}