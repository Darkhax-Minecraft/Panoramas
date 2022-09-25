package net.darkhax.panoramas.client.screen.preview;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.darkhax.panoramas.client.api.IPanorama;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class PanoramaEntry extends ExtendedList.AbstractListEntry<PanoramaEntry> {
    
    private final ScreenPanoramaSelector owningScreen;
    private final PanoramaList owningList;
    private final IPanorama panorama;
    
    public PanoramaEntry(ScreenPanoramaSelector owningScreen, PanoramaList owningList, IPanorama panorama) {
        
        this.owningScreen = owningScreen;
        this.owningList = owningList;
        this.panorama = panorama;
    }
    
    @Override
    public void render (MatrixStack pMatrixStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTicks) {
        
        final FontRenderer font = this.owningScreen.getFont();
        final ITextComponent name = this.panorama.getName();

        font.drawShadow(pMatrixStack, name, this.owningList.getWidth() / 2 - font.width(name) / 2, pTop + 3, pIsMouseOver ? 0xffbbbbbb : 0xffffffff);
    }
    
    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int buttonId) {
        
        if (buttonId == 0) {
            
            this.owningList.setSelected(this);
            return true;
        }
        
        return false;
    }
    
    public IPanorama getPanorama () {
        
        return this.panorama;
    }
    
    public void renderMouseOver (MatrixStack matrix, int mouseX, int mouseY) {
        
        final List<ITextComponent> tooltip = new ArrayList<>();
        
        final ITextComponent author = this.panorama.getAuthor();
        
        tooltip.add(author != null ? new TranslationTextComponent("screen.panoramas.preview.entry.title", this.panorama.getName(), author) : this.panorama.getName());
        
        final IFormattableTextComponent desc = this.panorama.getDescription();
        
        if (desc != null) {
            
            tooltip.add(desc.withStyle(TextFormatting.GRAY));
        }
        
        tooltip.add(new TranslationTextComponent("screen.panoramas.preview.entry.id", this.panorama.getId().toString()).withStyle(TextFormatting.DARK_GRAY));
        tooltip.add(new TranslationTextComponent("screen.panoramas.preview.entry.weight", this.panorama.getWeight()).withStyle(TextFormatting.DARK_GRAY));
        
        this.owningScreen.renderComponentTooltip(matrix, tooltip, mouseX, mouseY);
    }
}