package me.alpha432.oyvey.mixin.autototem;

import com.mojang.authlib.GameProfile;

import me.alpha432.oyvey.event.impl.autototem.EventPostSync;
import me.alpha432.oyvey.event.impl.autototem.EventSync;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

import org.apache.logging.log4j.core.Core;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static me.alpha432.oyvey.util.traits.Util.mc;
import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;
import static me.alpha432.oyvey.features.Feature.fullNullCheck;
import static me.alpha432.oyvey.features.modules.Module.fullNullCheck;

@Mixin(value = ClientPlayerEntity.class, priority = 800)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
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

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPacketsHook(CallbackInfo info) {
        if (fullNullCheck()) return;
        EventSync event = new EventSync(getYaw(), getPitch());
        EVENT_BUS.post(event);
        //EventSprint e = new EventSprint(isSprinting());
        //EVENT_BUS.post(e);
        //EVENT_BUS.post(new EventAfterRotate());
        //if (e.getSprintState() != mc.player.lastSprinting) {
        //    if (e.getSprintState())
        //        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_SPRINTING));
        //    else
        //        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

        //    mc.player.lastSprinting = e.getSprintState();
        //}
        //pre_sprint_state = mc.player.lastSprinting;
        //Core.lockSprint = true;

        if (event.isCancelled()) info.cancel();
    }

    @Inject(method = "sendMovementPackets", at = @At("RETURN"), cancellable = true)
    private void sendMovementPacketsPostHook(CallbackInfo info) {
        if (fullNullCheck()) return;
        mc.player.lastSprinting = pre_sprint_state;
        //Core.lockSprint = false;
        EventPostSync event = new EventPostSync();
        EVENT_BUS.post(event);
        if (event.isCancelled()) info.cancel();
    }
}
