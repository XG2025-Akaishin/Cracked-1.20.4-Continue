package me.alpha432.oyvey.manager.tntmanager;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import me.alpha432.oyvey.OyVey;

import static me.alpha432.oyvey.util.traits.Util.mc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TntCManager {
    public HashMap<String, Integer> popList = new HashMap<>();



    public @Nullable PlayerEntity getNearestTarget(float range) {
        return getTargets(range).stream().min(Comparator.comparing(t -> mc.player.distanceTo(t))).orElse(null);
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
}
