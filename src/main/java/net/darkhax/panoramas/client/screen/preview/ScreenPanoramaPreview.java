package net.darkhax.panoramas.client.screen.preview;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darkhax.panoramas.client.api.IPanorama;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A screen that will render a panorama without any other UI elements. Click Esc to close.
 */
@OnlyIn(Dist.CLIENT)
public class ScreenPanoramaPreview extends Screen {

    private final Screen lastScreen;
    private final IPanorama panorama;

    public ScreenPanoramaPreview(Screen lastScreen, IPanorama panorama) {

        super(new TranslationTextComponent("screen.panoramas.selector.title"));
        this.lastScreen = lastScreen;
        this.panorama = panorama;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

        this.panorama.getRenderer().render(partialTicks, 1f);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {

        this.minecraft.setScreen(this.lastScreen);
    }
}