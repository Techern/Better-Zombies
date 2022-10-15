package org.techern.betterzombies.events;

import joptsimple.internal.Reflection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles all {@link PlayerInteractEvent.EntityInteractSpecific}s and filters out zombie-related events to {@code #playerInteractsWithZombie}
 *
 * @since 0.1
 */
@Mod.EventBusSubscriber(modid = "betterzombies", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InteractWithZombiesEventHandler {

    /**
     * Called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is called
     *
     * @param event the {@link PlayerInteractEvent.EntityInteractSpecific}
     * @since 0.1
     */
    @SubscribeEvent
    public static void playerInteractsWithAnything(PlayerInteractEvent.EntityInteractSpecific event) {

        //We only need to process this in the server side
        if (event.getSide().isClient()) {
            return;
        }

        //TODO #5 allow conversion with enchanted apple
        if (event.getEntity().getItemInHand(event.getHand()).getItem().equals(Items.GOLDEN_APPLE)) {

            EntityType<?> type = event.getTarget().getType();
            if (type.equals(EntityType.ZOMBIE)) {
                playerInteractsWithZombie(event, (Zombie) event.getTarget());
            } else if (type.equals(EntityType.HUSK)) {
                playerInteractsWithZombie(event, (Husk) event.getTarget());
            } else if (type.equals(EntityType.DROWNED)) {
                playerInteractsWithZombie(event, (Drowned) event.getTarget());
            } else if (EntityType.ZOMBIE_HORSE.equals(type)) {
                playerInteractsWithZombieHorse(event);
            } else if (EntityType.ZOMBIE_VILLAGER.equals((type))) {
                playerInteractsWithZombieVillager(event);
            } else if (EntityType.ZOMBIFIED_PIGLIN.equals((type))) {
                playerInteractsWithZombiePiglin(event);
            } else if (EntityType.ZOGLIN.equals((type))) {
                playerInteractsWithZoglin(event);
            }
        }
    }

    /**
     * Checks to see if the {@link Mob} can be converted according to current game rules
     *
     * @param mobToConvert the {@link Mob} to convert
     * @return {@code true} if it can be converted, otherwise {@code false}
     * @since 0.1
     */
    private static <T extends Mob> boolean meetsConversionRequirements(T mobToConvert) {
        //TODO #5 allow conversion with no buff
        if (mobToConvert.hasEffect(MobEffects.WEAKNESS)) return true;

        return false;
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zombie mob
     *
     * @param event The {@link PlayerInteractEvent}
     * @param mob the {@link Zombie} mob
     * @since 0.1
     */
    protected static <T extends Zombie> void playerInteractsWithZombie(PlayerInteractEvent.EntityInteractSpecific event, T mob) {
        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20*60*5, 4));
        mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20*60*5, 30));
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zombie horse
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZombieHorse(PlayerInteractEvent.EntityInteractSpecific event) {
        ZombieHorse zombieHorse = (ZombieHorse) event.getTarget();
        ServerLevel level = (ServerLevel) event.getLevel();
        if (meetsConversionRequirements(zombieHorse)) {
            Horse horse = zombieHorse.convertTo(EntityType.HORSE, true);

            horse.finalizeSpawn(level, level.getCurrentDifficultyAt(horse.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);
            horse.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            level.levelEvent((Player)null, 1027, horse.blockPosition(), 0);
            event.getEntity().getItemInHand(event.getHand()).shrink(1);
        }
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob hat is a zombie villager
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZombieVillager(PlayerInteractEvent.EntityInteractSpecific event) {
        ZombieVillager zombieVillager = (ZombieVillager) event.getTarget();
        if (meetsConversionRequirements(zombieVillager)) {
            zombieVillager.finishConversion((ServerLevel) event.getLevel());
            event.getEntity().getItemInHand(event.getHand()).shrink(1);
        }
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zombie piglin mob
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZombiePiglin(PlayerInteractEvent.EntityInteractSpecific event) {
        ZombifiedPiglin zombifiedPiglin = (ZombifiedPiglin) event.getTarget();
        ServerLevel level = (ServerLevel) event.getLevel();

        if (meetsConversionRequirements(zombifiedPiglin)) {
            Piglin piglin = zombifiedPiglin.convertTo(EntityType.PIGLIN, true);

            piglin.finalizeSpawn(level, level.getCurrentDifficultyAt(piglin.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);
            piglin.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            piglin.setImmuneToZombification(true);
            level.levelEvent((Player)null, 1027, piglin.blockPosition(), 0);
            event.getEntity().getItemInHand(event.getHand()).shrink(1);
        }
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zoglin mob
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZoglin(PlayerInteractEvent.EntityInteractSpecific event) {
        Zoglin zoglin = (Zoglin) event.getTarget();
        ServerLevel level = (ServerLevel) event.getLevel();

        if (meetsConversionRequirements(zoglin)) {
            Hoglin pig = zoglin.convertTo(EntityType.HOGLIN, false);

            pig.finalizeSpawn(level, level.getCurrentDifficultyAt(pig.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData)null, (CompoundTag)null);
            pig.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            pig.setImmuneToZombification(true);
            level.levelEvent((Player)null, 1027, pig.blockPosition(), 0);
            event.getEntity().getItemInHand(event.getHand()).shrink(1);
        }
    }
}
