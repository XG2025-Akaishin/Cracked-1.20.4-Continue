package me.alpha432.oyvey.features.modules.render.cameraclip;

import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.render.AspectRatio;
import me.alpha432.oyvey.features.settings.Setting;

import me.alpha432.oyvey.features.modules.render.cameraclip.util.AnimationUtility;

public class NoCameraClip extends Module {
    
    private static NoCameraClip INSTANCE = new NoCameraClip();
    public Setting<Boolean> antiFront = this.register(new Setting<>("AntiFront", false));
    public Setting<Float> distance = this.register(new Setting<>("Distance", 3.8f, 1f, 20f));
    private float animation;

    public NoCameraClip() {
        super("NoCameraClip", "NoCameraClip", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static NoCameraClip getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoCameraClip();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onRender3D(MatrixStack matrix) {
        if (mc.options.getPerspective() == Perspective.FIRST_PERSON) animation = AnimationUtility.fast(animation, 0f, 10);
        else animation = AnimationUtility.fast(animation, 1f, 10);

        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT && antiFront.getValue())
            mc.options.setPerspective(Perspective.FIRST_PERSON);
    }

    public double getDistance() {
        //1f +
        return distance.getValue() + ((distance.getValue() - 1f) * animation);
    }
}
