package me.alpha432.oyvey.mixin.portalchat;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface IEntity {
    @Accessor("inNetherPortal")
    void setInNetherPortal(boolean bool);
}
