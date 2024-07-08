package me.alpha432.oyvey.features.modules.render.freelook.utils;

import me.alpha432.oyvey.mixin.freelook.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;


public class KeyBinds {
    private static final String CATEGORY = "Meteor Client";
    public static int getKey(KeyBinding bind) {
        return ((KeyBindingAccessor) bind).getKey().getCode();
    }
}
