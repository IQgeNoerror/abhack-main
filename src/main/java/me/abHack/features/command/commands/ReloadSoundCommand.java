package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import me.abHack.features.command.Command;

public class ReloadSoundCommand extends Command
{
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }

    @Override
    public void execute(final String[] commands) {
        try {
            final SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)ReloadSoundCommand.mc.getSoundHandler(), new String[] { "sndManager", "sndManager" });
            sndManager.reloadSoundSystem();
            Command.sendMessage(ChatFormatting.GREEN + "Reloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println(ChatFormatting.RED + "Could not restart sound manager: " + e);
            e.printStackTrace();
            Command.sendMessage(ChatFormatting.RED + "Couldnt Reload Sound System!");
        }
    }
}