package me.alpha432.oyvey.features.modules.movement.flight;

import com.google.common.eventbus.Subscribe;

import me.alpha432.oyvey.event.impl.freelook.TickEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;



public class Flight extends Module {

    private boolean flying;

        private final Setting<Float> Speed = this.register(new Setting<>("Speed", 0.3f, 0.1f, 5.0f));
        
        public Flight() {
            super("Flight", "Flight 1.20", Category.MOVEMENT, true, false, false);
    }


    public void onEnable() {
        super.onEnable();
        if (mc.player != null) {
            flying = true;
            mc.player.getAbilities().flying = true;
            if (mc.player.getAbilities().creativeMode) return;
            mc.player.getAbilities().allowFlying = true;
        }
    }

    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            flying = false;
            mc.player.getAbilities().flying = false;
            if (mc.player.getAbilities().creativeMode) return;
            mc.player.getAbilities().allowFlying = false;
        }
    }

    //@EventHandler
        @Subscribe
    public void onTick(TickEvent event) {
        //if (event.isPre()) return;
        if (mc.player != null) {
            if (flying) {
                mc.player.getAbilities().setFlySpeed(Speed.getValue());
            }
        }
    }

    public enum Modes {
        Vanilla,
        Creative,
    }
}
