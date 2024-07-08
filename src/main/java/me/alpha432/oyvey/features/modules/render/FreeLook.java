package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.freelook.TickEvent;

import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.render.freelook.utils.Input;
import me.alpha432.oyvey.features.settings.Setting;

public class FreeLook extends Module {

    public Setting<Float> sensitivity = this.register(new Setting<>("Sensitivity", 8f, 0f, 10f));
    public Setting<Boolean> togglePerspective = this.register(new Setting<>("TogglePerspective", true));
    public Setting<Boolean> arrows = this.register(new Setting<>("Arrows", true));
    public Setting<Float> arrowSpeed = this.register(new Setting<>("ArrowSpeed", 8f, 0f, 10f));
    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.Player));

    public float cameraYaw;
    public float cameraPitch;

    private Perspective prePers;

    public FreeLook() {
        super("FreeLook", "Opens FreeLook", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {//onActivated
        cameraYaw = mc.player.getYaw();
        cameraPitch = mc.player.getPitch();
        prePers = mc.options.getPerspective();

        if (prePers != Perspective.THIRD_PERSON_BACK &&  togglePerspective.getValue()) mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {//ondisactivate
        if (mc.options.getPerspective() != prePers && togglePerspective.getValue()) mc.options.setPerspective(prePers);
    }

    public boolean playerMode() {
        return isOn() && mc.options.getPerspective() == Perspective.THIRD_PERSON_BACK && mode.getValue() == Mode.Player;
    }
    //IS isActivate )= isEnable(phobos)

    public boolean cameraMode() {
        return isOn() && mode.getValue() == Mode.Camera;
    }


    private void onTick(TickEvent.Post event) {
        if (arrows.getValue()) {
            for (int i = 0; i < (arrowSpeed.getValue() * 2); i++) {
                switch (mode.getValue()) {
                    case Player -> {
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT)) cameraYaw -= 0.5;
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) cameraYaw += 0.5;
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_UP)) cameraPitch -= 0.5;
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_DOWN)) cameraPitch += 0.5;
                    }
                    case Camera -> {
                        float yaw = mc.player.getYaw();
                        float pitch = mc.player.getPitch();

                        if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT)) yaw -= 0.5;
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) yaw += 0.5;
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_UP)) pitch -= 0.5;
                        if (Input.isKeyPressed(GLFW.GLFW_KEY_DOWN)) pitch += 0.5;

                        mc.player.setYaw(yaw);
                        mc.player.setPitch(pitch);
                    }
                }
            }
        }

        mc.player.setPitch(MathHelper.clamp(mc.player.getPitch(), -90, 90));
        cameraPitch = MathHelper.clamp(cameraPitch, -90, 90);
    }

    public enum Mode {
        Player,
        Camera
    }
}
