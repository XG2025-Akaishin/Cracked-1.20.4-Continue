package me.alpha432.oyvey.features.modules.render.cameraclip.util;

public class AnimationUtility {
    public static double deltaTime() {
        return FrameRateCounter.INSTANCE.getFps() > 0 ? (1.0000 / FrameRateCounter.INSTANCE.getFps()) : 1;
    }

    public static float fast(float end, float start, float multiple) {
        return (1 - MathUtility.clamp((float) (deltaTime() * multiple), 0, 1)) * end + MathUtility.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }
}
