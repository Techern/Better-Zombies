package org.techern.betterzombies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(BetterZombies.MODID)
public class BetterZombies
{
    public static final String MODID = "betterzombies";

    public BetterZombies()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
