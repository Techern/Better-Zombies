package org.techern.betterzombies.events;

import com.mojang.brigadier.LiteralMessage;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.techern.betterzombies.ZombieType;

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
    public static void playerInteractsWithAnything(PlayerInteractEvent.EntityInteractSpecific event){
        event.getEntity().sendSystemMessage(Component.literal("Hello, you clicked on a " + event.getTarget().getName().getString() + " with a " + event.getEntity().getItemInHand(event.getHand()).getDisplayName().getString()));
    }

    /**
     * Only called whenever a {@link PlayerInteractEvent.EntityInteractSpecific} is for a {@link ZombieType}
     *
     * @param event The {@link PlayerInteractEvent}
     * @param zombieType The type of zombie
     * @since 0.1
     */
    protected static void playerInteractsWithZombie(PlayerInteractEvent.EntityInteractSpecific event, ZombieType zombieType) {

    }
}
