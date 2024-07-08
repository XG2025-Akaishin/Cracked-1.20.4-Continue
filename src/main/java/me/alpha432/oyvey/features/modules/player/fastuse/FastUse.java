package me.alpha432.oyvey.features.modules.player.fastuse;

import me.alpha432.oyvey.mixin.fastuse.IMinecraftClient;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", "Fast Custom", Category.PLAYER, true, false, false);
    }

    private final Setting<Integer> delay = this.register(new Setting<>("Delay", 0, 0, 5));
    public Setting<Boolean> blocks = this.register(new Setting<>("Blocks", false));
    public Setting<Boolean> crystals = this.register(new Setting<>("Crystals", false));
    public Setting<Boolean> xp = this.register(new Setting<>("XP", false));
    public Setting<Boolean> all = this.register(new Setting<>("All", true));

    @Override
    public void onUpdate() {
        if (check(mc.player.getMainHandStack().getItem()) && ((IMinecraftClient) mc).getUseCooldown() > delay.getValue())
            ((IMinecraftClient) mc).setUseCooldown(delay.getValue());
    }

    public boolean check(Item item) {
        return (item instanceof BlockItem && blocks.getValue())
                || (item == Items.END_CRYSTAL && crystals.getValue())
                || (item == Items.EXPERIENCE_BOTTLE && xp.getValue())
                || (all.getValue());
    }
}
