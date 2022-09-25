package net.darkhax.panoramas.client;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.panoramas.PanoramasMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import javax.annotation.Nullable;

public class Configuration {
    
    public static final Configuration CFG = new Configuration();
    
    private final ForgeConfigSpec spec;

    private final ConfigValue<String> forcedPanorama;
    private final ConfigValue<List<? extends String>> removedEntries;
    private final ConfigValue<List<? extends String>> removedNamespaces;
    private final ConfigValue<Integer> maxDisplayTime;
    
    private Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Forces a specific panorama to load using it's ID.");
        this.forcedPanorama = builder.define("forcedPanorama", "");

        builder.comment("A list of panorama ids to remove from the panorama pool.");
        this.removedEntries = builder.defineList("removedEntries", new ArrayList<String>(), s -> ResourceLocation.isValidResourceLocation((String) s));
        
        builder.comment("A list of panorama namespaces to remove from the panorama pool. Adding a name here will remove all ids from that namespace.");
        this.removedNamespaces = builder.defineList("removedNamespaces", new ArrayList<String>(), s -> ResourceLocation.isValidResourceLocation((String) s));
        
        builder.comment("The maximum time in ms to display a panorama. After this time a new one will be selected. Setting to -1 will disable this timer.");
        this.maxDisplayTime = builder.defineInRange("maxDisplayTime", 600000, -1, Integer.MAX_VALUE);
        
        this.spec = builder.build();
    }
    
    public ForgeConfigSpec getSpec () {
        
        return this.spec;
    }

    @Nullable
    public ResourceLocation getForced() {

        final String configValue = this.forcedPanorama.get();

        if (configValue != null && !configValue.isEmpty()) {

            try {

                return new ResourceLocation(configValue);
            }

            catch (ResourceLocationException e) {

                PanoramasMod.LOGGER.error("Can not process ID from '{}'. The format is invalid! This is a user error, check your config!", configValue);
                PanoramasMod.LOGGER.catching(e);
            }
        }

        return null;
    }

    public int getMaxDisplayTime () {
        
        return this.maxDisplayTime.get();
    }
    
    public boolean canDisplay (ResourceLocation id) {
        
        return !this.removedNamespaces.get().contains(id.getNamespace()) && !this.removedEntries.get().contains(id.toString());
    }
}