package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.ItemFrameEntity;

public class AutoBedDupe
        extends Module {
    private final Setting<Boolean> shulkersonly  = this.register(new Setting<Boolean>("ShulkersOnly", true));
    private final Setting<Integer> range  = this.register(new Setting<Integer>("Range", 5, 0, 6));
    private final Setting<Integer> turns  = this.register(new Setting<Integer>("Turns", 1, 0, 5));
    private final Setting<Integer> ticks  = this.register(new Setting<Integer>("Ticks", 10, 1, 20));
    private int timeoutTicks = 0;

    public AutoBedDupe() {
        super("AutoBedDupe", "Best on 5b5t.org and 6b6t.org", Module.Category.MISC, true, false ,false);
    }

    public void onUpdate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        for (int x = Math.max((int) mc.player.getX() - range.getValue(), 0); x < Math.min((int) mc.player.getX() + range.getValue(), 256); x++) {
            for (int y = Math.max((int) mc.player.getY() - range.getValue(), 0); y < Math.min((int) mc.player.getY() + range.getValue(), 256); y++) {
                for (int z = Math.max((int) mc.player.getZ() - range.getValue(), 0); z < Math.min((int) mc.player.getZ() + range.getValue(), 256); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = mc.world.getBlockState(pos);
        
                    if (state.getBlock() instanceof BedBlock) {
                        if (timeoutTicks >= ticks.getValue()) {
                            for (int i = 0; i < turns.getValue(); i++) {
                            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,  new BlockHitResult(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Direction.DOWN, pos, true));//(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Direction.DOWN, pos, true));
                            }
                            timeoutTicks = 0;
                        }
                        ++timeoutTicks;
                    }
                }
            }
        }
    }
}
