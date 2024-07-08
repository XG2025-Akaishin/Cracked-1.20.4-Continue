package me.alpha432.oyvey.features.modules.combat.autototem.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;
import me.alpha432.oyvey.mixin.autototem.IExplosion;

import java.util.Objects;

import static me.alpha432.oyvey.features.modules.Module.mc;

public final class ExplosionUtility {

    public static boolean terrainIgnore = false;
    public static BlockPos anchorIgnore = null;

    public static Explosion explosion;








    public static float getSelfExplosionDamage(Vec3d explosionPos, int predictTicks) {
        if (predictTicks == 0)
            return getExplosionDamage1(explosionPos, mc.player);
        else
            return getExplosionDamageWPredict(explosionPos, mc.player, PredictUtility.predictPlayer(mc.player, predictTicks));
    }

    public static float getExplosionDamage1(Vec3d explosionPos, PlayerEntity target) {
        try {
            if (mc.world.getDifficulty() == Difficulty.PEACEFUL)
                return 0f;

            if (explosion == null) {
                explosion = new Explosion(mc.world, mc.player, 1f, 33f, 7f, 6f, false, Explosion.DestructionType.DESTROY);
            }

            ((IExplosion) explosion).setX(explosionPos.x);
            ((IExplosion) explosion).setY(explosionPos.y);
            ((IExplosion) explosion).setZ(explosionPos.z);

            if (((IExplosion) explosion).getWorld() != mc.world)
                ((IExplosion) explosion).setWorld(mc.world);

            double maxDist = 12;
            if (!new Box(MathHelper.floor(explosionPos.x - maxDist - 1.0), MathHelper.floor(explosionPos.y - maxDist - 1.0), MathHelper.floor(explosionPos.z - maxDist - 1.0), MathHelper.floor(explosionPos.x + maxDist + 1.0), MathHelper.floor(explosionPos.y + maxDist + 1.0), MathHelper.floor(explosionPos.z + maxDist + 1.0)).intersects(target.getBoundingBox())) {
                return 0f;
            }

            if (!target.isImmuneToExplosion(explosion) && !target.isInvulnerable()) {
                double distExposure = (float) target.squaredDistanceTo(explosionPos) / 144;
                if (distExposure <= 1.0) {
                    double xDiff = target.getX() - explosionPos.x;
                    double yDiff = target.getY() - explosionPos.y;
                    double zDiff = target.getZ() - explosionPos.z;
                    double diff = MathHelper.sqrt((float) (xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
                    if (diff != 0.0) {
                        terrainIgnore = true;
                        double exposure = Explosion.getExposure(explosionPos, target);
                        terrainIgnore = false;
                        double finalExposure = (1.0 - distExposure) * exposure;

                        float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * maxDist + 1.0);

                        if (mc.world.getDifficulty() == Difficulty.EASY) {
                            toDamage = Math.min(toDamage / 2f + 1f, toDamage);
                        } else if (mc.world.getDifficulty() == Difficulty.HARD) {
                            toDamage = toDamage * 3f / 2f;
                        }

                        toDamage = DamageUtil.getDamageLeft(toDamage, target.getArmor(), (float) target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());

                        if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
                            int resistance = 25 - (target.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
                            float resistance_1 = toDamage * resistance;
                            toDamage = Math.max(resistance_1 / 25f, 0f);
                        }

                        if (toDamage <= 0f) toDamage = 0f;
                        else {
                            int protAmount = EnchantmentHelper.getProtectionAmount(target.getArmorItems(), mc.world.getDamageSources().explosion(explosion));
                            if (protAmount > 0) {
                                toDamage = DamageUtil.getInflictedDamage(toDamage, protAmount);
                            }
                        }
                        return toDamage;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return 0f;
    }

    public static float getExplosionDamageWPredict(Vec3d explosionPos, PlayerEntity target, PlayerEntity predict) {
        if (mc.world.getDifficulty() == Difficulty.PEACEFUL)
            return 0f;

        if (explosion == null) {
            explosion = new Explosion(mc.world, mc.player, 1f, 33f, 7f, 6f, false, Explosion.DestructionType.DESTROY);
        }

        ((IExplosion) explosion).setX(explosionPos.x);
        ((IExplosion) explosion).setY(explosionPos.y);
        ((IExplosion) explosion).setZ(explosionPos.z);

        if (((IExplosion) explosion).getWorld() != mc.world)
            ((IExplosion) explosion).setWorld(mc.world);

        if (!new Box(
                MathHelper.floor(explosionPos.x - 11d),
                MathHelper.floor(explosionPos.y - 11d),
                MathHelper.floor(explosionPos.z - 11d),
                MathHelper.floor(explosionPos.x + 13d),
                MathHelper.floor(explosionPos.y + 13d),
                MathHelper.floor(explosionPos.z + 13d)).intersects(predict.getBoundingBox())
        ) {
            return 0f;
        }

        if (!target.isImmuneToExplosion(explosion) && !target.isInvulnerable()) {
            double distExposure = MathHelper.sqrt((float) predict.squaredDistanceTo(explosionPos)) / 12d;
            if (distExposure <= 1.0) {
                double xDiff = predict.getX() - explosionPos.x;
                double yDiff = predict.getY() - explosionPos.y;
                double zDiff = predict.getZ() - explosionPos.z;
                double diff = MathHelper.sqrt((float) (xDiff * xDiff + yDiff * yDiff + zDiff * zDiff));
                if (diff != 0.0) {
                    terrainIgnore = true;
                    double exposure = Explosion.getExposure(explosionPos, predict);
                    terrainIgnore = false;
                    double finalExposure = (1.0 - distExposure) * exposure;

                    float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12d + 1.0);

                    if (mc.world.getDifficulty() == Difficulty.EASY) {
                        toDamage = Math.min(toDamage / 2f + 1f, toDamage);
                    } else if (mc.world.getDifficulty() == Difficulty.HARD) {
                        toDamage = toDamage * 3f / 2f;
                    }

                    toDamage = DamageUtil.getDamageLeft(toDamage, target.getArmor(), (float) Objects.requireNonNull(target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).getValue());

                    if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
                        int resistance = 25 - (Objects.requireNonNull(target.getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1) * 5;
                        float resistance_1 = toDamage * resistance;
                        toDamage = Math.max(resistance_1 / 25f, 0f);
                    }

                    if (toDamage <= 0f) {
                        toDamage = 0f;
                    } else {
                        int protAmount = EnchantmentHelper.getProtectionAmount(target.getArmorItems(), mc.world.getDamageSources().explosion(explosion));
                        if (protAmount > 0) {
                            toDamage = DamageUtil.getInflictedDamage(toDamage, protAmount);
                        }
                    }
                    return toDamage;
                }
            }
        }
        return 0f;
    }

}