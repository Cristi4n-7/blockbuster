package mchorse.blockbuster.client.gui;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.ClientProxy;
import mchorse.blockbuster.recording.RecordRecorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Recording GUI overlay
 *
 * This class is responsible for rendering red circle (like the icon that
 * represents recording in progress) and name of the recording file.
 */
@SideOnly(Side.CLIENT)
public class GuiRecordingOverlay extends Gui
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Blockbuster.MOD_ID, "textures/gui/recording.png");

    protected Minecraft mc;
    protected String caption;
    protected boolean isVisible = false;
    protected boolean recording = false;

    public GuiRecordingOverlay(Minecraft mc)
    {
        this.mc = mc;
    }

    /* Public API */

    public void setCaption(String caption, boolean recording)
    {
        this.caption = recording ? I18n.format("blockbuster.recording", caption) : caption;
        this.recording = recording;
    }

    public void setVisible(boolean isVisible)
    {
        this.isVisible = isVisible;
    }

    /* Rendering code */

    /**
     * Draw recording overlay if the recording in the process in top-left corner
     * of the screen.
     *
     * Thanks to coolAlias and to his tutorial github repo for this rendering
     * code.
     */
    public void draw(int width, int height)
    {
        if (!this.isVisible)
        {
            return;
        }

        FontRenderer font = this.mc.fontRenderer;
        String caption = this.caption;

        if (this.recording)
        {
            RecordRecorder recorder = ClientProxy.manager.recorders.get(Minecraft.getMinecraft().player);

            if (recorder != null)
            {
                caption += "\u00A7r (\u00A7l" + (recorder.tick + recorder.offset) + "\u00A7r)";
            }
        }

        this.mc.renderEngine.bindTexture(TEXTURE);

        GlStateManager.pushAttrib();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(4, 4, 0, 0, 16, 16);
        font.drawStringWithShadow(caption, 22, 8, 0xffffffff);

        GlStateManager.popAttrib();
    }
}