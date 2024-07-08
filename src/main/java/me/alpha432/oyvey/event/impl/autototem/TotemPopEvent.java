package me.alpha432.oyvey.event.impl.autototem;

import net.minecraft.entity.player.PlayerEntity;
import me.alpha432.oyvey.event.Event;

public class TotemPopEvent extends Event {
    private final PlayerEntity entity;
    private int pops;

    public TotemPopEvent(PlayerEntity entity,int pops) {
        this.entity = entity;
        this.pops = pops;
    }

    public PlayerEntity getEntity() {
        return this.entity;
    }

    public int getPops() {
        return this.pops;
    }
}