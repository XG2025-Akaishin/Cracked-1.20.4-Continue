package me.alpha432.oyvey.mixin.noslow;

import com.mojang.authlib.GameProfile;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.movement.noslow.NoSlow;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;



@Mixin(value = ClientPlayerEntity.class, priority = 800)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
   // public static NoSlow noSlow;//New NoSlow();//Import Externt Similar Register
    @Unique
    boolean pre_sprint_state = false;
    @Unique
    private boolean updateLock = false;

    @Shadow
    public abstract float getPitch(float tickDelta);
    @Shadow
    protected abstract void sendMovementPackets();

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    private boolean tickMovementHook(ClientPlayerEntity player) {
        if (NoSlow.getInstance().isEnabled() && NoSlow.getInstance().canNoSlow())//Posible error posible solucition
            return false;
        return player.isUsingItem();
    }
}
