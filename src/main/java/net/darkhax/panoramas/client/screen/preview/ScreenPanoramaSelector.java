package net.darkhax.panoramas.client.screen.preview;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A panorama preview list that allows you to view all the available panoramas. This screen is accessed using the Forge
 * mod list config button.
 */
@OnlyIn(Dist.CLIENT)
public class ScreenPanoramaSelector extends Screen {

    private static final ITextComponent FULLSCREEN = new TranslationTextComponent("screen.panoramas.preview.fullscreen");

    private PanoramaList availableOptions;
    private final Screen lastScreen;

    public ScreenPanoramaSelector(Screen lastScreen) {

        super(new TranslationTextComponent("screen.panoramas.preview.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {

        this.availableOptions = new PanoramaList(this, this.minecraft);
        this.children.add(this.availableOptions);
        this.addButton(new Button(this.width / 2 - 155, this.height - 38, 150, 20, DialogTexts.GUI_DONE, btn -> this.minecraft.setScreen(this.lastScreen)));
        this.addButton(new Button(this.width / 2 - 155 + 160, this.height - 38, 150, 20, FULLSCREEN, btn -> this.minecraft.setScreen(new ScreenPanoramaPreview(this, this.availableOptions.getSelected().getPanorama()))));
        super.init();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

        this.availableOptions.render(stack, mouseX, mouseY, partialTicks);
        drawCenteredString(stack, this.font, this.title, this.width / 2, 12, 0xffffff);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {

        this.minecraft.setScreen(this.lastScreen);
    }

    protected FontRenderer getFont() {

        return this.font;
    }

    public static Screen factory(Minecraft mc, Screen parent) {

        return new ScreenPanoramaSelector(parent);
    }
}