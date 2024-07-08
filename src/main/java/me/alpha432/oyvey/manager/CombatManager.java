package me.alpha432.oyvey.manager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.eventbus.Subscribe;

import me.alpha432.oyvey.event.impl.autototem.EventPostSync;
import me.alpha432.oyvey.event.impl.autototem.TotemPopEvent;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PacketEvent;
import static me.alpha432.oyvey.util.traits.Util.mc;
import static me.alpha432.oyvey.util.traits.Util.EVENT_BUS;
import static me.alpha432.oyvey.features.Feature.fullNullCheck;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CombatManager implements IManager {
    public HashMap<String, Integer> popList = new HashMap<>();

    //@EventHandler
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fullNullCheck()) return;

        if (event.getPacket() instanceof EntityStatusS2CPacket pac) {
            if (pac.getStatus() == EntityStatuses.USE_TOTEM_OF_UNDYING) {
                Entity ent = pac.getEntity(mc.world);
                if (!(ent instanceof PlayerEntity)) return;
                if (popList == null) {
                    popList = new HashMap<>();
                }
                if (popList.get(ent.getName().getString()) == null) {
                    popList.put(ent.getName().getString(), 1);
                } else if (popList.get(ent.getName().getString()) != null) {
                    popList.put(ent.getName().getString(), popList.get(ent.getName().getString()) + 1);
                }
                EVENT_BUS.post(new TotemPopEvent((PlayerEntity) ent, popList.get(ent.getName().getString())));
            }
        }
    }

    //@EventHandler
    @Subscribe
    public void onPostTick(EventPostSync event) {
        if (fullNullCheck())
            return;
        for (PlayerEntity player : mc.world.getPlayers()) {
            //if (AntiBot.bots.contains(player)) continue;

            if (player.getHealth() <= 0 && popList.containsKey(player.getName().getString()))
                popList.remove(player.getName().getString(), popList.get(player.getName().getString()));
        }
    }

    public int getPops(@NotNull PlayerEntity entity) {
        if (popList.get(entity.getName().getString()) == null) return 0;
        return popList.get(entity.getName().getString());
    }

    public List<PlayerEntity> getTargets(float range) {
        return mc.world.getPlayers().stream()
                .filter(e -> !e.isDead())
                .filter(entityPlayer -> !OyVey.friendManager.isFriend(entityPlayer.getName().getString()))
                .filter(entityPlayer -> entityPlayer != mc.player)
                .filter(entityPlayer -> mc.player.squaredDistanceTo(entityPlayer) < range * range)
                .sorted(Comparator.comparing(e -> mc.player.distanceTo(e)))
                .collect(Collectors.toList());
    }

    public @Nullable PlayerEntity getTarget(float range, @NotNull TargetBy targetBy) {
        PlayerEntity target = null;

        switch (targetBy) {
            case FOV -> target = getTargetByFOV(range);
            case Health -> target = getTargetByHealth(range);
            case Distance -> target = getNearestTarget(range);
        }

        return target;
    }

    public @Nullable PlayerEntity getNearestTarget(float range) {
        return getTargets(range).stream().min(Comparator.comparing(t -> mc.player.distanceTo(t))).orElse(null);
    }

    public PlayerEntity getTargetByHealth(float range) {
        return getTargets(range).stream().min(Comparator.comparing(t -> (t.getHealth() + t.getAbsorptionAmount()))).orElse(null);
    }

    public PlayerEntity getTargetByFOV(float range) {
        return getTargets(range).stream().min(Comparator.comparing(this::getFOVAngle)).orElse(null);
    }

    private float getFOVAngle(@NotNull LivingEntity e) {
        double difX = e.getX() - mc.player.getPos().x;
        double difZ = e.getZ() - mc.player.getPos().z;
        float yaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0);
        double plYaw = MathHelper.wrapDegrees(mc.player.getYaw());
        return (float) Math.abs(yaw - plYaw);
    }

    public enum TargetBy {
        Distance,
        FOV,
        Health
    }
}
