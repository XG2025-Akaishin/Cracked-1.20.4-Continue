package me.alpha432.oyvey.features.modules.misc.autoauth;


import org.jetbrains.annotations.NotNull;

import com.google.common.eventbus.Subscribe;
import java.awt.*;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.manager.CommandManager;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.RandomStringUtils;

public final class AutoAuth extends Module {
    private final Setting<Mode> passwordMode = this.register(new Setting<>("Password Mode", Mode.Custom));
    private final Setting<String> cpass = this.register( new Setting<>("Password", "password777", v -> passwordMode.getValue() == Mode.Custom));
    private final Setting<Boolean> showPasswordInChat = this.register(new Setting<>("Show Pass In Chat", true));
    private final Setting<Boolean> notifiDesk = this.register(new Setting<>("DesktopNotifs", true));

    private static AutoAuth instance;
    private String password;

    public AutoAuth() {
        super("AutoAuth", "", Category.MISC, true, false, false);
        instance = this;
    }

    private enum Mode {
        Custom, Random, Qwerty
    }

    @Override
    public void onEnable() {
        String warningMsg = Formatting.RED + "Attention!!! " + Formatting.GREEN + "The passwords are stored in the config, so before sharing your configs " + Formatting.RED + " TOGGLE OFF THE MODULE!";
        sendMessage(warningMsg);
    }

    @Override
    public void onDisable() {
        sendMessage("Resetting password...");
        cpass.setValue("none");
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.@NotNull Receive event) {
        if (event.getPacket() instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket pac = (GameMessageS2CPacket) event.getPacket();
            if (passwordMode.getValue() == Mode.Custom) {
                this.password = cpass.getValue();
            } else if (passwordMode.getValue() == Mode.Qwerty) {
                this.password = "qwerty";
            } else if (passwordMode.getValue() == Mode.Random) {
                String str1 = RandomStringUtils.randomAlphabetic(5);
                String str2 = RandomStringUtils.randomPrint(5);
                this.password = str1 + str2;
            }
            if (passwordMode.getValue() == Mode.Custom && (this.password == null || this.password.isEmpty()))
                return;
            if (pac.content().getString().contains("/reg") || pac.content().getString().contains("/register") || pac.content().getString().contains("register")) {
                mc.getNetworkHandler().sendChatCommand("reg " + this.password + " " + this.password);
                this.disable();
                if (this.showPasswordInChat.getValue())
                    sendMessage("Tu contraseña: " + Formatting.RED + this.password);
            } else if (pac.content().getString().contains("login") || pac.content().getString().contains("/l")) {
                mc.getNetworkHandler().sendChatCommand("login " + this.password);
                this.disable();
            }
        }
    }    

    public static AutoAuth getInstance() {
        return instance;
    }
}
