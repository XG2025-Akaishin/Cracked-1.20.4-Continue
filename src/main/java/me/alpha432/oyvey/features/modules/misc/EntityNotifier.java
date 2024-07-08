package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.GhastEntity;

import java.awt.*;
import net.minecraft.entity.passive.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

import java.util.*;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class EntityNotifier extends Module {
    private final Set<Entity> ghasts;
    private final Set<Entity> donkeys;
    private final Set<Entity> mules;
    private final Set<Entity> llamas;
    public Setting<Boolean> Chat;
    public Setting<Boolean> Sound;
    public Setting<Boolean> Ghasts;
    public Setting<Boolean> Donkeys;
    public Setting<Boolean> Mules;
    public Setting<Boolean> Llamas;
    
    public EntityNotifier() {
        super("EntityNotifier", "Helps you find certain things", Module.Category.PLAYER, true, false, false);
        this.ghasts = new HashSet<Entity>();
        this.donkeys = new HashSet<Entity>();
        this.mules = new HashSet<Entity>();
        this.llamas = new HashSet<Entity>();
        this.Chat = (Setting<Boolean>)this.register(new Setting("Chat", true));
        this.Sound = (Setting<Boolean>)this.register(new Setting("Sound", true));
        this.Ghasts = (Setting<Boolean>)this.register(new Setting("Ghasts", true));
        this.Donkeys = (Setting<Boolean>)this.register(new Setting("Donkeys", true));
        this.Mules = (Setting<Boolean>)this.register(new Setting("Mules", true));
        this.Llamas = (Setting<Boolean>)this.register(new Setting("Llamas", true));
    }
    
    public void onEnable() {
        this.ghasts.clear();
        this.donkeys.clear();
        this.mules.clear();
        this.llamas.clear();
    }
    
    public void onUpdate() {
        if (this.Ghasts.getValue()) {
            for (final Entity entity : EntityNotifier.mc.world.getEntities()) {
                if (entity instanceof GhastEntity) {
                    if (this.ghasts.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Ghast Detected at: " + Math.round((float)entity.getX()) + "x, " + Math.round((float)entity.getY()) + "y, " + Math.round((float)entity.getZ()) + "z.");
                    }
                    this.ghasts.add(entity);

                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                }
            }
        }
        if (this.Donkeys.getValue()) {
            for (final Entity entity : EntityNotifier.mc.world.getEntities()) {
                if (entity instanceof DonkeyEntity) {
                    if (this.donkeys.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Donkey Detected at: " + Math.round((float)entity.getX()) + "x, " + Math.round((float)entity.getY()) + "y, " + Math.round((float)entity.getZ()) + "z.");
                    }
                    this.donkeys.add(entity);

                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                }
            }
        }
        if (this.Mules.getValue()) {
            for (final Entity entity : EntityNotifier.mc.world.getEntities()) {
                if (entity instanceof MuleEntity) {
                    if (this.mules.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Mule Detected at: " + Math.round((float)entity.getX()) + "x, " + Math.round((float)entity.getY()) + "y, " + Math.round((float)entity.getZ()) + "z.");
                    }
                    this.mules.add(entity);
                    
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                }
            }
        }
        if (this.Llamas.getValue()) {
            for (final Entity entity : EntityNotifier.mc.world.getEntities()) {
                if (entity instanceof LlamaEntity) {
                    if (this.llamas.contains(entity)) {
                        continue;
                    }
                    if (this.Chat.getValue()) {
                        Command.sendMessage("Llama Detected at: " + Math.round((float)entity.getX()) + "x, " + Math.round((float)entity.getY()) + "y, " + Math.round((float)entity.getZ()) + "z.");
                    }
                    this.llamas.add(entity);
                    
                    if (!this.Sound.getValue()) {
                        continue;
                    }
                    EntityNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                }
            }
        }
    }
}
