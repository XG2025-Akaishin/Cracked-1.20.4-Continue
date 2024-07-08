package me.alpha432.oyvey.features.modules.player;

import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import me.alpha432.oyvey.features.modules.Module;

import me.alpha432.oyvey.features.settings.Setting;

import com.google.common.eventbus.Subscribe;

import me.alpha432.oyvey.event.impl.PacketEvent;

public class XCarry extends Module {
            private static XCarry INSTANCE = new XCarry();
    public XCarry() {
        super("XCarry", "XCarry", Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static XCarry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XCarry();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Subscribe//@Override
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket) e.cancel();
    }
}
