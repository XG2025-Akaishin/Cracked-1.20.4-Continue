package me.alpha432.oyvey.features.modules.chat.notifications;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.ClientEvent;
import me.alpha432.oyvey.features.modules.chat.moduletools.ModuleTools;
import me.alpha432.oyvey.features.modules.chat.moduletools.utils.TextUtil;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;

public class Notifications
        extends Module {
    private static final List<String> modules = new ArrayList<>();
    private static Notifications INSTANCE = new Notifications();
    public Setting<Boolean> totemPops = this.register(new Setting<>("TotemPops", true));
    public Setting<Boolean> totemNoti = this.register(new Setting<Boolean>("TotemNoti", Boolean.FALSE, v -> this.totemPops.getValue()));
    public Setting<Boolean> moduleMessage = this.register(new Setting<>("ModuleMessage", true));
    public Setting<Boolean> list = this.register(new Setting<Boolean>("List", Boolean.FALSE, v -> this.moduleMessage.getValue()));
    private boolean check;

    public Notifications() {
        super("Notifications", "Sends Messages.", Module.Category.CHAT, true, false, false);
        this.setInstance();
    }

    public static Notifications getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    public static void displayCrash(Exception e) {
        Command.sendMessage("\u00a7cException caught: " + e.getMessage());
    }

    private void setInstance() {
        INSTANCE = this;
    }


    public String getNotifierOn(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
				case CRACKED: {
                    String text = (OyVey.commandManager.getClientMessage()) + " " + Formatting.BOLD + module.getDisplayName() + Formatting.RESET + Formatting.GREEN + " enabled.";
                    return text;
                }
                case FUTURE: {
                    String text = Formatting.RED + "[Future] " + Formatting.GRAY + module.getDisplayName() + " toggled " + Formatting.GREEN + "on" + Formatting.GRAY + ".";
                    return text;
                }
                case PHOBOS: {
                    final String text = Formatting.BOLD + module.getDisplayName() + Formatting.RESET + Formatting.GREEN + " enabled.";
                    return text;
                }
                case DOTGOD: {
                    String text = Formatting.DARK_PURPLE + "[" + Formatting.LIGHT_PURPLE + "DotGod.CC" + Formatting.DARK_PURPLE + "] " + Formatting.DARK_AQUA + module.getDisplayName() + Formatting.LIGHT_PURPLE + " was " + Formatting.GREEN + "enabled.";
                    return text;
                }
				case SNOW: {
                    String text = (Object)Formatting.BLUE + "[" + (Object)Formatting.AQUA + "Snow" + (Object)Formatting.BLUE + "] [" + (Object)Formatting.DARK_AQUA + module.getDisplayName() + (Object)Formatting.BLUE + "] " + (Object)Formatting.GREEN + "enabled";
                    return text;
                }
                case WEATHER: {
                    String text = (Object)Formatting.AQUA + "[" + (Object)Formatting.AQUA + "Weather" + (Object)Formatting.AQUA + "] " + (Object)Formatting.DARK_AQUA + module.getDisplayName() + (Object)Formatting.WHITE + " was toggled " + (Object)Formatting.GREEN + "on.";
                    return text;
                }
                case CATALYST: {
                    String text = (Object)Formatting.DARK_GRAY + "[" + (Object)Formatting.AQUA + "Catalyst" + (Object)Formatting.DARK_GRAY + "] " + (Object)Formatting.GRAY + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.GREEN + " ON";
                    return text;
                }
                case RUSHERHACK: {
                    String text = (Object)Formatting.WHITE + "[" + (Object)Formatting.GREEN + "rusherhack" + (Object)Formatting.WHITE + "] " + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.WHITE + " has been enabled";
                    return text;
                }
                case KONAS: {
                    String text = (Object)Formatting.DARK_GRAY + "[" + (Object)Formatting.LIGHT_PURPLE + "Konas" + (Object)Formatting.DARK_GRAY + "] " + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.WHITE + " has been enabled";
                    return text;
                }
                case LEGACY: {
                    String text = (Object)Formatting.WHITE + "[" + (Object)Formatting.LIGHT_PURPLE + "Legacy" + (Object)Formatting.WHITE + "] " + (Object)Formatting.BOLD + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.GREEN + " enabled.";
                    return text;
                }
                case EUROPA: {
                    String text = (Object)Formatting.GRAY + "[" + (Object)Formatting.RED + "Europa" + (Object)Formatting.GRAY + "] " + (Object)Formatting.RESET + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.GREEN + (Object)Formatting.BOLD + " Enabled!";
                    return text;
                }
                case PYRO: {
                    String text = (Object)Formatting.DARK_RED + "[" + (Object)Formatting.DARK_RED + "Pyro" + (Object)Formatting.DARK_RED + "] " + (Object)Formatting.GREEN + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.GREEN + " has been enabled.";
                    return text;
                }
                case MUFFIN: {
                    String text = (Object)Formatting.LIGHT_PURPLE + "[" + (Object)Formatting.DARK_PURPLE + "Muffin" + (Object)Formatting.LIGHT_PURPLE + "] " + (Object)Formatting.LIGHT_PURPLE + module.getDisplayName() + (Object)Formatting.DARK_PURPLE + " was " + (Object)Formatting.GREEN + "enabled.";
                    return text;
                }
				case ABYSS: {
                    String text = TextUtil.coloredString("[Abyss] ", ModuleTools.getInstance().abyssColor.getPlannedValue()) + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.GREEN + " ON";
                    return text;
                }
				case LUIGIHACK: {
                    String text = (Object)Formatting.GREEN + "[LuigiHack] " + (Object)Formatting.GRAY + module.getDisplayName() + " toggled " + (Object)Formatting.GREEN + "on" + (Object)Formatting.GRAY + ".";
                    return text;
                }
            }
        }
        String text = OyVey.commandManager.getClientMessage() + " " + Formatting.GREEN + module.getDisplayName() + " toggled on.";
        return text;
    }

    public String getNotifierOff(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
				case CRACKED: {
                    String text = (OyVey.commandManager.getClientMessage()) + " " + Formatting.BOLD + module.getDisplayName() + Formatting.RESET + Formatting.RED + " disabled.";
                    return text;
                }
                case FUTURE: {
                    String text = Formatting.RED + "[Future] " + Formatting.GRAY + module.getDisplayName() + " toggled " + Formatting.RED + "off" + Formatting.GRAY + ".";
                    return text;
                }
                case PHOBOS: {
                    String text = Formatting.BOLD + module.getDisplayName() + Formatting.RESET + Formatting.RED + " disabled.";
                    return text;
                }
                case DOTGOD: {
                    String text = Formatting.DARK_PURPLE + "[" + Formatting.LIGHT_PURPLE + "DotGod.CC" + Formatting.DARK_PURPLE + "] " + Formatting.DARK_AQUA + module.getDisplayName() + Formatting.LIGHT_PURPLE + " was " + Formatting.RED + "disabled.";
                    return text;
                }
				case SNOW: {
                    String text = (Object)Formatting.BLUE + "[" + (Object)Formatting.AQUA + "Snow" + (Object)Formatting.BLUE + "] [" + (Object)Formatting.DARK_AQUA + module.getDisplayName() + (Object)Formatting.BLUE + "] " + (Object)Formatting.RED + "disabled";
                    return text;
                }
                case WEATHER: {
                    String text = (Object)Formatting.AQUA + "[" + (Object)Formatting.AQUA + "Weather" + (Object)Formatting.AQUA + "] " + (Object)Formatting.DARK_AQUA + module.getDisplayName() + (Object)Formatting.WHITE + " was toggled " + (Object)Formatting.RED + "off.";
                    return text;
                }
                case CATALYST: {
                    String text = (Object)Formatting.DARK_GRAY + "[" + (Object)Formatting.AQUA + "Catalyst" + (Object)Formatting.DARK_GRAY + "] " + (Object)Formatting.GRAY + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.RED + " OFF";
                    return text;
                }
                case RUSHERHACK: {
                    String text = (Object)Formatting.WHITE + "[" + (Object)Formatting.GREEN + "rusherhack" + (Object)Formatting.WHITE + "] " + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.WHITE + " has been disabled";
                    return text;
                }
                case LEGACY: {
                    String text = (Object)Formatting.WHITE + "[" + (Object)Formatting.LIGHT_PURPLE + "Legacy" + (Object)Formatting.WHITE + "] " + (Object)Formatting.BOLD + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.RED + " disabled.";
                    return text;
                }
                case KONAS: {
                    String text = (Object)Formatting.DARK_GRAY + "[" + (Object)Formatting.LIGHT_PURPLE + "Konas" + (Object)Formatting.DARK_GRAY + "] " + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.WHITE + " has been disabled";
                    return text;
                }
                case EUROPA: {
                    String text = (Object)Formatting.GRAY + "[" + (Object)Formatting.RED + "Europa" + (Object)Formatting.GRAY + "] " + (Object)Formatting.RESET + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.RED + (Object)Formatting.BOLD + " Disabled!";
                    return text;
                }
                case PYRO: {
                    String text = (Object)Formatting.DARK_RED + "[" + (Object)Formatting.DARK_RED + "Pyro" + (Object)Formatting.DARK_RED + "] " + (Object)Formatting.RED + module.getDisplayName() + (Object)Formatting.LIGHT_PURPLE + "" + (Object)Formatting.RED + " has been disabled.";
                    return text;
                }
                case MUFFIN: {
                    String text = (Object)Formatting.LIGHT_PURPLE + "[" + (Object)Formatting.DARK_PURPLE + "Muffin" + (Object)Formatting.LIGHT_PURPLE + "] " + (Object)Formatting.LIGHT_PURPLE + module.getDisplayName() + (Object)Formatting.DARK_PURPLE + " was " + (Object)Formatting.RED + "disabled.";
                    return text;
                }
				case ABYSS: {
                    String text = TextUtil.coloredString("[Abyss] ", ModuleTools.getInstance().abyssColor.getPlannedValue()) + (Object)Formatting.WHITE + module.getDisplayName() + (Object)Formatting.RED + " OFF";
                    return text;
                }
				case LUIGIHACK: {
                    String text = (Object)Formatting.GREEN + ("[LuigiHack] ") + (Object)Formatting.GRAY + module.getDisplayName() + " toggled " + (Object)Formatting.RED + "off" + (Object)Formatting.GRAY + ".";
                    return text;
                }
            }
        }
        String text = OyVey.commandManager.getClientMessage() + " " + Formatting.RED + module.getDisplayName() + " toggled off.";
        return text;
    }

    //@SubscribeEvent
    @Subscribe
    public void onToggleModule(ClientEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ChatHud chatHud = mc.inGameHud.getChatHud();
        int moduleNumber;
        Module module;
        if (!this.moduleMessage.getValue()) {
            return;
        }
        if (!(event.getStage() != 0 || (module = (Module) event.getFeature()).equals(this) || !modules.contains(module.getDisplayName()) && this.list.getValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            chatHud.addMessage(Text.literal(getNotifierOff(module)));
        }

        if (event.getStage() == 1 && (modules.contains((module = (Module) event.getFeature()).getDisplayName()) || !this.list.getValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            chatHud.addMessage(Text.literal(getNotifierOn(module)));
        }
    }
}
