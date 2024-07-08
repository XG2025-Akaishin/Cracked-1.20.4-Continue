package me.alpha432.oyvey.features.modules.misc.autoframedupe;


import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import me.alpha432.oyvey.event.impl.framedupe.GameLeftEvent;
import me.alpha432.oyvey.event.impl.framedupe.InteractEntityEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

import java.util.List;

import com.google.common.eventbus.Subscribe;

public class FrameDupeTest extends Module {
    //en78
    public Setting<Boolean> autoDisable = this.register(new Setting<>("AutoDisable", false));
    public Setting<Boolean> alwaysActive = this.register(new Setting<>("AlwaysActive", false));
    public Setting<Integer> destroyTime = this.register(new Setting<>("DestroyTime", 50, 1, 255));
    public boolean isDuping = false;
    private Thread thread = null;

    public FrameDupeTest() {
        super("FrameDupeTest", "AutoFrameDupe 1.20.1", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        thread = new Thread(this::doItemFrameDupe);
        thread.start();
    }

    @Override
    public void onDisable() {
        if (thread != null && thread.isAlive()) thread.stop();
    }

    public boolean getShouldDupe() {
        if (!alwaysActive.getValue()) return mc.mouse.wasRightButtonClicked();
        return true;
    }

    @Subscribe//@EventHandler
    public void onInteractItemFrame(InteractEntityEvent event) {
        if (getShouldDupe()) {
            if (!isDuping) {
                if (!(event.entity instanceof ItemFrameEntity)) return;
                thread = new Thread(this::doItemFrameDupe);
                thread.start();
            }
        }
    }

    public void doItemFrameDupe() throws AssertionError {
        isDuping = true;

        while (getShouldDupe()) {
            try {
                long sleepTime = (long) (destroyTime.getValue() * 0.5);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Box box = new Box(mc.player.getEyePos().add(-3, -3, -3), mc.player.getEyePos().add(3, 3, 3));
            List<ItemFrameEntity> itemFrames = mc.world.getEntitiesByClass(ItemFrameEntity.class, box, itemFrameEntity -> true);

            if (!itemFrames.isEmpty()) {
                ItemFrameEntity itemFrame = itemFrames.get(0);

                mc.interactionManager.interactEntity(mc.player, itemFrame, Hand.MAIN_HAND);

                try {
                    Thread.sleep((long) (destroyTime.getValue() * 0.7));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (itemFrame.getHeldItemStack().getCount() > 0)
                    mc.interactionManager.interactEntity(mc.player, itemFrame, Hand.MAIN_HAND);

                try {
                    Thread.sleep((long) (destroyTime.getValue() * 0.7));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mc.interactionManager.attackEntity(mc.player, itemFrame);

                try {
                    Thread.sleep((long) (destroyTime.getValue() * 0.7));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else break;
        }

        isDuping = false;
    }

    @Subscribe//@EventHandler
    private void onGameLeft(GameLeftEvent event) { //remove inecesari
        if (autoDisable.getValue()) toggle();
    }
}