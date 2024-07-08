package me.alpha432.oyvey.features.modules.misc.fakeplayer.utils;

import static me.alpha432.oyvey.features.modules.misc.fakeplayer.utils.Util.mc;

import me.alpha432.oyvey.OyVey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtils {

    public static void sendMessage(String message) {
        mc.player.sendMessage(Text.of(OyVey.commandManager.getClientMessage() + " " + Formatting.GREEN + message));
    }

    public static void warningMessage(String message) {
        mc.player.sendMessage(Text.of(OyVey.commandManager.getClientMessage() + " " + Formatting.RED + message));
    }

}
