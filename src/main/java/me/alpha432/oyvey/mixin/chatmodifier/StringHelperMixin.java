

package me.alpha432.oyvey.mixin.chatmodifier;

import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.chat.chatmodifier.ChatModifier;

@Mixin(StringHelper.class)
public class StringHelperMixin {
    @ModifyArg(method = "truncateChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringHelper;truncate(Ljava/lang/String;IZ)Ljava/lang/String;"), index = 1)
    private static int injected(int maxLength) { // this method is only used in one place, to truncate chat messages, so it's fine to do this
        return (ChatModifier.getInstance().isInfiniteChatBox() ? Integer.MAX_VALUE : maxLength);
    }
}
