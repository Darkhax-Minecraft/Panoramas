package net.darkhax.panoramas.client.api;

import net.darkhax.panoramas.PanoramasMod;
import net.darkhax.panoramas.client.Configuration;
import net.darkhax.panoramas.client.panorama.PanoramaVanilla;
import net.darkhax.panoramas.util.WeightedSelector;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PanoramaAPI {

    /**
     * The ID for the default vanilla panorama.
     */
    public static final ResourceLocation DEFAULT_ID = new ResourceLocation("minecraft", "default");

    /**
     * The ID for the missing/error panorama.
     */
    public static final ResourceLocation MISSING_ID = new ResourceLocation(PanoramasMod.MOD_ID, "missing");

    /**
     * A map containing all known serializers. New serializers can be registered by using
     * {@link #registerSerializer(net.minecraft.util.ResourceLocation, IPanoramaSerializer)}.
     */
    private static final Map<ResourceLocation, IPanoramaSerializer<?>> SERIALIZERS = new HashMap<>();

    /**
     * An internal map of all the currently loaded serializers.
     */
    private static Map<ResourceLocation, IPanorama> PANORAMAS = new HashMap<>();

    /**
     * Gets a panorama serializer using it's namespaced ID.
     *
     * @param id The namespaced ID of the panorama serializer.
     * @return The serializer registered to the specified ID. If the ID is not known a null value will be returned.
     */
    @Nullable
    public static IPanoramaSerializer<?> getSerializer(ResourceLocation id) {

        return SERIALIZERS.get(id);
    }

    /**
     * Registers a new panorama serializer.
     *
     * @param id         The ID of the serializer type.
     * @param serializer The serializer to register.
     */
    public static void registerSerializer(ResourceLocation id, IPanoramaSerializer<?> serializer) {

        if (SERIALIZERS.containsKey(id)) {

            PanoramasMod.LOGGER.warn("Replacing panorama serializer {} of type {} with {}.", id, SERIALIZERS.get(id).getClass(), serializer.getClass());
        }

        SERIALIZERS.put(id, serializer);
    }

    /**
     * Updates the internal map with new data. This is generally reserved for resource pack reload listener and should
     * not be used by third parties to add new panoramas.
     *
     * @param data The new panorama data.
     */
    public static void updatePanoramas(Map<ResourceLocation, IPanorama> data) {

        PanoramasMod.LOGGER.info("Read {} panoramas.", data.size());
        PANORAMAS = data;
    }

    /**
     * Selects a random panorama from the set of available panoramas.
     *
     * @return A random panorama to display.
     */
    public static IPanorama getRandomPanorama() {

        final WeightedSelector<IPanorama> selector = new WeightedSelector<>(IPanorama::getWeight);
        PANORAMAS.values().stream().filter(p -> Configuration.CFG.canDisplay(p.getId())).forEach(selector::add);
        return selector.isEmpty() ? PanoramaVanilla.MISSING_PANORAMA : selector.get();
    }

    /**
     * Retrieves an unmodifiable list of all loaded panorama entries.
     *
     * @return An unmodifiable list of all loaded panorama entries.
     */
    public static Collection<IPanorama> getPanoramas() {

        return PANORAMAS.values();
    }

    /**
     * Attempts to retrieve a panorama entry using its namespaced ID.
     *
     * @param id The ID of the panorama to find.
     * @return The panorama that was found. If the ID is not linked to anything this will be null.
     */
    @Nullable
    public static IPanorama getPanoramaById(ResourceLocation id) {

        return PANORAMAS.get(id);
    }
}