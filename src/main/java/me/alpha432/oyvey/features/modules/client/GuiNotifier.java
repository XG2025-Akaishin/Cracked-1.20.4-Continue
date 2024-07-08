package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.modules.chat.moduletools.utils.TextUtil;

public class GuiNotifier
        extends Module {
    private static GuiNotifier INSTANCE;
    //public Setting<GlobalChat> globalchat = register(new Setting<GlobalChat>("GlobalChat", GlobalChat.CRACKED));
    //public Setting<TextUtil.Color> abyssColorGlobal = this.register(new Setting<TextUtil.Color>("AbyssTextColor", TextUtil.Color.AQUA, color -> this.globalchat.getValue() == GlobalChat.ABYSS));
    //public Setting<Notifier> notifier = register(new Setting<Notifier>("ModuleNotifier", Notifier.CRACKED));
    //public Setting<TextUtil.Color> abyssColor = this.register(new Setting<TextUtil.Color>("AbyssTextColor", TextUtil.Color.AQUA, color -> this.notifier.getValue() == Notifier.ABYSS));
    //public Setting<PopNotifier> popNotifier = register(new Setting<PopNotifier>("PopNotifier", PopNotifier.CRACKED));
	//public Setting<TextUtil.Color> abyssColorPop = this.register(new Setting<TextUtil.Color>("AbyssTextColor", TextUtil.Color.AQUA, color -> this.popNotifier.getValue() == PopNotifier.ABYSS));
    public Setting<Integer> red = this.register(new Setting<>("Red", 0, 0, 255));
    public Setting<Integer> green = this.register(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<>("Alpha", 240, 0, 255));
    
    public GuiNotifier() {
        super("GuiNotifier", "Change settings", Category.CHAT, true, false, false);
        INSTANCE = this;
    }


    public static GuiNotifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuiNotifier();
        }
        return INSTANCE;
    }
}