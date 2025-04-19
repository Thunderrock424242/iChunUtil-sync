package me.ichun.mods.ichunutil.client.gui.bns.window.view.element;

import me.ichun.mods.ichunutil.client.gui.bns.window.Fragment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class ElementClickable<M extends Fragment> extends Element<M> //we reset our focus when we're clicked.
{
    public @Nonnull Consumer<ElementClickable> callback;
    public boolean hover; //for rendering

    public ElementClickable(@Nonnull M parent, Consumer<ElementClickable> callback)
    {
        super(parent);
        this.callback = callback;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick)
    {
        hover = isMouseOver(mouseX, mouseY) || parentFragment.getFocused() == this;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        super.mouseReleased(mouseX, mouseY, button); // unsets dragging;
        parentFragment.setFocused(null); //we're a one time click, stop focusing on us
        if(isMouseOver(mouseX, mouseY) && button == 0) //lmb
        {
            trigger();
        }
        return getFocused() != null && getFocused().mouseReleased(mouseX, mouseY, button);
    }

    private void trigger()
    {
        if(renderMinecraftStyle())
        {
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
        onClickRelease();
        callback.accept(this);
    }

    public abstract void onClickRelease();

    @Override
    public boolean keyPressed(int key, int scancode, int listener)
    {
        if(key == GLFW.GLFW_KEY_SPACE || key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER)
        {
            trigger();
            return true;
        }
        return false;
    }

    @Override
    public int getMinecraftFontColour()
    {
        return parentFragment.isDragging() && parentFragment.getFocused() == this ? 10526880 : hover ? 16777120 : 14737632;
    }
}
