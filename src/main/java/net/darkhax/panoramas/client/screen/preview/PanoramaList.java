package net.darkhax.panoramas.client.screen.preview;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.darkhax.panoramas.client.api.IPanorama;
import net.darkhax.panoramas.client.api.PanoramaAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;

public class PanoramaList extends ExtendedList<PanoramaEntry> {
    
    private final ScreenPanoramaSelector parentScreen;
    
    public PanoramaList(ScreenPanoramaSelector parentScreen, Minecraft mc) {
        
        super(mc, parentScreen.width, parentScreen.height, 32, parentScreen.height - 65 + 4, 18);
        
        this.parentScreen = parentScreen;
        
        if (!PanoramaAPI.getPanoramas().isEmpty()) {
            
            for (final IPanorama panorama : PanoramaAPI.getPanoramas()) {
                
                this.addEntry(new PanoramaEntry(parentScreen, this, panorama));
            }
            
            this.setSelected(this.getEntry(0));
        }
    }
    
    @Override
    public void render (MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        
        this.setRenderBackground(this.getSelected() == null);
        this.setRenderTopAndBottom(this.getSelected() == null);
        
        if (this.getSelected() != null) {
            
            this.getSelected().getPanorama().getRenderer().render(partialTicks, 1f);
        }
        
        super.render(stack, mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected int getScrollbarPosition () {
        
        return super.getScrollbarPosition() + 20;
    }
    
    @Override
    public int getRowWidth () {
        
        return super.getRowWidth() + 50;
    }
    
    @Override
    protected boolean isFocused () {
        
        return this.parentScreen.getFocused() == this;
    }
    
    @Override
    protected void renderDecorations (MatrixStack matrix, int mouseX, int mouseY) {
        
        if (this.isMouseOver(mouseX, mouseY)) {
            
            for (int i = 0; i < this.getItemCount(); i++) {
                
                final PanoramaEntry entry = this.getEntry(i);
                
                if (entry.isMouseOver(mouseX, mouseY)) {
                    
                    entry.renderMouseOver(matrix, mouseX, mouseY);
                }
            }
        }
    }
}