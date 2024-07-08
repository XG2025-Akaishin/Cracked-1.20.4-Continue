package me.alpha432.oyvey.features.modules.render.fov;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class FOV extends Module {
    private static FOV INSTANCE = new FOV();
    public final Setting<Integer> fovModifier = this.register(new Setting<>("FOV modifier", 120, 0, 358));
    public final Setting<Boolean> itemFov = this.register(new Setting<>("Item Fov", false));
    public final Setting<Integer> itemFovModifier = this.register(new Setting<>("Item FOV modifier", 120, 0, 358));

    public FOV() {
        super("FOV", "Opens FOV", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

        public static FOV getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FOV();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}
