package me.ichun.mods.ichunutil.client.gui.bns.window.view.element;

import me.ichun.mods.ichunutil.client.gui.bns.window.Fragment;
import me.ichun.mods.ichunutil.client.gui.bns.window.view.View;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ElementButton extends ElementClickable<Fragment>
{
    public @Nonnull String text; //already localized

    public ElementButton(@Nonnull View parent, String s)
    {
        this(parent, s, (button) -> {});
    }

    public ElementButton(@Nonnull View parent, String s, Consumer<ElementClickable> callback)
    {
        super(parent, callback);
        text = s;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        super.render(mouseX, mouseY, partialTick);
        if(renderMinecraftStyle())
        {
            renderMinecraftStyleButton(getLeft(), getTop(), width, height, parentFragment.isDragging() && parentFragment.getFocused() == this ? ButtonState.CLICK : hover ? ButtonState.HOVER : ButtonState.IDLE);
        }
        else
        {
            fill(getTheme().elementButtonBorder, 0);
            int[] colour = parentFragment.isDragging() && parentFragment.getFocused() == this ? getTheme().elementButtonClick : hover ? getTheme().elementButtonBackgroundHover : getTheme().elementButtonBackgroundInactive;
            fill(colour, 1);
        }
        if(!text.isEmpty())
        {
            String s = reString(text, width - 4);
            if(s.equals(text))
            {
                setTooltip(null);
            }
            else
            {
                setTooltip(text);
            }
            drawString(s, getLeft() + (this.width - getFontRenderer().getStringWidth(s)) / 2F, getTop() + (height - getFontRenderer().FONT_HEIGHT) / 2F);
        }
    }

    @Override
    public void onClickRelease() {} //we don't do anything, we're a static button

    @Override
    public int getMinWidth()
    {
        return 15;
    }

    @Override
    public int getMinHeight()
    {
        return 16;
    }
}
