package me.abHack.features.gui.alts.ias.gui;

import org.apache.commons.lang3.StringUtils;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.FontRenderer;
import java.util.Iterator;
import me.abHack.features.gui.alts.iasencrypt.EncryptionTools;
import me.abHack.features.gui.alts.tools.alt.AccountData;
import me.abHack.features.gui.alts.ias.config.ConfigValues;
import me.abHack.features.gui.alts.ias.tools.HttpTools;
import me.abHack.features.gui.alts.ias.account.AlreadyLoggedInException;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import me.abHack.features.gui.alts.tools.alt.AltManager;
import me.abHack.features.gui.alts.tools.alt.AltDatabase;
import me.abHack.features.gui.alts.ias.tools.JavaTools;
import me.abHack.features.gui.alts.ias.enums.EnumBool;
import me.abHack.features.gui.alts.tools.Tools;
import me.abHack.features.gui.alts.tools.Config;
import java.io.IOException;
import me.abHack.features.gui.alts.ias.tools.SkinTools;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import me.abHack.features.gui.alts.ias.account.ExtendedAccountData;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class GuiAccountSelector extends GuiScreen
{
    private int selectedAccountIndex;
    private int prevIndex;
    private Throwable loginfailed;
    private ArrayList<ExtendedAccountData> queriedaccounts;
    private List accountsgui;
    private GuiButton login;
    private GuiButton loginoffline;
    private GuiButton delete;
    private GuiButton edit;
    private GuiButton reloadskins;
    private String query;
    private GuiTextField search;

    public GuiAccountSelector() {
        this.queriedaccounts = this.convertData();
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (this.accountsgui = new List(this.mc)).registerScrollButtons(5, 6);
        this.query = I18n.format("ias.search", (Object[])new Object[0]);
        this.buttonList.clear();
        this.reloadskins = new GuiButton(8, this.width / 2 - 154 - 10, this.height - 76 - 8, 120, 20, I18n.format("ias.reloadskins", (Object[])new Object[0]));
        this.buttonList.add(this.reloadskins);
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 40, this.height - 52, 120, 20, I18n.format("ias.addaccount", (Object[])new Object[0])));
        this.login = new GuiButton(1, this.width / 2 - 154 - 10, this.height - 52, 120, 20, I18n.format("ias.login", (Object[])new Object[0]));
        this.buttonList.add(this.login);
        this.edit = new GuiButton(7, this.width / 2 - 40, this.height - 52, 80, 20, I18n.format("ias.edit", (Object[])new Object[0]));
        this.buttonList.add(this.edit);
        this.loginoffline = new GuiButton(2, this.width / 2 - 154 - 10, this.height - 28, 110, 20, I18n.format("ias.login", (Object[])new Object[0]) + " " + I18n.format("ias.offline", (Object[])new Object[0]));
        this.buttonList.add(this.loginoffline);
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 28, 110, 20, I18n.format("gui.cancel", (Object[])new Object[0])));
        this.delete = new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, I18n.format("ias.delete", (Object[])new Object[0]));
        this.buttonList.add(this.delete);
        (this.search = new GuiTextField(8, this.fontRenderer, this.width / 2 - 80, 14, 160, 16)).setText(this.query);
        this.updateButtons();
        if (!this.queriedaccounts.isEmpty()) {
            SkinTools.buildSkin(this.queriedaccounts.get(this.selectedAccountIndex).alias);
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.accountsgui.handleMouseInput();
    }

    public void updateScreen() {
        this.search.updateCursorCounter();
        this.updateText();
        this.updateButtons();
        if (this.prevIndex != this.selectedAccountIndex) {
            this.updateShownSkin();
            this.prevIndex = this.selectedAccountIndex;
        }
    }

    private void updateShownSkin() {
        if (!this.queriedaccounts.isEmpty()) {
            SkinTools.buildSkin(this.queriedaccounts.get(this.selectedAccountIndex).alias);
        }
    }

    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean flag = this.search.isFocused();
        this.search.mouseClicked(mouseX, mouseY, mouseButton);
        if (!flag && this.search.isFocused()) {
            this.query = "";
            this.updateText();
            this.updateQueried();
        }
    }

    private void updateText() {
        this.search.setText(this.query);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        Config.save();
    }

    public void drawScreen(final int par1, final int par2, final float par3) {
        this.accountsgui.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, I18n.format("ias.selectaccount", (Object[])new Object[0]), this.width / 2, 4, -1);
        if (this.loginfailed != null) {
            this.drawCenteredString(this.fontRenderer, this.loginfailed.getLocalizedMessage(), this.width / 2, this.height - 62, 16737380);
        }
        this.search.drawTextBox();
        super.drawScreen(par1, par2, par3);
        if (!this.queriedaccounts.isEmpty()) {
            SkinTools.javDrawSkin(8, this.height / 2 - 64 - 16, 64, 128);
            Tools.drawBorderedRect(this.width - 8 - 64, this.height / 2 - 64 - 16, this.width - 8, this.height / 2 + 64 - 16, 2, -5855578, -13421773);
            if (this.queriedaccounts.get(this.selectedAccountIndex).premium == EnumBool.TRUE) {
                this.drawString(this.fontRenderer, I18n.format("ias.premium", (Object[])new Object[0]), this.width - 8 - 61, this.height / 2 - 64 - 13, 6618980);
            }
            else if (this.queriedaccounts.get(this.selectedAccountIndex).premium == EnumBool.FALSE) {
                this.drawString(this.fontRenderer, I18n.format("ias.notpremium", (Object[])new Object[0]), this.width - 8 - 61, this.height / 2 - 64 - 13, 16737380);
            }
            this.drawString(this.fontRenderer, I18n.format("ias.timesused", (Object[])new Object[0]), this.width - 8 - 61, this.height / 2 - 64 - 15 + 12, -1);
            this.drawString(this.fontRenderer, String.valueOf(this.queriedaccounts.get(this.selectedAccountIndex).useCount), this.width - 8 - 61, this.height / 2 - 64 - 15 + 21, -1);
            if (this.queriedaccounts.get(this.selectedAccountIndex).useCount > 0) {
                this.drawString(this.fontRenderer, I18n.format("ias.lastused", (Object[])new Object[0]), this.width - 8 - 61, this.height / 2 - 64 - 15 + 30, -1);
                this.drawString(this.fontRenderer, JavaTools.getJavaCompat().getFormattedDate(), this.width - 8 - 61, this.height / 2 - 64 - 15 + 39, -1);
            }
        }
    }

    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == 3) {
                this.escape();
            }
            else if (button.id == 0) {
                this.add();
            }
            else if (button.id == 4) {
                this.delete();
            }
            else if (button.id == 1) {
                this.login(this.selectedAccountIndex);
            }
            else if (button.id == 2) {
                this.logino(this.selectedAccountIndex);
            }
            else if (button.id == 7) {
                this.edit();
            }
            else if (button.id == 8) {
                this.reloadSkins();
            }
            else {
                this.accountsgui.actionPerformed(button);
            }
        }
    }

    private void reloadSkins() {
        Config.save();
        SkinTools.cacheSkins();
        this.updateShownSkin();
    }

    private void escape() {
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    private void delete() {
        AltDatabase.getInstance().getAlts().remove(this.getCurrentAsEditable());
        if (this.selectedAccountIndex > 0) {
            --this.selectedAccountIndex;
        }
        this.updateQueried();
        this.updateButtons();
    }

    private void add() {
        this.mc.displayGuiScreen((GuiScreen)new GuiAddAccount());
    }

    private void logino(final int selected) {
        final ExtendedAccountData data = this.queriedaccounts.get(selected);
        AltManager.getInstance().setUserOffline(data.alias);
        this.loginfailed = null;
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
        final ExtendedAccountData current = this.getCurrentAsEditable();
        final ExtendedAccountData extendedAccountData = Objects.requireNonNull(current);
        ++extendedAccountData.useCount;
        current.lastused = JavaTools.getJavaCompat().getDate();
    }

    private void login(final int selected) {
        final ExtendedAccountData data = this.queriedaccounts.get(selected);
        this.loginfailed = AltManager.getInstance().setUser(data.user, data.pass);
        if (this.loginfailed == null) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
            final ExtendedAccountData current = this.getCurrentAsEditable();
            Objects.requireNonNull(current).premium = EnumBool.TRUE;
            final ExtendedAccountData extendedAccountData = current;
            ++extendedAccountData.useCount;
            current.lastused = JavaTools.getJavaCompat().getDate();
        }
        else if (this.loginfailed instanceof AlreadyLoggedInException) {
            Objects.requireNonNull(this.getCurrentAsEditable()).lastused = JavaTools.getJavaCompat().getDate();
        }
        else if (HttpTools.ping("https://minecraft.net")) {
            Objects.requireNonNull(this.getCurrentAsEditable()).premium = EnumBool.FALSE;
        }
    }

    private void edit() {
        this.mc.displayGuiScreen((GuiScreen)new GuiEditAccount(this.selectedAccountIndex));
    }

    private void updateQueried() {
        this.queriedaccounts = this.convertData();
        if (!this.query.equals(I18n.format("ias.search", (Object[])new Object[0])) && !this.query.equals("")) {
            for (int i = 0; i < this.queriedaccounts.size(); ++i) {
                if (!this.queriedaccounts.get(i).alias.contains(this.query) && ConfigValues.CASESENSITIVE) {
                    this.queriedaccounts.remove(i);
                    --i;
                }
                else if (!this.queriedaccounts.get(i).alias.toLowerCase().contains(this.query.toLowerCase())) {
                    if (!ConfigValues.CASESENSITIVE) {
                        this.queriedaccounts.remove(i);
                        --i;
                    }
                }
            }
        }
        if (!this.queriedaccounts.isEmpty()) {
            while (this.selectedAccountIndex >= this.queriedaccounts.size()) {
                --this.selectedAccountIndex;
            }
        }
    }

    protected void keyTyped(final char character, final int keyIndex) {
        if (keyIndex == 200 && !this.queriedaccounts.isEmpty()) {
            if (this.selectedAccountIndex > 0) {
                --this.selectedAccountIndex;
            }
        }
        else if (keyIndex == 208 && !this.queriedaccounts.isEmpty()) {
            if (this.selectedAccountIndex < this.queriedaccounts.size() - 1) {
                ++this.selectedAccountIndex;
            }
        }
        else if (keyIndex == 1) {
            this.escape();
        }
        else if (keyIndex == 211 && this.delete.enabled) {
            this.delete();
        }
        else if (character == '+') {
            this.add();
        }
        else if (character == '/' && this.edit.enabled) {
            this.edit();
        }
        else if (!this.search.isFocused() && keyIndex == 19) {
            this.reloadSkins();
        }
        else if (keyIndex == 28 && !this.search.isFocused() && (this.login.enabled || this.loginoffline.enabled)) {
            if ((Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42)) && this.loginoffline.enabled) {
                this.logino(this.selectedAccountIndex);
            }
            else if (this.login.enabled) {
                this.login(this.selectedAccountIndex);
            }
        }
        else if (keyIndex == 14) {
            if (this.search.isFocused() && this.query.length() > 0) {
                this.query = this.query.substring(0, this.query.length() - 1);
                this.updateText();
                this.updateQueried();
            }
        }
        else if (keyIndex == 63) {
            this.reloadSkins();
        }
        else if (character != '\0' && this.search.isFocused()) {
            if (keyIndex == 28) {
                this.search.setFocused(false);
                this.updateText();
                this.updateQueried();
                return;
            }
            this.query += character;
            this.updateText();
            this.updateQueried();
        }
    }

    private ArrayList<ExtendedAccountData> convertData() {
        final ArrayList<AccountData> tmp = (ArrayList<AccountData>)AltDatabase.getInstance().getAlts().clone();
        final ArrayList<ExtendedAccountData> converted = new ArrayList<ExtendedAccountData>();
        int index = 0;
        for (final AccountData data : tmp) {
            if (data instanceof ExtendedAccountData) {
                converted.add((ExtendedAccountData)data);
            }
            else {
                converted.add(new ExtendedAccountData(EncryptionTools.decode(data.user), EncryptionTools.decode(data.pass), data.alias));
                AltDatabase.getInstance().getAlts().set(index, new ExtendedAccountData(EncryptionTools.decode(data.user), EncryptionTools.decode(data.pass), data.alias));
            }
            ++index;
        }
        return converted;
    }

    private ArrayList<AccountData> getAccountList() {
        return AltDatabase.getInstance().getAlts();
    }

    private ExtendedAccountData getCurrentAsEditable() {
        for (final AccountData dat : this.getAccountList()) {
            if (dat instanceof ExtendedAccountData) {
                if (!dat.equals(this.queriedaccounts.get(this.selectedAccountIndex))) {
                    continue;
                }
                return (ExtendedAccountData)dat;
            }
        }
        return null;
    }

    private void updateButtons() {
        this.login.enabled = (!this.queriedaccounts.isEmpty() && !EncryptionTools.decode(this.queriedaccounts.get(this.selectedAccountIndex).pass).equals(""));
        this.loginoffline.enabled = !this.queriedaccounts.isEmpty();
        this.delete.enabled = !this.queriedaccounts.isEmpty();
        this.edit.enabled = !this.queriedaccounts.isEmpty();
        this.reloadskins.enabled = !AltDatabase.getInstance().getAlts().isEmpty();
    }

    class List extends GuiSlot
    {
        public List(final Minecraft mcIn) {
            super(mcIn, GuiAccountSelector.this.width, GuiAccountSelector.this.height, 32, GuiAccountSelector.this.height - 64, 14);
        }

        protected int getSize() {
            return GuiAccountSelector.this.queriedaccounts.size();
        }

        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            GuiAccountSelector.this.selectedAccountIndex = slotIndex;
            GuiAccountSelector.this.updateButtons();
            if (isDoubleClick && GuiAccountSelector.this.login.enabled) {
                GuiAccountSelector.this.login(slotIndex);
            }
        }

        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiAccountSelector.this.selectedAccountIndex;
        }

        protected int getContentHeight() {
            return GuiAccountSelector.this.queriedaccounts.size() * 14;
        }

        protected void drawBackground() {
            GuiAccountSelector.this.drawDefaultBackground();
        }

        protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
            final ExtendedAccountData data = GuiAccountSelector.this.queriedaccounts.get(slotIndex);
            String s = data.alias;
            if (StringUtils.isEmpty((CharSequence)s)) {
                s = I18n.format("ias.alt", (Object[])new Object[0]) + " " + (slotIndex + 1);
            }
            int color = 16777215;
            if (Minecraft.getMinecraft().getSession().getUsername().equals(data.alias)) {
                color = 65280;
            }
            GuiAccountSelector.this.drawString(GuiAccountSelector.this.fontRenderer, s, xPos + 2, yPos + 1, color);
        }
    }
}