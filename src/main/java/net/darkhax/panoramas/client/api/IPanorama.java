package net.darkhax.panoramas.client.api;

import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public interface IPanorama {

    /**
     * Gets the namespaced ID of the panorama. This should be a unique ID that can be used to lookup the panorama in the
     * registry.
     *
     * @return The unique namespaced ID of the panorama.
     */
    ResourceLocation getId();

    /**
     * Gets the renderer for the panorama. Most of the time this will be a simple skybox renderer however it is also
     * perfectly valid to create another render type using the available parameters.
     *
     * @return The renderer for the panorama.
     */
    RenderSkybox getRenderer();

    /**
     * Gets the random weight of the panorama. This determines how often this panorama will be randomly selected, with
     * higher weights appearing more often.
     *
     * @return The random weight of the panorama.
     */
    int getWeight();

    /**
     * Gets the name of the panorama. The default implementation derives a localization key from the namespaced ID of
     * the panorama. This uses the panorama.namespace.path.title format.
     *
     * @return The name of the panorama.
     */
    default IFormattableTextComponent getName() {

        final String translationKey = getLocalizationKeyFromId(this.getId(), "title");
        return I18n.exists(translationKey) ? new TranslationTextComponent(translationKey) : new StringTextComponent(this.getId().toString());
    }

    /**
     * Gets a description of the panorama. The default implementation derives a localization key from the namespaced ID
     * of the panorama. This uses the panorama.namespace.path.desc format.
     *
     * @return The description of the panorama.
     */
    @Nullable
    default IFormattableTextComponent getDescription() {

        final String translationKey = getLocalizationKeyFromId(this.getId(), "desc");
        return I18n.exists(translationKey) ? new TranslationTextComponent(translationKey) : null;
    }

    /**
     * Gets the name of the author who created the panorama. The default implementation derives a localization key from
     * the namespaced ID of the panorama. This uses the panorama.namespace.path.author format.
     *
     * @return The author of the panorama.
     */
    @Nullable
    default IFormattableTextComponent getAuthor() {

        final String translationKey = getLocalizationKeyFromId(this.getId(), "author");
        return I18n.exists(translationKey) ? new TranslationTextComponent(translationKey) : null;
    }

    /**
     * Creates a localization key from a namespaced ID. For internal/panorama use only.
     *
     * @param id   The ID to create a localization key from.
     * @param type A suffix to append to the end of the key.
     * @return The fully combined key.
     */
    static String getLocalizationKeyFromId(ResourceLocation id, String type) {

        return "panorama." + id.getNamespace() + "." + id.getPath() + "." + type;
    }
}