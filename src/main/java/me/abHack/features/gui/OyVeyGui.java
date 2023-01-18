/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.input.Mouse
 */
package me.abHack.features.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import me.abHack.OyVey;
import me.abHack.features.Feature;
import me.abHack.features.gui.components.Component;
import me.abHack.features.gui.components.items.DescriptionDisplay;
import me.abHack.features.gui.components.items.Item;
import me.abHack.features.gui.components.items.buttons.ModuleButton;
import me.abHack.features.gui.particle.ParticleSystem;
import me.abHack.features.gui.particle.Snow;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class OyVeyGui
extends GuiScreen {
    private static final DescriptionDisplay descriptionDisplay;
    private static OyVeyGui oyveyGui;
    private static OyVeyGui INSTANCE;
    private final ArrayList<Snow> _snowList = new ArrayList();
    private final ArrayList<Component> components = new ArrayList();
    public ParticleSystem particleSystem;

    public OyVeyGui() {
        this.setInstance();
        this.load();
    }

    public static OyVeyGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OyVeyGui();
        }
        return INSTANCE;
    }

    public static OyVeyGui getClickGui() {
        return OyVeyGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -84;
        Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            for (int y = 0; y < 3; ++y) {
                Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
                this._snowList.add(snow);
            }
        }
        for (final Module.Category category : OyVey.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 90, 4, true){

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    OyVey.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton((Module)module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGui = ClickGui.getInstance();
        descriptionDisplay.setDraw(false);
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        if (descriptionDisplay.shouldDraw() && clickGui.moduleDescription.getValue().booleanValue()) {
            descriptionDisplay.drawScreen(mouseX, mouseY, partialTicks);
        }
        ScaledResolution res = new ScaledResolution(this.mc);
        if (!this._snowList.isEmpty() && ClickGui.getInstance().snowing.getValue().booleanValue()) {
            this._snowList.forEach(snow -> snow.Update(res));
        }
        if (this.particleSystem != null && ClickGui.getInstance().particles.getValue().booleanValue()) {
            this.particleSystem.render(mouseX, mouseY);
        } else {
            this.particleSystem = new ParticleSystem(new ScaledResolution(this.mc));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public DescriptionDisplay getDescriptionDisplay() {
        return descriptionDisplay;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    public void updateScreen() {
        if (this.particleSystem != null) {
            this.particleSystem.update();
        }
    }

    static {
        INSTANCE = new OyVeyGui();
        descriptionDisplay = new DescriptionDisplay("", 0.0f, 0.0f);
    }
}

