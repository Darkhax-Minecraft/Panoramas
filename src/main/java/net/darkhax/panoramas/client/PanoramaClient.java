package net.darkhax.panoramas.client;

import net.darkhax.panoramas.PanoramasMod;
import net.darkhax.panoramas.client.api.IPanorama;
import net.darkhax.panoramas.client.api.PanoramaAPI;
import net.darkhax.panoramas.client.panorama.PanoramaLoader;
import net.darkhax.panoramas.client.panorama.PanoramaVanilla;
import net.darkhax.panoramas.client.screen.preview.ScreenPanoramaSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

import javax.annotation.Nullable;

public class PanoramaClient {

    /**
     * Holds the last title screen that a panorama was rendered on. This is updated when a new main menu is initialized.
     * This is tracked to prevent the panorama from changing by going in and out of te same menu instance.
     */
    @Nullable
    private MainMenuScreen lastTitleMenu;

    /**
     * The panorama that has been selected for display.
     */
    @Nullable
    private IPanorama selectedPanorama;

    /**
     * The time the custom panorama was initialized and set. This is used to track if a new panorama should be
     * initialized after a set duration of time.
     */
    private long panoramaDisplayTime;

    public PanoramaClient() {

        // Register the vanilla style panorama serializer.
        PanoramaAPI.registerSerializer(PanoramaVanilla.TYPE_ID, PanoramaVanilla.SERIALIZER);

        // Register panorama entries with the resource pack system.
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(PanoramaLoader.INSTANCE);

        // Register the config on the client.
        ModLoadingContext.get().registerConfig(Type.CLIENT, Configuration.CFG.getSpec());

        // Set the config button on Forge's mod list to display the panorama preview selector.
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ScreenPanoramaSelector::factory);

        // Adds listeners that initialize panoramas and refresh them.
        MinecraftForge.EVENT_BUS.addListener(this::onGuiInit);
        MinecraftForge.EVENT_BUS.addListener(this::onGuiDraw);
    }

    private void setPanorama(MainMenuScreen screen) {

        // If a forced panorama ID has been set try to set it to that.
        final ResourceLocation forcedId = Configuration.CFG.getForced();

        if (forcedId != null) {

            final IPanorama forcedPanorama = PanoramaAPI.getPanoramaById(forcedId);

            if (forcedPanorama == null) {

                PanoramasMod.LOGGER.error("Failed to force Panoramam '{}'. This is not a known ID! Check your config!", forcedId);
                this.selectedPanorama = PanoramaAPI.getRandomPanorama();
            }

            else {

                this.selectedPanorama = forcedPanorama;
            }
        }

        // Otherwise set it to be a random panorama.
        else {

            this.selectedPanorama = PanoramaAPI.getRandomPanorama();
        }

        PanoramasMod.LOGGER.debug("Panorama set to {}.", this.selectedPanorama.getId());
        this.panoramaDisplayTime = System.currentTimeMillis();
        screen.panorama = this.selectedPanorama.getRenderer();
    }

    private void onGuiInit(InitGuiEvent.Post event) {

        if (event.getGui() instanceof MainMenuScreen) {

            if (this.selectedPanorama == null || this.lastTitleMenu == null || this.lastTitleMenu != event.getGui()) {

                this.setPanorama((MainMenuScreen) event.getGui());
            }

            ((MainMenuScreen) event.getGui()).panorama = this.selectedPanorama.getRenderer();
            this.lastTitleMenu = (MainMenuScreen) event.getGui();
        }
    }

    private void onGuiDraw(DrawScreenEvent.Pre event) {

        if (event.getGui() instanceof MainMenuScreen) {

            final int maxDisplayTime = Configuration.CFG.getMaxDisplayTime();

            if (maxDisplayTime != -1 && (System.currentTimeMillis() - this.panoramaDisplayTime) >= maxDisplayTime) {

                this.setPanorama((MainMenuScreen) event.getGui());
            }
        }
    }
}
