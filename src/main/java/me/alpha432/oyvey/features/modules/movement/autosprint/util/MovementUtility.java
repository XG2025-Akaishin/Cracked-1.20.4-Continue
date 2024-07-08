package me.alpha432.oyvey.features.modules.movement.autosprint.util;

import static me.alpha432.oyvey.features.modules.movement.autosprint.util.Util.mc;

public final class MovementUtility {

    public static boolean isMoving() {
        return mc.player.input.movementForward != 0.0 || mc.player.input.movementSideways != 0.0;
    }

}
