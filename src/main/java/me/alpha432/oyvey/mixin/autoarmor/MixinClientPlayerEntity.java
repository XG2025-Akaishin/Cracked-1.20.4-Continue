package me.alpha432.oyvey.mixin.autoarmor;

import com.mojang.authlib.GameProfile;

import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.impl.autoarmor.PlayerMotionUpdate;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;

@Mixin({ClientPlayerEntity.class})
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    private MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets$Inject$HEAD(CallbackInfo p_Info) {
        //if(PlayerUtil.rotating)
        //{
        //    PlayerUtil.rotating = false;
        //    return;
        // }

        PlayerMotionUpdate event = new PlayerMotionUpdate(Stage.PRE);
        EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            p_Info.cancel();
        }
    }
}
