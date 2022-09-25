package net.darkhax.panoramas.client.panorama;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.panoramas.PanoramasMod;
import net.darkhax.panoramas.client.api.IPanorama;
import net.darkhax.panoramas.client.api.IPanoramaSerializer;
import net.darkhax.panoramas.client.api.PanoramaAPI;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.util.ResourceLocation;

/**
 * A vanilla-like panorama type.
 */
public class PanoramaVanilla implements IPanorama {

    /**
     * The serializer type ID for vanilla-like panoramas.
     */
    public static final ResourceLocation TYPE_ID = new ResourceLocation(PanoramasMod.MOD_ID, "vanilla");

    /**
     * Mojang's vanilla panorama.
     */
    public static final PanoramaVanilla VANILLA_PANORAMA = new PanoramaVanilla(PanoramaAPI.DEFAULT_ID, 1000, new ResourceLocation("textures/gui/title/background/panorama"), new RenderSkybox(MainMenuScreen.CUBE_MAP));

    /**
     * A missing / fallback panorama for error situations.
     */
    public static final PanoramaVanilla MISSING_PANORAMA = new PanoramaVanilla(PanoramaAPI.MISSING_ID, -1, new ResourceLocation("textures/gui/title/background/missingno"));

    /**
     * The serializer instance for this type of panorama.
     */
    public static final Serializer SERIALIZER = new Serializer();

    /**
     * The ID of the panorama.
     */
    private final ResourceLocation id;

    /**
     * The display weight of the panorama.
     */
    private final int weight;

    /**
     * The texture to render. This uses Mojang's 6 face cubemap style manorama.
     */
    private final ResourceLocation texture;

    /**
     * The renderer instance for the panorama. This is just a default box renderer.
     */
    private final RenderSkybox renderer;

    public PanoramaVanilla(ResourceLocation id, int weight, ResourceLocation texture) {

        this(id, weight, texture, new RenderSkybox(new RenderSkyboxCube(texture)));
    }

    public PanoramaVanilla(ResourceLocation id, int weight, ResourceLocation texture, RenderSkybox renderer) {

        this.id = id;
        this.weight = weight;
        this.texture = texture;
        this.renderer = renderer;
    }

    @Override
    public ResourceLocation getId() {

        return this.id;
    }

    @Override
    public RenderSkybox getRenderer() {

        return this.renderer;
    }

    @Override
    public int getWeight() {

        return this.weight;
    }

    /**
     * Serializer for vanilla-like panoramas.
     */
    private static final class Serializer implements IPanoramaSerializer<PanoramaVanilla> {

        @Override
        public PanoramaVanilla read(ResourceLocation id, JsonObject json) {

            final ResourceLocation texture = Serializers.RESOURCE_LOCATION.read(json, "texture", new ResourceLocation(id.getNamespace(), "textures/gui/title/background/" + id.getPath()));
            final int weight = Serializers.INT.read(json, "weight", 1000);
            return new PanoramaVanilla(id, weight, texture);
        }

        @Override
        public JsonElement write(PanoramaVanilla toWrite) {

            final JsonObject json = new JsonObject();
            json.add("type", Serializers.RESOURCE_LOCATION.write(TYPE_ID));
            json.add("texture", Serializers.RESOURCE_LOCATION.write(toWrite.texture));
            json.add("weight", Serializers.INT.write(toWrite.weight));

            return json;
        }
    }
}