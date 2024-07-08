package me.alpha432.oyvey.features.modules.movement.autosprint;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.autosprint.util.MovementUtility;
import me.alpha432.oyvey.features.settings.Setting;

public class AutoSprint extends Module {
    private static AutoSprint INSTANCE = new AutoSprint();
    public final Setting<Boolean> sprint = this.register(new Setting<>("KeepSprint", true));
    public final Setting<Float> motion = this.register(new Setting("Motion", 1f, 0f, 1f, v -> sprint.getValue()));
    private final Setting<Boolean> stopWhileUsing = this.register(new Setting<>("StopWhileUsing", false));
    private final Setting<Boolean> legit = this.register(new Setting<>("Legit", false));

    public AutoSprint() {
        super("AutoSprint", "AutoSprint lol", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static AutoSprint getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoSprint();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }


    @Override
    public void onUpdate() {
        if (mc.player.getHungerManager().getFoodLevel() <= 6) return;
        if (mc.player.horizontalCollision) return;
        if (mc.player.input.movementForward < 0) return;
        if (mc.player.isSneaking()) return;
        if (mc.player.isUsingItem() && stopWhileUsing.getValue()) return;
        if (!MovementUtility.isMoving()) return;
        mc.player.setSprinting(true);
    }
}
