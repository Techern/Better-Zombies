package org.techern.betterzombies.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
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
            }
        }
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zombie mob
     *
     * @param event The {@link PlayerInteractEvent}
     * @param mob the {@link Zombie} mob
     * @since 0.1
     */
    protected static <T extends Zombie> void playerInteractsWithZombie(PlayerInteractEvent.EntityInteractSpecific event, T mob) {
        event.getEntity().sendSystemMessage(Component.literal("You tried to cure a " + mob.getName().getString() + " that isn't a villager"));
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zombie horse
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZombieHorse(PlayerInteractEvent.EntityInteractSpecific event) {
        event.getEntity().sendSystemMessage(Component.literal("that's a horse"));
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob hat is a zombie villager
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZombieVillager(PlayerInteractEvent.EntityInteractSpecific event) {
        event.getEntity().sendSystemMessage(Component.literal("YES!"));
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a mob that is a zoglin mob
     *
     * @param event The {@link PlayerInteractEvent}
     * @since 0.1
     */
    protected static void playerInteractsWithZombiePiglin(PlayerInteractEvent.EntityInteractSpecific event) {
        event.getEntity().sendSystemMessage(Component.literal("*pig noises*"));
    }
}
