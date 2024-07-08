package me.alpha432.oyvey.features.modules.combat.autocrystal;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.autoCrystal.DeadManager;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.math.ExplosionUtility;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.math.MathUtility;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.player.InteractionUtility;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.player.InventoryUtility;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.player.PlayerUtility;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.player.SearchInvResult;
import me.alpha432.oyvey.features.modules.combat.autototem.AutoTotem;
import me.alpha432.oyvey.features.modules.combat.autocrystal.AutoCrystal.Recalc;
import me.alpha432.oyvey.features.modules.combat.autocrystal.utils.Timer;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.event.impl.autototem.EventEntitySpawn;
import me.alpha432.oyvey.event.impl.autototem.EventSetBlockState;
import me.alpha432.oyvey.event.impl.autototem.EventSync;
import me.alpha432.oyvey.event.impl.autototem.EventTick;
import me.alpha432.oyvey.event.impl.tntaura.EventPostSync;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Bind;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.manager.ModuleManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoCrystal extends Module {

    private static AutoCrystal INSTANCE = new AutoCrystal();
    /*   MAIN   */
    private  final Setting<Pages> page = this.register(new Setting<>("Page", Pages.Main));
    private final Setting<Timing> timing = this.register(new Setting<>("Timing", Timing.NORMAL, v -> page.getValue() == Pages.Main));
    private final Setting<TargetLogic> targetLogic = this.register(new Setting<>("TargetLogic", TargetLogic.Distance, v -> page.getValue() == Pages.Main));
    private final Setting<Float> targetRange = this.register(new Setting<>("TargetRange", 10.0f, 1.0f, 15f, v -> page.getValue() == Pages.Main));
    public  final Setting<Integer> selfPredictTicks = this.register(new Setting<>("SelfPredictTicks", 3, 0, 20, v -> page.getValue() == Pages.Main));
    private final Setting<OnBreakBlock> onBreakBlock = this.register(new Setting<>("OnBreakBlock", OnBreakBlock.Smart, v -> page.getValue() == Pages.Main));

    /*   PLACE   */
    private final Setting<Interact> interact = this.register(new Setting<>("Interact", Interact.Default, v -> page.getValue() == Pages.Place));
    private final Setting<Boolean> strictCenter = this.register(new Setting<>("StrictCenter", true, v -> page.getValue() == Pages.Place && interact.getValue() == Interact.Strict));
    private final Setting<Boolean> oldVer = this.register(new Setting<>("1.12", false, v -> page.getValue() == Pages.Place));
    private final Setting<Boolean> ccPlace = this.register(new Setting<>("CC", true, v -> page.getValue() == Pages.Place));
    private final Setting<Boolean> instantPlace = this.register(new Setting<>("InstantPlace", true, v -> page.getValue() == Pages.Place));
    private final Setting<Recalc> recalculate = this.register(new Setting<>("Recalc", Recalc.FAST, v -> page.getValue() == Pages.Place)).withParent(instantPlace);
    private final Setting<Integer> placeDelay = this.register(new Setting<>("PlaceDelay", 0, 0, 1000, v -> page.getValue() == Pages.Place));
    private final Setting<Float> placeRange = this.register(new Setting<>("PlaceRange", 5f, 1.0f, 6f, v -> page.getValue() == Pages.Place));
    private final Setting<Float> placeWallRange = this.register(new Setting<>("PlaceWallRange", 3.5f, 1.0f, 6f, v -> page.getValue() == Pages.Place));
    public  final Setting<Integer> predictTicks = this.register(new Setting<>("PredictTicks", 3, 0, 20, v -> page.getValue() == Pages.Place));

    /*   BREAK   */
    private final Setting<Integer> breakDelay = this.register(new Setting<>("BreakDelay", 0, 0, 1000, v -> page.getValue() == Pages.Break));
    private final Setting<Float> explodeRange = this.register(new Setting<>("BreakRange", 5.0f, 1.0f, 6f, v -> page.getValue() == Pages.Break));
    private final Setting<Float> explodeWallRange = this.register(new Setting<>("BreakWallRange", 3.5f, 1.0f, 6f, v -> page.getValue() == Pages.Break));
    private final Setting<Integer> crystalAge = this.register(new Setting<>("CrystalAge", 0, 0, 20, v -> page.getValue() == Pages.Break));

    /*   PAUSE   */
    private final Setting<Boolean> mining = this.register(new Setting<>("Mining", true, v -> page.getValue() == Pages.Pause));
    private final Setting<Boolean> eating = this.register(new Setting<>("Eating", true, v -> page.getValue() == Pages.Pause));
    private final Setting<Boolean> aura = this.register(new Setting<>("Aura", false, v -> page.getValue() == Pages.Pause));
    private final Setting<Boolean> pistonAura = this.register(new Setting<>("PistonAura", true, v -> page.getValue() == Pages.Pause));
    private final Setting<Boolean> surround = this.register(new Setting<>("Surround", true, v -> page.getValue() == Pages.Pause));
    private final Setting<Float> pauseHP = this.register(new Setting<>("HP", 8.0f, 2.0f, 10f, v -> page.getValue() == Pages.Pause));
    private final Setting<Boolean> switchPause = this.register(new Setting<>("SwitchPause", true, v -> page.getValue() == Pages.Pause));
    private final Setting<Integer> switchDelay = this.register(new Setting<>("SwitchDelay", 100, 0, 1000, v -> page.getValue() == Pages.Pause)).withParent(switchPause);

    /*   DAMAGES   */
    public final Setting<Sort> sort = this.register(new Setting<>("Sort", Sort.DAMAGE, v -> page.getValue() == Pages.Damages));
    public final Setting<Float> minDamage = this.register(new Setting<>("MinDamage", 6.0f, 2.0f, 20f, v -> page.getValue() == Pages.Damages));
    public final Setting<Float> maxSelfDamage = this.register(new Setting<>("MaxSelfDamage", 10.0f, 2.0f, 20f, v -> page.getValue() == Pages.Damages));
    private final Setting<Safety> safety = this.register(new Setting<>("Safety", Safety.NONE, v -> page.getValue() == Pages.Damages));
    private final Setting<Float> safetyBalance = this.register(new Setting<>("SafetyBalance", 1.1f, 0.1f, 3f, v -> page.getValue() == Pages.Damages && safety.getValue() == Safety.BALANCE));
    public final Setting<Boolean> protectFriends = this.register(new Setting<>("ProtectFriends", true, v -> page.getValue() == Pages.Damages));
    private final Setting<Boolean> overrideSelfDamage = this.register(new Setting<>("OverrideSelfDamage", true, v -> page.getValue() == Pages.Damages));
    private final Setting<Float> lethalMultiplier = this.register(new Setting<>("LethalMultiplier", 1.0f, 0.0f, 5f, v -> page.getValue() == Pages.Damages));
    private final Setting<Boolean> armorBreaker = this.register(new Setting<>("ArmorBreaker",true, v -> page.getValue() == Pages.Damages));
    private final Setting<Float> armorScale = this.register(new Setting<>("Armor %", 5.0f, 0.0f, 40f, v -> page.getValue() == Pages.Damages)).withParent(armorBreaker);
    private final Setting<Float> facePlaceHp = this.register(new Setting<>("FacePlaceHp", 5.0f, 2.0f, 20f, v -> page.getValue() == Pages.Damages));
    private final Setting<Boolean> facePlaceButton = this.register(new Setting<>("FacePlaceBtn",  true, v -> page.getValue() == Pages.Damages));

    /*   SWITCH   */
    private final Setting<Boolean> autoGapple = this.register(new Setting<>("AutoGapple", true, v -> page.getValue() == Pages.Switch));
    private final Setting<Switch> autoSwitch = this.register(new Setting<>("Switch", Switch.NORMAL, v -> page.getValue() == Pages.Switch));
    private final Setting<Switch> antiWeakness = this.register(new Setting<>("AntiWeakness", Switch.SILENT, v -> page.getValue() == Pages.Switch));

    /*   Remove   */
    private final Setting<Remove> remove = this.register(new Setting<>("Remove", Remove.Fake, v -> page.getValue() == Pages.Remove));
    private final Setting<Integer> removeDelay = this.register(new Setting<>("RemoveDelay", 0, 0, 200, v -> page.getValue() == Pages.Remove));

    /*   INFO   */
    private final Setting<Boolean> targetName = this.register(new Setting<>("TargetName", false, v -> page.getValue() == Pages.Info));
    private final Setting<Boolean> currentSide = this.register(new Setting<>("CurrentSide", true, v -> page.getValue() == Pages.Info));
    private final Setting<Boolean> speed = this.register(new Setting<>("Speed", true, v -> page.getValue() == Pages.Info));
    private final Setting<Boolean> confirmInfo = this.register(new Setting<>("ConfirmTime", true, v -> page.getValue() == Pages.Info));
    private final Setting<Boolean> calcInfo = this.register(new Setting<>("CalcInfo", false, v -> page.getValue() == Pages.Info));

    public static PlayerEntity target;
    private BlockHitResult bestPosition;
    private EndCrystalEntity bestCrystal;

    private final Timer placeTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private final Timer blockRecalcTimer = new Timer();

    // позиция и время постановки
    private final Map<BlockPos, Long> placedCrystals = new HashMap<>();

    private final DeadManager deadManager = new DeadManager();

    public float renderDamage, renderSelfDamage;

    private int prevCrystalsAmount, crystalSpeed, invTimer;

    private boolean rotated, tickBusy;

    private long confirmTime, calcTime;

    private BlockPos renderPos, prevRenderPos;
    private long renderMultiplier;
    private final Map<BlockPos, Long> renderPositions = new ConcurrentHashMap<>();

    public AutoCrystal() {
        super("AutoCrystal", "AutoCrystal", Category.COMBAT, true, false, false);
        this.setInstance();
    }

    public static AutoCrystal getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoCrystal();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        rotated = false;
        renderDamage = 0;
        renderSelfDamage = 0;
        placedCrystals.clear();
        deadManager.reset();
        breakTimer.reset();
        placeTimer.reset();
        bestCrystal = null;
        bestPosition = null;
        renderPos = null;
        prevRenderPos = null;
        target = null;
        renderMultiplier = 0;
        confirmTime = 0;
        renderPositions.clear();
    }

    @Override
    public void onDisable() {
        target = null;
    }

   // @EventHandler
   @Subscribe
    public void onSync(EventSync e) {
        if (mc.player == null || mc.world == null) return;

        switch (targetLogic.getValue()) {
            case HP -> target = OyVey.combatManager.getTargetByHealth(targetRange.getValue());
            case Distance -> target = OyVey.combatManager.getNearestTarget(targetRange.getValue());
            case FOV -> target = OyVey.combatManager.getTargetByFOV(targetRange.getValue());
        }

        if (target != null && (target.isDead() || target.getHealth() < 0)) {
            target = null;
            return;
        }

        // Right click gapple
        if (mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL && autoGapple.getValue()
                && mc.options.useKey.isPressed() && mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
            SearchInvResult result = InventoryUtility.findItemInHotBar(Items.ENCHANTED_GOLDEN_APPLE);
            result.switchTo();
        }


        // CA Speed counter
        if (invTimer++ >= 20) {
            crystalSpeed = MathUtility.clamp(prevCrystalsAmount - InventoryUtility.getItemCount(Items.END_CRYSTAL), 0, 255);
            prevCrystalsAmount = InventoryUtility.getItemCount(Items.END_CRYSTAL);
            invTimer = 0;
        }

    }

    @Override
    public String getDisplayInfo() {
        StringBuilder info = new StringBuilder();
        if (bestPosition != null) {
            if (targetName.getValue() && target != null) info.append(target.getName().getString()).append(" | ");
            if (speed.getValue()) info.append(crystalSpeed).append(" c/s").append(" | ");
            if (currentSide.getValue()) info.append(bestPosition.getSide().toString().toUpperCase()).append(" | ");
            if (confirmInfo.getValue()) info.append("c: ").append(confirmTime).append(" | ");
            if (calcInfo.getValue()) info.append("calc: ").append(calcTime).append(" | ");
        }
        return info.length() < 4 ? info.toString() : info.substring(0, info.length() - 3);
    }

    //@EventHandler
    @Subscribe
    public void onCrystalSpawn(@NotNull EventEntitySpawn e) {
        if (e.getEntity() instanceof EndCrystalEntity crystal && !placedCrystals.isEmpty()) {
            HashMap<BlockPos, Long> cache = new HashMap<>(placedCrystals);
            for (BlockPos bp : cache.keySet())
                if (crystal.squaredDistanceTo(bp.toCenterPos()) < 0.3 && timing.getValue() == Timing.NORMAL && breakTimer.passedMs(breakDelay.getValue())) {
                    confirmTime = System.currentTimeMillis() - cache.get(bp);
                    placedCrystals.remove(bp);
                    OyVey.asyncManager.run(() -> handleSpawn(crystal));
                }
        }
    }

    private void handleSpawn(EndCrystalEntity crystal) {
        if (mc.player == null || mc.world == null) return;

        getCrystalToExplode();
        if (bestCrystal == crystal) {
            attackCrystal(crystal);
            debug("end sequence");
            if (isEnabled() && instantPlace.getValue() && placeTimer.passedMs(placeDelay.getValue())) {
                debug("placing after attack");
                if (recalculate.getValue() != Recalc.OFF)
                    calcPosition(recalculate.getValue() == Recalc.FAST ? 2f : placeRange.getValue(), recalculate.getValue() == Recalc.FAST ? crystal.getPos() : mc.player.getPos());

                if (bestPosition != null)
                    placeCrystal(bestPosition);
            }
        }
    }

    //@EventHandler
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive e) {
        if (mc.player == null || mc.world == null) return;

        if (e.getPacket() instanceof ExplosionS2CPacket explosion) {
            for (Entity ent : Lists.newArrayList(mc.world.getEntities())) {
                if (ent instanceof EndCrystalEntity crystal
                        && crystal.squaredDistanceTo(explosion.getX(), explosion.getY(), explosion.getZ()) <= 144
                        && !deadManager.isDead(crystal)) {
                    debug("Removed " + crystal.getPos().toString() + " (due to explosion)");
                    deadManager.setDead(crystal, System.currentTimeMillis());
                }
            }
        }
    }

    //@EventHandler
    @Subscribe
    public void onBlockDestruct(EventSetBlockState e) {
        if (mc.player == null || mc.world == null) return;

        if (e.getPrevState() == null || e.getState() == null)
            return;
        if (target != null && target.squaredDistanceTo(e.getPos().toCenterPos()) <= 4 && e.getState().isAir() && !e.getPrevState().isAir() && blockRecalcTimer.every(200)) {
            debug("Detected change of state " + e.getPos() + ", recalculating...");
            OyVey.asyncManager.run(() -> calcPosition(recalculate.getValue() == Recalc.FAST ? 2f : placeRange.getValue(), recalculate.getValue() == Recalc.FAST ? e.getPos().toCenterPos() : mc.player.getPos()));
        }
    }


    //@EventHandler
    @Subscribe
    public void onPostSync(EventPostSync e) {
        if (bestPosition != null && placeTimer.passedMs(placeDelay.getValue()))
            placeCrystal(bestPosition);

        if (bestCrystal != null && breakTimer.passedMs(breakDelay.getValue()))
            attackCrystal(bestCrystal);
        tickBusy = false;
    }



    //@Override
    public void onRender3D(MatrixStack stack) {
        deadManager.update(remove.getValue() == Remove.ON, removeDelay.getValue());
    }

    private void renderBox(String dmg, Box box) {


    //    if (drawDamage.getValue());
    }

    public boolean shouldPause() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return true;

        boolean offhand = mc.player.getOffHandStack().getItem() instanceof EndCrystalItem;
        boolean mainHand = mc.player.getMainHandStack().getItem() instanceof EndCrystalItem;

        if (mc.interactionManager.isBreakingBlock() && !offhand && mining.getValue())
            return true;

        if(!offhand && !mainHand && autoSwitch.getValue() != Switch.SILENT && autoSwitch.getValue() != Switch.INVENTORY)
            return true;

        if (mc.player.isUsingItem() && eating.getValue() && !offhand)
            return true;

        if (tickBusy && timing.getValue() == Timing.SEQUENTIAL)
            return true;

        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() < pauseHP.getValue())
            return true;

        if (!offhand && autoGapple.getValue() && mc.options.useKey.isPressed() && mc.player.getMainHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE)
            return true;

        boolean silentWeakness = antiWeakness.getValue() == Switch.SILENT || antiWeakness.getValue() == Switch.INVENTORY;

        boolean silent = autoSwitch.getValue() == Switch.SILENT || autoSwitch.getValue() == Switch.INVENTORY;

        return isEnabled() && switchPause.getValue() && !OyVey.playerManager.switchTimer.passedMs(switchDelay.getValue()) && !silent && !silentWeakness;
    }


    public void attackCrystal(EndCrystalEntity crystal) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || crystal == null) return;

        if (shouldPause() || target == null)
            return;

        if (crystalAge.getValue() != 0 && crystal.age < crystalAge.getValue())
            return;

        StatusEffectInstance weaknessEffect = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
        StatusEffectInstance strengthEffect = mc.player.getStatusEffect(StatusEffects.STRENGTH);

        int prevSlot = -1;
        SearchInvResult antiWeaknessResult = InventoryUtility.getAntiWeaknessItem();
        SearchInvResult antiWeaknessResultInv = InventoryUtility.findInInventory(itemStack ->
                itemStack.getItem() instanceof SwordItem
                        || itemStack.getItem() instanceof PickaxeItem
                        || itemStack.getItem() instanceof AxeItem
                        || itemStack.getItem() instanceof ShovelItem);

        if (antiWeakness.getValue() != Switch.NONE)
            if (weaknessEffect != null && (strengthEffect == null || strengthEffect.getAmplifier() < weaknessEffect.getAmplifier()))
                prevSlot = switchTo(antiWeaknessResult, antiWeaknessResultInv, antiWeakness);

        sendPacket(PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
        sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

        tickBusy = true;
        breakTimer.reset();

        if (remove.getValue() != Remove.OFF)
            deadManager.setDead(crystal, System.currentTimeMillis());

        if (prevSlot != -1) {
            if (antiWeakness.getValue() == Switch.SILENT) {
                mc.player.getInventory().selectedSlot = prevSlot;
                sendPacket(new UpdateSelectedSlotC2SPacket(prevSlot));
            }
            if (antiWeakness.getValue() == Switch.INVENTORY) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, prevSlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
                sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
            }
        }
    }

    private int switchTo(SearchInvResult result, SearchInvResult resultInv, @NotNull Setting<Switch> antiWeakness) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return -1;

        int prevSlot = mc.player.getInventory().selectedSlot;

        if (antiWeakness.getValue() != Switch.INVENTORY) {
            result.switchTo();
        } else if (resultInv.found()) {
            prevSlot = resultInv.slot();
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, prevSlot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
            sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
        }

        return prevSlot;
    }

    public void placeCrystal(BlockHitResult bhr) {
        if (shouldPause() || mc.player == null) return;
        int prevSlot = -1;

        SearchInvResult crystalResult = InventoryUtility.findItemInHotBar(Items.END_CRYSTAL);
        SearchInvResult crystalResultInv = InventoryUtility.findItemInInventory(Items.END_CRYSTAL);

        boolean offhand = mc.player.getOffHandStack().getItem() instanceof EndCrystalItem;
        boolean holdingCrystal = mc.player.getMainHandStack().getItem() instanceof EndCrystalItem || offhand;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Box posBB = new Box(bhr.getBlockPos().up());

        if (!ccPlace.getValue())
            posBB = posBB.expand(0, 1f, 0);

        if (checkOtherEntities(posBB))
            return;

        if (autoSwitch.getValue() != Switch.NONE && !holdingCrystal)
            prevSlot = switchTo(crystalResult, crystalResultInv, autoSwitch);

        if (!(mc.player.getMainHandStack().getItem() instanceof EndCrystalItem || offhand))
            return;

        sendPacket(new PlayerInteractBlockC2SPacket(offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, bhr, PlayerUtility.getWorldActionId(mc.world)));
        mc.player.swingHand(offhand ? Hand.OFF_HAND : Hand.MAIN_HAND);
        placeTimer.reset();

        if (!bhr.getBlockPos().equals(renderPos)) {
            renderMultiplier = System.currentTimeMillis();
            prevRenderPos = renderPos;
            renderPos = bhr.getBlockPos();
        }

        if (!placedCrystals.containsKey(bhr.getBlockPos()))
            placedCrystals.put(bhr.getBlockPos(), System.currentTimeMillis());
        renderPositions.put(bhr.getBlockPos(), System.currentTimeMillis());
        tickBusy = true;

        postPlaceSwitch(prevSlot);
    }

    private boolean checkOtherEntities(Box posBoundingBox) {
        if (mc.player == null || mc.world == null) return false;

        Iterable<Entity> entities = Lists.newArrayList(mc.world.getEntities());

        for (Entity ent : entities) {
            if (ent == null) continue;
            if (ent.getBoundingBox().intersects(posBoundingBox)) {
                if (ent instanceof ExperienceOrbEntity)
                    continue;
                if (ent instanceof EndCrystalEntity cr && deadManager.isDead(cr))
                    continue;

                return true;
            }
        }
        return false;
    }

    private void postPlaceSwitch(int slot) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        if (autoSwitch.getValue() == Switch.SILENT && slot != -1) {
            mc.player.getInventory().selectedSlot = slot;
            sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }

        if (autoSwitch.getValue() == Switch.INVENTORY && slot != -1) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
            sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
        }
    }

    public void calcPosition(float range, Vec3d center) {
        if (mc.player == null || mc.world == null) return;

        if (target == null) {
            renderPos = null;
            prevRenderPos = null;
            bestPosition = null;
            return;
        }

        long currentTime = System.currentTimeMillis();
        List<PlaceData> list = getPossibleBlocks(target, center, range).stream().filter(data -> isSafe(data.damage, data.selfDamage, data.overrideDamage)).toList();
        bestPosition = list.isEmpty() ? null : filterPositions(list);
        calcTime = System.currentTimeMillis() - currentTime;
    }

    private @NotNull List<PlaceData> getPossibleBlocks(PlayerEntity target, Vec3d center, float range) {
        List<PlaceData> blocks = new ArrayList<>();
        BlockPos playerPos = BlockPos.ofFloored(center);

        for (int x = (int) Math.floor(playerPos.getX() - range); x <= Math.ceil(playerPos.getX() + range); x++) {
            for (int y = (int) Math.floor(playerPos.getY() - range); y <= Math.ceil(playerPos.getY() + range); y++) {
                for (int z = (int) Math.floor(playerPos.getZ() - range); z <= Math.ceil(playerPos.getZ() + range); z++) {
                    PlaceData data = getPlaceData(new BlockPos(x, y, z), target);
                    if (data != null)
                        blocks.add(data);
                }
            }
        }

        return blocks;
    }

    private @NotNull List<CrystalData> getPossibleCrystals(PlayerEntity target) {
        List<CrystalData> crystals = new ArrayList<>();

        if (mc.player == null || mc.world == null) return crystals;

        Iterable<Entity> entities = Lists.newArrayList(mc.world.getEntities());
        for (Entity ent : entities) {
            if (!(ent instanceof EndCrystalEntity cr))
                continue;

            if (deadManager.isDead(cr))
                continue;

            if (PlayerUtility.squaredDistanceFromEyes(ent.getPos()) > explodeRange.getPow2Value())
                continue;

            if (!InteractionUtility.canSee(ent) && PlayerUtility.squaredDistanceFromEyes(ent.getPos()) > explodeWallRange.getPow2Value())
                continue;

            if (!ent.isAlive())
                continue;

            float damage = ExplosionUtility.getExplosionDamage2(ent.getPos(), target);
            float selfDamage = ExplosionUtility.getSelfExplosionDamage(ent.getPos(), selfPredictTicks.getValue());

            boolean overrideDamage = shouldOverrideDamage(damage, selfDamage);

            if (protectFriends.getValue()) {
                List<PlayerEntity> players = Lists.newArrayList(mc.world.getPlayers());
                for (PlayerEntity pl : players) {
                    if (!OyVey.friendManager.isFriend(pl)) continue;
                    float fdamage = ExplosionUtility.getExplosionDamage2(ent.getPos(), pl);
                    if (fdamage > selfDamage) {
                        selfDamage = fdamage;
                    }
                }
            }

            if (damage < 1.5f) continue;
            if (selfDamage > maxSelfDamage.getValue() && !overrideDamage) continue;
            crystals.add(new CrystalData((EndCrystalEntity) ent, damage, selfDamage, overrideDamage));
        }
        return crystals;
    }

    public void getCrystalToExplode() {
        if (target == null)
            bestCrystal = null;

        List<CrystalData> list = getPossibleCrystals(target).stream().filter(data -> isSafe(data.damage, data.selfDamage, data.overrideDamage)).toList();
        bestCrystal = list.isEmpty() ? null : filterCrystals(list);
    }

    public boolean isSafe(float damage, float selfDamage, boolean overrideDamage) {
        if (mc.player == null || mc.world == null) return false;

        if (overrideDamage)
            return true;

        if (selfDamage + 0.5 > mc.player.getHealth() + mc.player.getAbsorptionAmount()) return false;
        else if (safety.getValue() == Safety.STABLE) return damage - selfDamage > 0;
        else if (safety.getValue() == Safety.BALANCE) return (damage * safetyBalance.getValue()) - selfDamage > 0;
        return true;
    }

    private @Nullable BlockHitResult filterPositions(@NotNull List<PlaceData> clearedList) {
        PlaceData bestData = null;
        float bestVal = 0f;
        for (PlaceData data : clearedList) {
            if ((shouldOverride(data.damage) || data.damage > minDamage.getValue())) {
                if (sort.getValue() == Sort.DAMAGE) {
                    if (bestVal < data.damage) {
                        bestData = data;
                        bestVal = data.damage;
                    }
                } else {
                    if (bestVal < data.damage / data.selfDamage) {
                        bestData = data;
                        bestVal = data.damage / data.selfDamage;
                    }
                }
            }
        }

        if (bestData == null) return null;
        renderDamage = bestData.damage;
        renderSelfDamage = bestData.selfDamage;
        return bestData.bhr;
    }

    public boolean shouldOverride(float damage) {
        if (target == null) return false;

        boolean override = target.getHealth() + target.getAbsorptionAmount() <= facePlaceHp.getValue();

        if (isEnabled() && armorBreaker.getValue())
            for (ItemStack armor : target.getArmorItems())
                if (armor != null && !armor.getItem().equals(Items.AIR) && ((armor.getMaxDamage() - armor.getDamage()) / (float) armor.getMaxDamage()) * 100 < armorScale.getValue()) {
                    override = true;
                    break;
                }

        if ( facePlaceButton.getValue());
            override = true;

        if ((target.getHealth() + target.getAbsorptionAmount()) - (damage * lethalMultiplier.getValue()) < 0.5)
            override = true;

        return override;
    }

    private @Nullable EndCrystalEntity filterCrystals(@NotNull List<CrystalData> clearedList) {
        CrystalData bestData = null;
        float bestVal = 0f;
        for (CrystalData data : clearedList) {
            if ((shouldOverride(data.damage) || data.damage > minDamage.getValue())) {
                if (sort.getValue() == Sort.DAMAGE) {
                    if (bestVal < data.damage) {
                        bestData = data;
                        bestVal = data.damage;
                    }
                } else {
                    if (bestVal < data.damage / data.selfDamage) {
                        bestData = data;
                        bestVal = data.damage / data.selfDamage;
                    }
                }
            }
        }

        if (bestData == null) return null;
        renderDamage = bestData.damage;
        renderSelfDamage = bestData.selfDamage;
        return bestData.crystal;
    }

    public @Nullable PlaceData getPlaceData(BlockPos bp, PlayerEntity target) {
        if (mc.player == null || mc.world == null) return null;


        Block base = mc.world.getBlockState(bp).getBlock();
        boolean freeSpace = mc.world.isAir(bp.up());
        boolean legacyFreeSpace = mc.world.isAir(bp.up().up());

        if (base != Blocks.OBSIDIAN && base != Blocks.BEDROCK)
            return null;

        if (!(freeSpace && (!oldVer.getValue() || legacyFreeSpace)))
            return null;

        if (checkEntities(bp)) return null;

        Vec3d crystalVec = new Vec3d(0.5f + bp.getX(), 1f + bp.getY(), 0.5f + bp.getZ());

        float damage = target == null ? 10f : ExplosionUtility.getExplosionDamage2(crystalVec, target);
        float selfDamage = ExplosionUtility.getSelfExplosionDamage(crystalVec, selfPredictTicks.getValue());
        boolean overrideDamage = shouldOverrideDamage(damage, selfDamage);

        if (protectFriends.getValue()) {
            List<PlayerEntity> players = Lists.newArrayList(mc.world.getPlayers());
            for (PlayerEntity pl : players) {
                if (!OyVey.friendManager.isFriend(pl)) continue;
                float fdamage = ExplosionUtility.getExplosionDamage2(crystalVec, pl);
                if (fdamage > selfDamage) {
                    selfDamage = fdamage;
                }
            }
        }

        if (damage < 1.5f) return null;
        if (selfDamage > maxSelfDamage.getValue() && !overrideDamage) return null;

        BlockHitResult interactResult = getInteractResult(bp, crystalVec);
        if (interactResult == null) return null;

        return new PlaceData(interactResult, damage, selfDamage, overrideDamage);
    }

    public BlockHitResult getInteractResult(BlockPos bp, Vec3d crystalVec) {
        BlockHitResult interactResult = null;
        switch (interact.getValue()) {
            case Default -> interactResult = getDefaultInteract(crystalVec, bp);
            case Strict -> interactResult = getStrictInteract(bp);
            case Legit -> interactResult = getLegitInteract(bp);
        }
        return interactResult;
    }

    public boolean checkEntities(@NotNull BlockPos base) {
        if (mc.player == null || mc.world == null) return false;


        Box posBoundingBox = new Box(base.up());

        if (!ccPlace.getValue())
            posBoundingBox = posBoundingBox.expand(0, 1f, 0);

        Iterable<Entity> entities = Lists.newArrayList(mc.world.getEntities());
        for (Entity ent : entities) {
            if (ent == null) continue;
            if (ent.getBoundingBox().intersects(posBoundingBox)) {
                if (ent instanceof ExperienceOrbEntity)
                    continue;
                if (ent instanceof EndCrystalEntity) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public boolean shouldOverrideDamage(float damage, float selfDamage) {
        if (overrideSelfDamage.getValue() && target != null) {
            if (mc.player == null || mc.world == null) return false;

            boolean targetSafe = (target.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING
                    || target.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING);

            boolean playerSafe = (mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING
                    || mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING);

            float targetHp = target.getHealth() + target.getAbsorptionAmount() - 1f;

            float playerHp = mc.player.getHealth() + mc.player.getAbsorptionAmount() - 1f;

            boolean canPop = damage > targetHp && targetSafe;

            boolean canKill = damage > targetHp && !targetSafe;

            boolean canPopSelf = selfDamage > playerHp && playerSafe;

            boolean canKillSelf = selfDamage > playerHp && !playerSafe;

            if (canPopSelf && canKill)
                return true;

            return selfDamage > maxSelfDamage.getValue() && (canPop || canKill) && !canKillSelf && !canPopSelf;
        }
        return false;
    }

    private @Nullable BlockHitResult getDefaultInteract(Vec3d crystalVector, BlockPos bp) {
        if (mc.player == null || mc.world == null) return null;

        if (PlayerUtility.squaredDistanceFromEyes(crystalVector) > placeRange.getPow2Value())
            return null;

        BlockHitResult wallCheck = mc.world.raycast(new RaycastContext(InteractionUtility.getEyesPos(mc.player), crystalVector, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));

        if (wallCheck != null && wallCheck.getType() == HitResult.Type.BLOCK && wallCheck.getBlockPos() != bp)
            if (PlayerUtility.squaredDistanceFromEyes(crystalVector) > placeWallRange.getPow2Value())
                return null;

        return new BlockHitResult(crystalVector, mc.world.isInBuildLimit(bp.up()) ? Direction.UP : Direction.DOWN, bp, false);
    }

    public BlockHitResult getStrictInteract(@NotNull BlockPos bp) {
        if (mc.player == null || mc.world == null) return null;

        float bestDistance = Float.MAX_VALUE;
        Direction bestDirection = null;
        Vec3d bestVector = null;

        float upPoint = strictCenter.getValue() ? (float) bp.toCenterPos().getY() : bp.up().getY();

        if (mc.player.getEyePos().getY() > upPoint) {
            bestDirection = Direction.UP;
            bestVector = new Vec3d(bp.getX() + 0.5, bp.getY() + 1, bp.getZ() + 0.5);
        } else if (mc.player.getEyePos().getY() < bp.getY() && mc.world.isAir(bp.down())) {
            bestDirection = Direction.DOWN;
            bestVector = new Vec3d(bp.getX() + 0.5, ccPlace.getValue() ? bp.getY() + 1 : bp.getY(), bp.getZ() + 0.5);
        } else {
            for (Direction dir : Direction.values()) {
                if (dir == Direction.UP || dir == Direction.DOWN)
                    continue;

                Vec3d directionVec = new Vec3d(bp.getX() + 0.5 + dir.getVector().getX() * 0.5, bp.getY() + 0.9, bp.getZ() + 0.5 + dir.getVector().getZ() * 0.5);

                if (!mc.world.isAir(bp.offset(dir)))
                    continue;

                float distance = PlayerUtility.squaredDistanceFromEyes(directionVec);
                if (bestDistance > distance) {
                    bestDirection = dir;
                    bestVector = directionVec;
                    bestDistance = distance;
                }
            }
        }

        if (bestVector == null) return null;

        if (PlayerUtility.squaredDistanceFromEyes(bestVector) > placeRange.getPow2Value())
            return null;

        BlockHitResult wallCheck = mc.world.raycast(new RaycastContext(InteractionUtility.getEyesPos(mc.player), bestVector, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));

        if (wallCheck != null && wallCheck.getType() == HitResult.Type.BLOCK && wallCheck.getBlockPos() != bp)
            if (PlayerUtility.squaredDistanceFromEyes(bestVector) > placeWallRange.getPow2Value())
                return null;

        return new BlockHitResult(bestVector, bestDirection, bp, false);
    }

    public BlockHitResult getLegitInteract(BlockPos bp) {
        if (mc.player == null || mc.world == null) return null;

        float bestDistance = Float.MAX_VALUE;
        BlockHitResult bestResult = null;
        for (float x = 0f; x <= 1f; x += 0.2f) {
            for (float y = 0f; y <= 1f; y += 0.2f) {
                for (float z = 0f; z <= 1f; z += 0.2f) {
                    Vec3d point = new Vec3d(bp.getX() + x, bp.getY() + y, bp.getZ() + z);
                    float distance = PlayerUtility.squaredDistanceFromEyes(point);

                    BlockHitResult wallCheck = mc.world.raycast(new RaycastContext(InteractionUtility.getEyesPos(mc.player), point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
                    if (wallCheck != null && wallCheck.getType() == HitResult.Type.BLOCK && wallCheck.getBlockPos() != bp)
                        if (distance > placeWallRange.getPow2Value())
                            continue;


                    BlockHitResult result = ExplosionUtility.rayCastBlock(new RaycastContext(InteractionUtility.getEyesPos(mc.player), point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player), bp);
                    if (distance > placeRange.getPow2Value())
                        continue;

                    if (distance < bestDistance) {
                        if (result != null && result.getType() == HitResult.Type.BLOCK) {
                            bestResult = result;
                            bestDistance = distance;
                        }
                    }
                }
            }
        }
        return bestResult;
    }

    @Subscribe //////////////////Test
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null) return;

        OyVey.asyncManager.run(() -> {
            calcPosition(placeRange.getValue(), mc.player.getPos());
            getCrystalToExplode();
        });
    }

    public record PlaceData(BlockHitResult bhr, float damage, float selfDamage, boolean overrideDamage) {
    }

    private record CrystalData(EndCrystalEntity crystal, float damage, float selfDamage, boolean overrideDamage) {
    }

    private enum Pages {
        Place, Break, Pause, Render, Damages, Main, Switch, Remove, Info
    }

    private enum Switch {
        NONE, NORMAL, SILENT, INVENTORY
    }

    private enum Timing {
        NORMAL, SEQUENTIAL
    }

    private enum Interact {
        Default, Strict, Legit
    }

    private enum TargetLogic {
        Distance, HP, FOV
    }

    public enum Safety {
        BALANCE, STABLE, NONE
    }

    public enum Sort {
        SAFE, DAMAGE
    }

    public enum Render {
        Fade, Slide, Default
    }

    public enum OnBreakBlock {
        PlaceOn, Smart, None
    }

    public enum Remove {
        OFF, Fake, ON
    }

    public enum Recalc {
        OFF, FAST, SLOW
    }
}
