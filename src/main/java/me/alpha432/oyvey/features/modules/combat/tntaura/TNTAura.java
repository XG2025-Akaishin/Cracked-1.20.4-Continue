package me.alpha432.oyvey.features.modules.combat.tntaura;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.TntBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.alpha432.oyvey.features.modules.combat.tntaura.utils.Timer;
import me.alpha432.oyvey.features.modules.combat.tntaura.utils.InteractionUtility;
import me.alpha432.oyvey.features.modules.combat.tntaura.utils.InventoryUtility;
import me.alpha432.oyvey.features.modules.combat.tntaura.utils.PlayerUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.tntaura.EventPostSync;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class TNTAura extends Module {
    public TNTAura() {
        super("TNTAura", "TNTAura", Category.COMBAT, true, false, false);
    }

    private final Setting<Float> range = this.register(new Setting<>("Range", 5f, 2f, 7f));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<>("Block/Tick", 8, 1, 12));
    private final Setting<Integer> placeDelay = this.register(new Setting<>("Delay/Place", 3, 0, 10));
    private final Setting<InteractionUtility.PlaceMode> placeMode = this.register(new Setting<>("PlaceMode", InteractionUtility.PlaceMode.Normal));
    private final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));
    public static Timer inactivityTimer = new Timer();

    private int delay;

    @Subscribe//@EventHandler
    public void onPostSync(EventPostSync e) {
        if (getTntSlot() == -1) {
            Command.sendMessage( "no tnt");
            disable("No tnt");
            return;
        }
        if (getFlintSlot() == -1) {
            Command.sendMessage( "No flint and steel!");
            disable("No flint and steel!");
            return;
        }
        if (getObbySlot() == -1) {
            Command.sendMessage( "No obsidian!");
            disable("No obsidian!");
            return;
        }

        PlayerEntity pl = OyVey.tntCManager.getNearestTarget(range.getValue());

        List<BlockPos> blocks = getBlocks(pl);
        if (!blocks.isEmpty()) {
            if (delay > 0) {
                delay--;
                return;
            }

            InventoryUtility.saveSlot();
            int placed = 0;
            while (placed < blocksPerTick.getValue()) {
                BlockPos targetBlock = getSequentialPos(pl);
                if (targetBlock == null)
                    break;
                if (InteractionUtility.placeBlock(targetBlock, rotate.getValue(), InteractionUtility.Interact.Vanilla, placeMode.getValue(), getObbySlot(), false, false)) {
                    placed++;
                    delay = placeDelay.getValue();
                    inactivityTimer.reset();
                    //renderPoses.put(targetBlock, System.currentTimeMillis());
                } else break;
            }
            InventoryUtility.returnSlot();
        }

        if (pl != null) {
            BlockPos headBlock = BlockPos.ofFloored(pl.getPos()).up(2);
            InventoryUtility.saveSlot();
            InteractionUtility.placeBlock(headBlock, rotate.getValue(), InteractionUtility.Interact.Vanilla, placeMode.getValue(), getTntSlot(), false, false);
            BlockHitResult igniteResult = getIgniteResult(headBlock);
            InventoryUtility.switchTo(getFlintSlot());
            if (mc.world.getBlockState(headBlock).getBlock() instanceof TntBlock && igniteResult != null) {
                sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, igniteResult, PlayerUtility.getWorldActionId(mc.world)));
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            //renderPoses.put(headBlock, System.currentTimeMillis());
            InventoryUtility.returnSlot();
        }
    }

    private BlockPos getSequentialPos(PlayerEntity pl) {
        List<BlockPos> list = getBlocks(pl);
        if (list.isEmpty()) return null;
        for (BlockPos bp : getBlocks(pl)) {
            if (InteractionUtility.canPlaceBlock(bp, InteractionUtility.Interact.Vanilla, false) && mc.world.isAir(bp)) {
                return bp;
            }
        }
        return null;
    }

    private List<BlockPos> getBlocks(PlayerEntity pl) {
        if (pl == null) return new ArrayList<>();
        List<BlockPos> blocks = new ArrayList<>();
        for (BlockPos bp : getAffectedBlocks(pl)) {
            for (Direction dir : Direction.values()) {
                if (dir == Direction.UP || dir == Direction.DOWN) continue;
                blocks.add(bp.offset(dir));
                blocks.add(bp.offset(dir).up());

                if (!new Box(bp.offset(dir).up(1)).intersects(pl.getBoundingBox()))
                    blocks.add(bp.offset(dir).up(2));

                blocks.add(bp.offset(dir).down());
            }

            blocks.add(bp.down());
            blocks.add(bp.up(3));

            if (!InteractionUtility.canPlaceBlock(bp.up(3), InteractionUtility.Interact.Vanilla, false)) {
                Direction dir = mc.player.getHorizontalFacing();
                if (dir != null) {
                    blocks.add(bp.up(3).offset(dir, 1));
                }
            }
        }

        return blocks.stream().sorted(Comparator.comparing(b -> mc.player.squaredDistanceTo(b.toCenterPos()) * -1)).toList();
    }

    private int getObbySlot() {
        if (mc.player.getMainHandStack().getItem() == Items.OBSIDIAN)
            return mc.player.getInventory().selectedSlot;

        int slot = -1;

        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private int getTntSlot() {
        if (mc.player.getMainHandStack().getItem() == Items.TNT)
            return mc.player.getInventory().selectedSlot;

        int slot = -1;

        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.TNT) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private int getFlintSlot() {
        if (mc.player.getMainHandStack().getItem() == Items.FLINT_AND_STEEL)
            return mc.player.getInventory().selectedSlot;

        int slot = -1;

        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.FLINT_AND_STEEL) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private @Nullable BlockHitResult getIgniteResult(BlockPos bp) {
        if (mc.player == null || mc.world == null) return null;

        if (PlayerUtility.squaredDistanceFromEyes(bp.toCenterPos()) > range.getPow2Value())
            return null;

        return new BlockHitResult(bp.toCenterPos().add(0, -0.5, 0), Direction.DOWN, bp, false);
    }

    private List<BlockPos> getAffectedBlocks(PlayerEntity pl) {
        List<BlockPos> tempPos = new ArrayList<>();
        List<BlockPos> finalPos = new ArrayList<>();
        List<Box> boxes = new ArrayList<>();

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.squaredDistanceTo(pl) < 9 && player != pl)
                boxes.add(player.getBoundingBox());
        }

        boxes.add(pl.getBoundingBox());

        BlockPos center = getPlayerPos(pl);

        tempPos.add(center);
        tempPos.add(center.north());
        tempPos.add(center.north().east());
        tempPos.add(center.west());
        tempPos.add(center.west().north());
        tempPos.add(center.south());
        tempPos.add(center.south().west());
        tempPos.add(center.east());
        tempPos.add(center.east().south());

        for (BlockPos bp : tempPos)
            if (new Box(bp).intersects(pl.getBoundingBox()))
                finalPos.add(bp);

        for (BlockPos bp : Lists.newArrayList(finalPos)) {
            for (Box box : boxes) {
                if (new Box(bp).intersects(box))
                    finalPos.add(BlockPos.ofFloored(box.getCenter()));
            }
        }

        return finalPos;
    }

    private BlockPos getPlayerPos(@NotNull PlayerEntity pl) {
        return BlockPos.ofFloored(pl.getX(), pl.getY() - Math.floor(pl.getY()) > 0.8 ? Math.floor(pl.getY()) + 1.0 : Math.floor(pl.getY()), pl.getZ());
    }

    //private enum RenderMode {
   //     Fade, Decrease
   // }
}
