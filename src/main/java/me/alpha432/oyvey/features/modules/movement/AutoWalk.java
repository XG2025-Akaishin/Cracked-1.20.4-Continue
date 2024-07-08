package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;


public class AutoWalk
        extends Module {
			public static AutoWalk INSTANCE = new AutoWalk();
        public AutoWalk() {
            super("AutoWalk", "ChatModifier 1.12.2 Super", Category.MOVEMENT, true, false, false);
		        INSTANCE = this;
	}

	    public static AutoWalk getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoWalk();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

	@Override
	public void onDisable() {
		mc.options.forwardKey.setPressed(false);
	}

	@Override
	public void onUpdate() {
		mc.options.forwardKey.setPressed(true);
    }

}
