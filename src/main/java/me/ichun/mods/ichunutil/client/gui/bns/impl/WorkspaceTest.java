package me.ichun.mods.ichunutil.client.gui.bns.impl;

import me.ichun.mods.ichunutil.client.gui.bns.Workspace;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class WorkspaceTest extends Workspace
{
    public WorkspaceTest(ITextComponent title)
    {
        super(title, Screen.hasControlDown());
        addWindow(new WindowTest(this));
    }
}
