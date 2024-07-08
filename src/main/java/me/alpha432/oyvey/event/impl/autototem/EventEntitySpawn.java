package me.alpha432.oyvey.event.impl.autototem;

import me.alpha432.oyvey.event.Event;
import net.minecraft.entity.Entity;

public class EventEntitySpawn extends Event {
    private final Entity entity;
    public EventEntitySpawn(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
