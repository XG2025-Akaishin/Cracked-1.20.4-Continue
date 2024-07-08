package me.alpha432.oyvey.event.impl.autototem;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.event.Event;

public class EventSetBlockState extends Event {
    private final BlockPos pos;
    private final BlockState state;
    private final BlockState prevState;

    public EventSetBlockState(BlockPos pos, BlockState state, BlockState prevState) {
        this.pos = pos;
        this.state = state;
        this.prevState = prevState;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public BlockState getPrevState() {
        return prevState;
    }
}
