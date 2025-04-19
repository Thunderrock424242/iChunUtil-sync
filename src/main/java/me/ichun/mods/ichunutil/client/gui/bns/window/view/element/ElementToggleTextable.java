package me.ichun.mods.ichunutil.client.gui.bns.window.view.element;

import me.ichun.mods.ichunutil.client.gui.bns.window.view.View;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class ElementToggleTextable extends ElementToggle
{
    public @Nonnull String offString;
    public @Nonnull String onString;

    public ElementToggleTextable(@Nonnull View parent, @Nonnull String s)
    {
        this(parent, s, I18n.format("gui.no"), I18n.format("gui.yes"));
    }

    public ElementToggleTextable(@Nonnull View parent, @Nonnull String s, @Nonnull String off, @Nonnull String on)
    {
        super(parent, s);
        this.tooltip = s;
        this.offString = off;
        this.onString = on;
    }

    @Override
    public void onClickRelease()
    {
        super.onClickRelease();
        text = toggleState ? onString : offString;
    }
}
