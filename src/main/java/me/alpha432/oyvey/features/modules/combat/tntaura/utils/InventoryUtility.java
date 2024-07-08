package me.alpha432.oyvey.features.modules.combat.tntaura.utils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;

import me.alpha432.oyvey.OyVey;

import static me.alpha432.oyvey.util.traits.Util.mc;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public final class InventoryUtility {
    private static int cachedSlot = -1;

     public static void saveSlot() {
        cachedSlot = mc.player.getInventory().selectedSlot;
    }
    public static void returnSlot() {
        if (cachedSlot != -1)
            switchTo(cachedSlot);
        cachedSlot = -1;
    }
    public static void switchTo(int slot) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        if (mc.player.getInventory().selectedSlot == slot && OyVey.playerManager.serverSideSlot == slot) return;
        mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }
}
