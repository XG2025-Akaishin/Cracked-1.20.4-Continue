package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.ItemFrameEntity;

public class AutoFrameDupe
        extends Module {
    private final Setting<Boolean> shulkersonly  = this.register(new Setting<Boolean>("ShulkersOnly", true));
    private final Setting<Integer> range  = this.register(new Setting<Integer>("Range", 5, 0, 6));
    private final Setting<Integer> turns  = this.register(new Setting<Integer>("Turns", 1, 0, 5));
    private final Setting<Integer> ticks  = this.register(new Setting<Integer>("Ticks", 10, 1, 20));
    private int timeoutTicks = 0;

    public AutoFrameDupe() {
        super("AutoFrameDupe", "Best on 5b5t.org and 6b6t.org", Module.Category.MISC, true, false ,false);
    }

    public void onUpdate() {
        MinecraftClient mc = MinecraftClient.getInstance();

            for (Entity frame : mc.world.getEntities()) {
                if (frame instanceof ItemFrameEntity) {
                    if (mc.player.distanceTo(frame) <= range.getValue()) {
                        if (timeoutTicks >= ticks.getValue()) {
                            if (((ItemFrameEntity) frame).getHeldItemStack().isEmpty()) {
                                mc.interactionManager.interactEntity(mc.player, frame, Hand.MAIN_HAND);
                            }
                            if (!((ItemFrameEntity) frame).getHeldItemStack().isEmpty()) {
                                for (int i = 0; i < turns.getValue(); i++) {
                                    mc.interactionManager.interactEntity(mc.player, frame, Hand.MAIN_HAND);
                                }
                                mc.player.attack(frame);
                                mc.interactionManager.attackEntity(mc.player, frame);
                                timeoutTicks = 0;
                            }
                        }
                        ++timeoutTicks;
                    }
                }
            }
    
    }

}
