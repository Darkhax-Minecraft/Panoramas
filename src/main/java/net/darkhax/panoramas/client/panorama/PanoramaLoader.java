package net.darkhax.panoramas.client.panorama;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.panoramas.PanoramasMod;
import net.darkhax.panoramas.client.api.IPanorama;
import net.darkhax.panoramas.client.api.IPanoramaSerializer;
import net.darkhax.panoramas.client.api.PanoramaAPI;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A resource reload listener that will read panorama entries from the resource pack system.
 */
public class PanoramaLoader extends ReloadListener<Map<ResourceLocation, IPanorama>> {

    /**
     * A constant instance to the panorama loader.
     */
    public static final PanoramaLoader INSTANCE = new PanoramaLoader();

    @Override
    protected void apply(Map<ResourceLocation, IPanorama> data, IResourceManager resources, IProfiler profiler) {

        // Called after panoramas have been loaded.
        PanoramaAPI.updatePanoramas(data);
    }

    @Override
    protected Map<ResourceLocation, IPanorama> prepare(IResourceManager resources, IProfiler profiler) {

        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        final Map<ResourceLocation, IPanorama> map = Maps.newHashMap();

        PanoramasMod.LOGGER.debug("Loading panoramas.");

        // Always register the vanilla panorama.
        map.put(PanoramaAPI.DEFAULT_ID, PanoramaVanilla.VANILLA_PANORAMA);

        // Find panorama entries from resource packs and attempt to read them.
        for (final ResourceLocation candidate : resources.listResources("panoramas", n -> n.endsWith(".json"))) {

            final String path = candidate.getPath();
            final ResourceLocation entryId = new ResourceLocation(candidate.getNamespace(), path.substring("panoramas/".length(), path.length() - ".json".length()));

            try {

                for (final IResource resource : resources.getResources(candidate)) {

                    try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));) {

                        final JsonObject json = gson.fromJson(reader, JsonObject.class);

                        // Uses Forge's recipe condition system to skip loading panoramas if their
                        // load conditions are not met. Forge's system is not explicitly designed
                        // to work at this stage so some condition types may not work as expected.
                        // Only the "is mod loaded" condition is recommended.
                        if (!CraftingHelper.processConditions(json, "conditions")) {

                            PanoramasMod.LOGGER.debug("Skipping panorama {} from {} as one or more of it's conditions were not met.", entryId, candidate);
                        }

                        else {

                            // Lookup the serializer to use. If this is not specified it will use the
                            // default vanilla-like panorama serializer.
                            final ResourceLocation serializerId = Serializers.RESOURCE_LOCATION.read(json, "type", PanoramaVanilla.TYPE_ID);
                            final IPanoramaSerializer<?> serializer = PanoramaAPI.getSerializer(serializerId);

                            if (serializer != null) {

                                final IPanorama panorama = serializer.read(entryId, json);

                                if (panorama != null) {

                                    map.put(entryId, panorama);
                                }

                                else {

                                    PanoramasMod.LOGGER.debug("Skipping panorama {} from {}. Serializer {} returned null.", entryId, candidate, serializerId);
                                }
                            }

                            else {

                                PanoramasMod.LOGGER.error("Could not read panorama {} from {}. Serializer {} does not exist!", entryId, candidate, serializerId);
                            }
                        }
                    }
                }
            }

            catch (final Exception e) {

                PanoramasMod.LOGGER.error("Unable to read panorama of {} from {}.", entryId, candidate, e);
            }
        }

        return map;
    }
}