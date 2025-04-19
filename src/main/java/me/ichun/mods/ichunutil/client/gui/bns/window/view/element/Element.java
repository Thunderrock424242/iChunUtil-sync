package me.ichun.mods.ichunutil.client.gui.bns.window.view.element;

import me.ichun.mods.ichunutil.client.gui.bns.window.Fragment;
import me.ichun.mods.ichunutil.client.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Element<M extends Fragment> extends Fragment //TODO handle narration?
{
    public final static List<Element> INFERTILE = Collections.emptyList();

    public String tooltip;

    public Element(@Nonnull M parent) //TODO Make aligned element, which counts the gaps between elements and equally distributes widht/height to each
    {
        super(parent);
    }

    public <T extends Element> T setPos(int x, int y)
    {
        posX = x;
        posY = y;
        return (T)this;
    }

    public <T extends Element> T setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
        return (T)this;
    }

    public <T extends Element> T setTooltip(String s)
    {
        tooltip = s;
        return (T)this;
    }

    @Override
    public @Nullable String tooltip(double mouseX, double mouseY)
    {
        return tooltip;
    }

    @Override
    public void init()
    {
        constraint.apply();
    }

    @Override
    public List<? extends IGuiEventListener> children()
    {
        return INFERTILE;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick){}

    @Override
    public void resize(Minecraft mc, int width, int height)
    {
        constraint.apply();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(isMouseOver(mouseX, mouseY))
        {
            return true;
        }
        return false;
    }

    /**
     * This is here to bypass our mouseClicked and use Fragment's
     */
    public boolean defaultMouseClicked(double mouseX, double mouseY, int button)
    {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean changeFocus(boolean direction)
    {
        return parentFragment.getFocused() != this; //focus on us if we're not focused
    }

    public enum ButtonState
    {
        IDLE,
        HOVER,
        CLICK
    }

    public static void renderMinecraftStyleButton(int posX, int posY, int width, int height, ButtonState state) // BUTTONS NEED TO BE LARGER THAN 3x3
    {
        Fragment.bindTexture(Fragment.VANILLA_WIDGETS);

        int yOffset = state == ButtonState.CLICK ? 0 : state == ButtonState.HOVER ? 2 : 1;
        if(height == 20 && width > 15)
        {
            //gg easy life
            int i = width - 28;
            int x = posX + 14;
            while(i > 0)
            {
                int dist = Math.min(i, 172);
                RenderHelper.draw(x, posY, dist, 20, 0, 14D / 256D, (14 + dist) / 256D, (46 + yOffset * 20) / 256D, (66 + yOffset * 20) / 256D); //draw body
                i -= dist;
                x += dist;
            }


            RenderHelper.draw(posX, posY, 14, 20, 0, 0D/256D, 14D/256D, (46 + yOffset * 20)/256D, (66 + yOffset * 20)/256D); //draw leftblock
            RenderHelper.draw(posX + width - 14, posY, 14, 20, 0, 186D/256D, 200D/256D, (46 + yOffset * 20)/256D, (66 + yOffset * 20)/256D); //draw leftblock
        }
        else if(height < 20 && width < 200) //default button length
        {
            RenderHelper.draw(posX, posY + height - (height - 3), (width - 3), (height - 3), 0, 0D/256D, (width - 3)/256D, ((66 - (height - 3)) + yOffset * 20)/256D, (66 + yOffset * 20)/256D); //draw bottomLeft
            RenderHelper.draw(posX, posY, (width - 3), (height - 3), 0, 0D/256D, (width - 3)/256D, (46 + yOffset * 20)/256D, (46 + (height - 3) + yOffset * 20)/256D); //draw topLeft
            RenderHelper.draw(posX + width - (width - 3), posY, (width - 3), (height - 3), 0, (200D - (width - 3))/256D, 200D/256D, (46 + yOffset * 20)/256D, (46 + (height - 3) + yOffset * 20)/256D); //draw topRight
            RenderHelper.draw(posX + width - (width - 3), posY + height - (height - 3), (width - 3), (height - 3), 0, (200 - (width - 3))/256D, 200D/256D, (66 - (height - 3) + yOffset * 20)/256D, (66 + yOffset * 20)/256D); //draw topRight
        }
        else //big bois
        {
            cropAndStitch(posX, posY, width, height, 4, 0D, 46 + (yOffset * 20), 200, 20, 256, 256);
        }
    }

    public static void cropAndStitch(int posX, int posY, int width, int height, int borderSize, double u, double v, int uLength, int vLength, double texWidth, double texHeight)
    {
        int ii = width - (borderSize * 2);
        int xx = posX + borderSize;
        while(ii > 0)
        {
            int jj = height - (borderSize * 2);
            int yy = posY + borderSize;
            int distx = Math.min(ii, uLength - (borderSize * 2));
            while(jj > 0)
            {
                int disty = Math.min(jj, vLength - (borderSize * 2));
                RenderHelper.draw(xx, yy, distx, disty, 0, (u + borderSize)/texWidth, ((u + borderSize) + distx)/texWidth, (v + borderSize)/texWidth, ((v + borderSize) + disty)/texWidth); //draw body
                jj -= disty;
                yy += disty;
            }
            ii -= distx;
            xx += distx;
        }


        int i = width - (borderSize * 2);
        int x = posX + borderSize;
        while(i > 0)
        {
            int dist = Math.min(i, uLength - (borderSize * 2));
            RenderHelper.draw(x, posY, dist, borderSize, 0, (u + borderSize)/texWidth, ((u + borderSize) + dist)/texWidth, v/texHeight, (v + borderSize)/texHeight); //draw top bar
            RenderHelper.draw(x, posY + height - borderSize, dist, borderSize, 0, (u + borderSize)/texWidth, ((u + borderSize) + dist)/texWidth, (v + vLength - borderSize)/texHeight, (v + vLength)/texHeight); //draw bottom bar
            i -= dist;
            x += dist;
        }

        i = height - (borderSize * 2);
        x = posY + borderSize;
        while(i > 0)
        {
            int dist = Math.min(i, vLength - (borderSize * 2));
            RenderHelper.draw(posX, x, borderSize, dist, 0, u/texWidth, (u + borderSize)/texWidth, (v + borderSize)/texWidth, ((v + borderSize) + dist)/texWidth); //draw left bar
            RenderHelper.draw(posX + width - borderSize, x, borderSize, dist, 0, (u + uLength - borderSize)/texWidth, (u + uLength)/texWidth, (v + borderSize)/texWidth, ((v + borderSize) + dist)/texWidth); //draw left bar
            i -= dist;
            x += dist;
        }

        RenderHelper.draw(posX, posY + height - borderSize, borderSize, borderSize, 0, u/texWidth, (u + borderSize)/texWidth, (v + vLength - borderSize)/texHeight, (v + vLength)/texHeight); //draw bottomLeft
        RenderHelper.draw(posX, posY, borderSize, borderSize, 0, u/texWidth, (u + borderSize)/texWidth, v/texHeight, (v + borderSize)/texHeight); //draw topLeft
        RenderHelper.draw(posX + width - borderSize, posY, borderSize, borderSize, 0, (u + uLength - borderSize)/texWidth, (u + uLength)/texWidth, v/texHeight, (v + borderSize)/texHeight); //draw topRight
        RenderHelper.draw(posX + width - borderSize, posY + height - borderSize, borderSize, borderSize, 0, (u + uLength - borderSize)/texWidth, (u + uLength)/texWidth, (v + vLength - borderSize)/texHeight, (v + vLength)/texHeight); //draw bottomRight
    }

    public class MousePos
    {
        int x;
        int y;

        public MousePos(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
