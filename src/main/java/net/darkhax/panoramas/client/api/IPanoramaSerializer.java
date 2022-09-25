package net.darkhax.panoramas.client.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

/**
 * A serializer that can produce new types of panorama entries from JSON data. Third parties can use these to create new
 * types of panoramas with features that are not supported by the default implementation.
 *
 * @param <T> The type of panorama produced by the serializer.
 */
public interface IPanoramaSerializer<T extends IPanorama> {

    /**
     * Reads a panorama from JSON data. Please note that the conditions parameter is reserved by the panorama loading
     * system for load conditions.
     *
     * @param id   The namespaced identifier assigned to the panorama being created. This is typically derived from a
     *             resource pack entry path.
     * @param json The raw JSON data to be read.
     * @return The new panorama that was read from the provided data. Returning a null value will safely skip over that
     * entry and log a warning in the logs.
     */
    T read(ResourceLocation id, JsonObject json);

    /**
     * Writes the panorama to JSON data. The result must be readable by {@link #read(ResourceLocation, JsonObject)} from
     * the same serializer.
     *
     * @param toWrite The panorama to be written.
     * @return The written JSON data.
     */
    JsonElement write(T toWrite);
}