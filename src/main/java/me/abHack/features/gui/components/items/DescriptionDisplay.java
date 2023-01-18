/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.gui.components.items;

import me.abHack.OyVey;
import me.abHack.features.gui.components.items.Item;
import me.abHack.util.RenderUtil;

public class DescriptionDisplay
extends Item {
    private String description;
    private boolean draw;

    public DescriptionDisplay(String description, float x, float y) {
        super("DescriptionDisplay");
        this.description = description;
        this.setLocation(x, y);
        this.width = OyVey.textManager.getStringWidth(this.description) + 4;
        this.height = OyVey.textManager.getFontHeight() + 4;
        this.draw = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.width = OyVey.textManager.getStringWidth(this.description) + 4;
        this.height = OyVey.textManager.getFontHeight() + 4;
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, -704643072);
        OyVey.textManager.drawString(this.description, this.x + 2.0f, this.y + 2.0f, 0xFFFFFF, true);
    }

    public boolean shouldDraw() {
        return this.draw;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}

