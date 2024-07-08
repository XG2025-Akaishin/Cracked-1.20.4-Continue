package me.alpha432.oyvey.features.modules.chat.chatmodifier;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.chat.chatmodifier.utils.Timer;
import me.alpha432.oyvey.features.modules.chat.moduletools.utils.TextUtil;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.mixin.chatmodifier.IGameMessageS2CPacket;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.eventbus.Subscribe;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class ChatModifier extends Module {
    public static String SUFFIX = "";
    public static String COLORTEXT = "";
    public static String GLOBAL = "";
    public static String COLOR = "";
    private String string;

    private static ChatModifier INSTANCE = new ChatModifier();
    public Setting<Boolean> globalchat = this.register(new Setting<Boolean>("GlobalChat", true, "Global Chat Prefix"));
    public Setting<GlobalChat> globalmode = register(new Setting<GlobalChat>("GlobalChat", GlobalChat.NONE, v -> this.globalchat.getValue()));
    public Setting<TextUtil.Color> abyssColorGlobal = this.register(new Setting<TextUtil.Color>("AbyssTextColor", TextUtil.Color.AQUA, color -> this.globalmode.getValue() == GlobalChat.ABYSS));
    public Setting<Suffix> suffix = this.register(new Setting<Suffix>("Suffix", Suffix.NONE, "Your Suffix."));
    public Setting<String> custom = this.register(new Setting("Custom", "cracked v2.0 plus ultra pro", v -> this.suffix.getValue() == Suffix.CUSTOM));
    public Setting<TextColor> textcolor = this.register(new Setting<TextColor>("TextColor", TextColor.NONE, "Your text color."));
    public Setting<StiTextColor> stiTextColor = this.register(new Setting<StiTextColor>("StiTextColor", StiTextColor.NONE, "Your sti text color."));
	public Setting<Boolean> infiniteChatBox = this.register(new Setting<Boolean>("infiniteChatBox", false,"Makes your chat infinite."));
	public Setting<Boolean> longerChatHistory = this.register(new Setting<Boolean>("longerChatHistory", false));
	public Setting<Integer> longerChatLines = this.register(new Setting<Integer>("longerChatLines", 1000, 0, 1000));
    public Setting<Boolean> keepHistory = this.register(new Setting<Boolean>("keepHistory", false,"What"));
    public Setting<Boolean> commandoption = this.register(new Setting<Boolean>("Command", false));
    private Setting<String> customcommand = this.register(new Setting("CustomCommand", "/help"));
    public Setting<Boolean> spammer = this.register(new Setting<Boolean>("Spammer", false));
    private Setting<String> spamerstring = this.register(new Setting("CustomSpammer", "CustomSpam", v -> this.spammer.getValue()));
    private Setting<Integer> random = this.register(new Setting("Random", 1, 1, 20, v -> this.spammer.getValue()));
    private Setting<Integer> delay = this.register(new Setting("Delay", 100, 0, 10000, v -> this.spammer.getValue()));
    private final Timer timer = new Timer();
    public final IntList lines = new IntArrayList();

    public ChatModifier() {
    super("ChatModifier", "Modifies your chat", Module.Category.CHAT, true, false, false);
    this.setInstance();
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    //@EventHandler
    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof ChatMessageC2SPacket packet) {
            if (packet.chatMessage().startsWith("/") || packet.chatMessage().startsWith("+")) {
                return;
            }
            if (Objects.equals(packet.chatMessage(), string)) {
                return;
            }

            switch (this.suffix.getValue()) {
                case CRACKED: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("cracked v2.0");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case FUTURE: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("future v2.19.5");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case ABYSS: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("abyss v4.0");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case PYRO: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("pyro v5.0");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case RUSHERHACK: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("rusherhack v4.6");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case PHOBOS: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("phobos v7.0");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case EARTHACK: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("earthack v1.7 - 1.20.4");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case PUTAHACK: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("putahack v2.8");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case BOZE: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("boze v10.1");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case LEUXBACKDOOR: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("leuxbackdoor v0.9.9 - 1.20.4");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case OYVEY: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("oyvey v8.0.2");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case SNOW: {
                    SUFFIX = " \u00bb " + this.convertToUnicode("snow v5.1.1");
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case CUSTOM: {
                    SUFFIX = " \u00bb " + this.convertToUnicode(this.custom.getValue());
                    break;
                }
            }
            switch (this.suffix.getValue()) {
                case NONE: {
                    SUFFIX = "";
                    break;
                }
            }

            switch (this.stiTextColor.getValue()) {
                case RANDOM: {
                    int minNum = 1;
                    int maxNum = 16;
                    int randomNumber = (int)Math.floor(Math.random()*(maxNum-minNum+1)+minNum);
                    if (randomNumber == 1) {
                        COLOR = "!2 ";
                    }
                    if (randomNumber == 2) {
                        COLOR = "!3 ";
                    }
                    if (randomNumber == 3) {
                        COLOR = "!4 " ;
                    }
                    if (randomNumber == 4) {
                        COLOR = "!5 " ;
                    }
                    if (randomNumber == 5) {
                        COLOR = "!7 ";
                    }
                    if (randomNumber == 6) {
                        COLOR = "!8 " ;
                    }
                    if (randomNumber == 7) {
                        COLOR = "!9 " ;
                    }
                    if (randomNumber == 8) {
                        COLOR = "!g " ;
                    }
                    if (randomNumber == 9) {
                        COLOR = "!c " ;
                    }
                    if (randomNumber == 10) {
                        COLOR = "!d ";
                    }
                    if (randomNumber == 11) {
                        COLOR = "!a ";
                    }
                    if (randomNumber == 12) {
                        COLOR = "!e ";
                        }
                    if (randomNumber == 13) {
                        COLOR = "!b " ;
                    }
                    if (randomNumber == 14) {
                        COLOR = "!f ";
                    }
                    if (randomNumber == 15) {
                        COLOR = "!1 " ;
                    }
                    if (randomNumber == 16) {
                        COLOR = "!6 " ;
                    }
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case LIGHT_GREEN: {
                    COLOR = "!2 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case LIGHT_BLUE: {
                    COLOR = "!3 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case LIGHT_RED: {
                    COLOR = "!4 ";
                }
            }
                switch (this.stiTextColor.getValue()) {
                case LIGHT_AQUA: {
                    COLOR = "!5 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case YELLOW: {
                    COLOR = "!7 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case LIGHT_GRAY: {
                    COLOR = "!8 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case LIGHT_PURPLE: {
                    COLOR = "!g ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case GRAY: {
                    COLOR = "!c " ;
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case BLACK: {
                    COLOR = "!9 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case BLUE: {
                    COLOR = "!d ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case GREEN: {
                    COLOR = "!a ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case AQUA: {
                    COLOR = "!e ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case RED: {
                    COLOR = "!b ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case PURPLE: {
                    COLOR = "!f ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case GOLD: {
                    COLOR = "!6 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case WHITE: {
                    COLOR = "!1 ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case ITALIC: {
                    COLOR = "!h ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case STRIKE: {
                    COLOR = "!k ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case UNDERLINE: {
                    COLOR = "!i ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case BOLD: {
                    COLOR = "!j ";
                    break;
                }
            }
                switch (this.stiTextColor.getValue()) {
                case NONE: {
                    COLOR = "";
                    break;
                }
            }


            switch (this.textcolor.getValue()) {
                case GREEN: {
                    COLORTEXT = "> ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case YELLOW: {
                    COLORTEXT = "# ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case GOLD: {
                    COLORTEXT = "$ ";
                }
            }
                switch (this.textcolor.getValue()) {
                case BLUE: {
                    COLORTEXT = "! ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case AQUA: {
                    COLORTEXT = "`` ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case PURPLE: {
                    COLORTEXT = "? ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case RED: {
                    COLORTEXT = "& ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case DARKRED: {
                    COLORTEXT = "@ ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case GRAY: {
                    COLORTEXT = ". ";
                    break;
                }
            }
                switch (this.textcolor.getValue()) {
                case NONE: {
                    COLORTEXT = "";
                    break;
                }
            }
            string = COLORTEXT + COLOR + packet.chatMessage() + SUFFIX;
            mc.player.networkHandler.sendChatMessage(COLORTEXT + COLOR + packet.chatMessage() + SUFFIX);
            event.setCancelled(true);
        }
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof GameMessageS2CPacket pac && globalchat.getValue()) {
            IGameMessageS2CPacket pac2 = event.getPacket();//event.getPacket();
            switch (this.globalmode.getValue()) {
				case CRACKED: {
                    GLOBAL = (OyVey.commandManager.getClientMessage()) + " " + Formatting.RESET;
                    break;
                }
            }
            switch (this.globalmode.getValue()) {
                case FUTURE: {
                    GLOBAL = Formatting.RED + "[Future]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case PHOBOS: {
                    GLOBAL = Formatting.BOLD + "PHOBOS" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case DOTGOD: {
                    GLOBAL = Formatting.DARK_PURPLE + "[" + Formatting.LIGHT_PURPLE + "DotGod.CC" + Formatting.DARK_PURPLE + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
				case SNOW: {
                    GLOBAL = (Object)Formatting.BLUE + "[" + (Object)Formatting.AQUA + "Snow" + (Object)Formatting.BLUE + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case WEATHER: {
                    GLOBAL = (Object)Formatting.AQUA + "[" + (Object)Formatting.AQUA + "Weather" + (Object)Formatting.AQUA + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case CATALYST: {
                    GLOBAL = (Object)Formatting.DARK_GRAY + "[" + (Object)Formatting.AQUA + "Catalyst" + (Object)Formatting.DARK_GRAY + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case RUSHERHACK: {
                    GLOBAL = (Object)Formatting.WHITE + "[" + (Object)Formatting.GREEN + "rusherhack" + (Object)Formatting.WHITE + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case KONAS: {
                    GLOBAL = (Object)Formatting.DARK_GRAY + "[" + (Object)Formatting.LIGHT_PURPLE + "Konas" + (Object)Formatting.DARK_GRAY + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case LEGACY: {
                    GLOBAL = (Object)Formatting.WHITE + "[" + (Object)Formatting.LIGHT_PURPLE + "Legacy" + (Object)Formatting.WHITE + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case EUROPA: {
                    GLOBAL = (Object)Formatting.GRAY + "[" + (Object)Formatting.RED + "Europa" + (Object)Formatting.GRAY + "]"  + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case PYRO: {
                    GLOBAL = (Object)Formatting.DARK_RED + "[" + (Object)Formatting.DARK_RED + "Pyro" + (Object)Formatting.DARK_RED + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
                case MUFFIN: {
                    GLOBAL = (Object)Formatting.LIGHT_PURPLE + "[" + (Object)Formatting.DARK_PURPLE + "Muffin" + (Object)Formatting.LIGHT_PURPLE + "]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
				case ABYSS: {
                    GLOBAL = TextUtil.coloredString("[Abyss]", this.abyssColorGlobal.getPlannedValue()) + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
				case LUIGIHACK: {
                    GLOBAL = (Object)Formatting.GREEN + "[LuigiHack]" + " " + Formatting.RESET;
                    break;
                }
            }
                switch (this.globalmode.getValue()) {
				case NONE: {
                    GLOBAL = "";
                    break;
                }
            }
        pac2.setContent(Text.of(GLOBAL).copy().append(pac.content));
        }
    }


    @Override
    public void onUpdate() {
        //Other Code future
        if (this.commandoption.getValue()) {
            this.sendPlayerMsg((String)this.customcommand.getValue());
            //this.disable();
            this.commandoption.setValue(isDisabled());
        }
    }


    @Override
   public void onTick() {
        if (this.spammer.getValue()) {
         if (this.timer.passedMs((long)(Integer)this.delay.getValue())) {
            mc.player.networkHandler.sendChatMessage((String)this.spamerstring.getValue() + "[" + RandomStringUtils.randomAlphanumeric((Integer)this.random.getValue()) + "]");
            this.timer.reset();
          }
        }
    }

    // Longer chat

    public boolean isInfiniteChatBox() {
        return isEnabled() && infiniteChatBox.getValue();//isEnale or IsOn
    }

    public boolean isLongerChat() {
        return isEnabled() && longerChatHistory.getValue();//isEnale or IsOn
    }

    public boolean keepHistory() { return isEnabled() && keepHistory.getValue(); }//isEnale or IsOn

    public int getExtraChatLines() {
        return longerChatLines.getValue();
    }

    public void sendPlayerMsg(String message) {
        mc.inGameHud.getChatHud().addToMessageHistory(message);
  
        if (message.startsWith("/")) mc.player.networkHandler.sendChatCommand(message.substring(1));
        else mc.player.networkHandler.sendChatMessage(message);
    }

    private String convertToUnicode(String base) {
        String new_base = base;

        new_base = new_base.replace("a", "\u1d00");
        new_base = new_base.replace("b", "\u0299");
        new_base = new_base.replace("c", "\u1d04");
        new_base = new_base.replace("d", "\u1d05");
        new_base = new_base.replace("e", "\u1d07");
        new_base = new_base.replace("f", "\u0493");
        new_base = new_base.replace("g", "\u0262");
        new_base = new_base.replace("h", "\u029c");
        new_base = new_base.replace("i", "\u026a");
        new_base = new_base.replace("j", "\u1d0a");
        new_base = new_base.replace("k", "\u1d0b");
        new_base = new_base.replace("l", "\u029f");
        new_base = new_base.replace("m", "\u1d0d");
        new_base = new_base.replace("n", "\u0274");
        new_base = new_base.replace("o", "\u1d0f");
        new_base = new_base.replace("p", "\u1d18");
        new_base = new_base.replace("q", "\u01eb");
        new_base = new_base.replace("r", "\u0280");
        new_base = new_base.replace("s", "\u0455");
        new_base = new_base.replace("t", "\u1d1b");
        new_base = new_base.replace("u", "\u1d1c");
        new_base = new_base.replace("v", "\u1d20");
        new_base = new_base.replace("w", "\u1d21");
        new_base = new_base.replace("x", "\u0445");
        new_base = new_base.replace("y", "\u028f");
        new_base = new_base.replace("z", "\u1d22");

        return new_base;
    }

    public enum StiTextColor {
        NONE,
        RANDOM,
        GREEN,
        BLUE,
        AQUA,
        YELLOW,
        GOLD,
        RED,
        PURPLE,
        LIGHT_BLUE,
        LIGHT_GREEN,
        LIGHT_AQUA,
        LIGHT_RED,
        LIGHT_PURPLE,
        LIGHT_GRAY,
        GRAY,
        BLACK,
        WHITE,
        BOLD,
        STRIKE,
        UNDERLINE,
        ITALIC;
    }

	public static enum GlobalChat {
        NONE,
        CRACKED,
        FUTURE,
        ABYSS,
        PYRO,
        KONAS,
        PHOBOS,
		DOTGOD,
        MUFFIN,
        WEATHER,
        SNOW,
        CATALYST,
        RUSHERHACK,
        LEGACY,
        EUROPA,
        LUIGIHACK;  
    }

    public enum TextColor {
        NONE,
        GREEN,
        YELLOW,
        GOLD,
        BLUE,
        AQUA,
        PURPLE,
        RED,
        DARKRED,
        GRAY;
    }

    public enum Suffix {
        NONE, 
        CRACKED,
        FUTURE,
        ABYSS,
        PYRO,
        RUSHERHACK,
        PHOBOS,
        EARTHACK,
        PUTAHACK,
        BOZE,
        LEUXBACKDOOR,
        OYVEY,
        SNOW,
        CUSTOM;
    }
}
