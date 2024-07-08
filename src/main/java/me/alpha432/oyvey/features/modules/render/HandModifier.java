package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.mixin.handmodifier.*;
import me.alpha432.oyvey.event.impl.PacketEvent;

import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;

public class HandModifier extends Module {

    public static HandModifier INSTANCE;

    public Setting<Boolean> shouldScale = this.register(new Setting<>("ShouldScale", true));
    public Setting<Float> scale = this.register(new Setting<>("Scale", 0.2f, 0.0f, 2.0f));
    public Setting<Boolean> shouldSlow = this.register(new Setting<>("ShouldSlow", false));
    public Setting<Integer> slow = this.register(new Setting<>("Slow", 240, 0, 255));
    public Setting<Boolean> old = this.register(new Setting<>("Old", false));
    public Setting<Boolean> noSwing = this.register(new Setting<>("NoSwing", false));

    public HandModifier() {
        super("HandModifier", "HandModifier", Module.Category.RENDER, true, false, false);
        INSTANCE = this;
    }


    private void onPacketSend(PacketEvent.Send event) {
        if (noSwing.getValue() && event.getPacket() instanceof HandSwingC2SPacket) mc.player.handSwinging = false;
    }

    private void onPacketRecieve(PacketEvent.Receive event) {
        if (noSwing.getValue() && event.getPacket() instanceof HandSwingC2SPacket) mc.player.handSwinging = false;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null) return;

        if (old.getValue() && ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).getEquippedProgressMainHand() <= 1f) {
            ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).setEquippedProgressMainHand(1f);
            ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).setItemStackMainHand(mc.player.getMainHandStack());
        }
        if (old.getValue() && ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).getEquippedProgressOffHand() <= 1) {
            ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).setEquippedProgressOffHand(1f);
            ((IHeldItemRenderer) mc.getEntityRenderDispatcher().getHeldItemRenderer()).setItemStackOffHand(mc.player.getOffHandStack());
        }
    }
}
