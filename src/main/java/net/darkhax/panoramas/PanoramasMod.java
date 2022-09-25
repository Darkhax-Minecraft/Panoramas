package net.darkhax.panoramas;

import net.darkhax.panoramas.client.PanoramaClient;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PanoramasMod.MOD_ID)
public class PanoramasMod {

    public static final String MOD_ID = "panoramas";
    public static final Logger LOGGER = LogManager.getLogger("Panoramas");

    public PanoramasMod() {

        // Tell clients they can join a server if they don't have the mod.
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        if (FMLEnvironment.dist.isClient()) {

            new PanoramaClient();
        }
    }
}