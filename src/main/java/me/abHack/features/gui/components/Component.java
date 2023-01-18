/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundEvent
 *  org.lwjgl.opengl.GL11
 */
package me.abHack.features.gui.components;

import java.awt.Color;
import java.util.ArrayList;
import me.abHack.OyVey;
import me.abHack.features.Feature;
import me.abHack.features.gui.OyVeyGui;
import me.abHack.features.gui.components.items.Item;
import me.abHack.features.gui.components.items.buttons.Button;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.util.ColorUtil;
import me.abHack.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

public class Component
extends Feature {
    public static int[] counter1 = new int[]{1};
    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final ArrayList<Item> items = new ArrayList();
    private final int barHeight;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private int angle;
    private boolean hidden = false;
    private int startcolor;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.width = 88;
        this.height = 18;
        this.barHeight = 15;
        this.angle = 180;
        this.open = open;
        this.setupItems();
    }

    public static void drawModalRect(int var0, int var1, float var2, float var3, int var4, int var5, int var6, int var7, float var8, float var9) {
        Gui.drawScaledCustomSizeModalRect((int)var0, (int)var1, (float)var2, (float)var3, (int)var4, (int)var5, (int)var6, (int)var7, (float)var8, (float)var9);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static float calculateRotation(float var0) {
        float f = 0;
        var0 %= 360.0f;
        if (f >= 180.0f) {
            var0 -= 360.0f;
        }
        if (var0 < -180.0f) {
            var0 += 360.0f;
        }
        return var0;
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int endcolor;
        this.drag(mouseX, mouseY);
        counter1 = new int[]{1};
        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        float f = totalItemHeight;
        if (ClickGui.getInstance().rainbowg.getValue().booleanValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.startcolor = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
                endcolor = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
            }
        } else {
            this.startcolor = ColorUtil.toRGBA(ClickGui.getInstance().g_red.getValue(), ClickGui.getInstance().g_green.getValue(), ClickGui.getInstance().g_blue.getValue(), ClickGui.getInstance().g_alpha.getValue());
        }
        endcolor = ColorUtil.toRGBA(ClickGui.getInstance().g_red1.getValue(), ClickGui.getInstance().g_green1.getValue(), ClickGui.getInstance().g_blue1.getValue(), ClickGui.getInstance().g_alpha1.getValue());
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + this.height - 5, ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 255));
        RenderUtil.drawGradientSideways(this.x - 1, this.y, this.x + this.width + 1, (float)(this.y + this.barHeight) - 2.0f, this.startcolor, endcolor);
        if (this.open) {
            RenderUtil.drawGradientSideways(this.x - 1, (float)this.y + 13.2f, this.x + this.width + 1, (float)this.y + totalItemHeight + 19.0f, this.startcolor, endcolor);
            RenderUtil.drawRect(this.x, (float)this.y + 13.2f, this.x + this.width, (float)(this.y + this.height) + totalItemHeight, ColorUtil.toRGBA(0, 0, 0, ClickGui.getInstance().alphaBox.getValue()));
        }
        OyVey.textManager.drawStringWithShadow(this.getName(), (float)this.x + 3.0f, (float)this.y - 4.0f - (float)OyVeyGui.getClickGui().getTextOffset(), -1);
        if (!this.open) {
            if (this.angle > 0) {
                this.angle -= 6;
            }
        } else if (this.angle < 180) {
            this.angle += 6;
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Component.glColor(new Color(255, 255, 255, 255));
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/arrow.png"));
        GlStateManager.translate((float)(this.getX() + this.getWidth() - 7), (float)((float)(this.getY() + 6) - 0.3f), (float)0.0f);
        GlStateManager.rotate((float)Component.calculateRotation(this.angle), (float)0.0f, (float)0.0f, (float)1.0f);
        Component.drawModalRect(-5, -5, 0.0f, 0.0f, 10, 10, 10, 10, 10.0f, 10.0f);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (this.open) {
            RenderUtil.drawRect(this.x, (float)this.y + 12.5f, this.x + this.width, (float)(this.y + this.height) + totalItemHeight, 0x77000000);
            if (ClickGui.getInstance().outline.getValue().booleanValue()) {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
                GlStateManager.shadeModel((int)7425);
                GL11.glBegin((int)2);
                GL11.glColor4f((float)((float)ClickGui.getInstance().red.getValue().intValue() / 255.0f), (float)((float)ClickGui.getInstance().green.getValue().intValue() / 255.0f), (float)((float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f), (float)255.0f);
                GL11.glVertex3f((float)this.x, (float)((float)this.y - 0.5f), (float)0.0f);
                GL11.glVertex3f((float)(this.x + this.width), (float)((float)this.y - 0.5f), (float)0.0f);
                GL11.glVertex3f((float)(this.x + this.width), (float)((float)(this.y + this.height) + totalItemHeight), (float)0.0f);
                GL11.glVertex3f((float)this.x, (float)((float)(this.y + this.height) + totalItemHeight), (float)0.0f);
                GL11.glEnd();
                GlStateManager.shadeModel((int)7424);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
            }
        }
        if (this.open) {
            float y = (float)(this.getY() + this.getHeight()) - 3.0f;
            for (Item item : this.getItems()) {
                Component.counter1[0] = counter1[0] + 1;
                if (item.isHidden()) continue;
                item.setLocation((float)this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += (float)item.getHeight() + 1.5f;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            OyVeyGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : this.getItems()) {
            height += (float)item.getHeight() + 1.5f;
        }
        return height;
    }
}

