package me.alpha432.oyvey.features.modules.player.middleclick.util;

import net.minecraft.item.*;

import static me.alpha432.oyvey.features.modules.Module.mc;

import java.util.Arrays;
import java.util.List;

public final class InventoryUtility {
    private static int cachedSlot = -1;


    public static SearchInvResult findInInventory(Searcher searcher) {
        if (mc.player != null) {
            for (int i = 36; i >= 0; i--) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (searcher.isValid(stack)) {
                    if (i < 9) i += 36;
                    return new SearchInvResult(i, true, stack);
                }
            }
        }

        return SearchInvResult.notFound();
    }
    public interface Searcher {
        boolean isValid(ItemStack stack);
    }
    public static SearchInvResult findItemInInventory(List<Item> items) {
        return findInInventory(stack -> items.contains(stack.getItem()));
    }
    public static SearchInvResult findItemInInventory(Item... items) {
        return findItemInInventory(Arrays.asList(items));
    }
}
