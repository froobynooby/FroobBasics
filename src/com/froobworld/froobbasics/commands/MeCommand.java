package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TimeUtils;
import net.minecraft.server.v1_13_R2.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MeCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private PunishmentManager punishmentManager;

    public MeCommand(PlayerManager playerManager, PunishmentManager punishmentManager) {
        this.playerManager = playerManager;
        this.punishmentManager = punishmentManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        String message = StringUtils.join(args, " ");
        Punishment punishment = punishmentManager.getPunishment(player);
        if (punishment.isMuted()) {
            player.sendMessage(ChatColor.YELLOW + "You are muted for "
                    + TimeUtils.getDaysHoursMinutes(punishment.getMutedTime() + punishment.getMutePeriod() -
                    System.currentTimeMillis()) + ".");
            player.sendMessage(ChatColor.YELLOW + "You were muted for '" + punishment.getMuteReason() + "'");
            return false;
        }
        Playerdata senderData = playerManager.getPlayerdata(player);
        for (Player receiver : Bukkit.getOnlinePlayers()) {
            Playerdata data = playerManager.getPlayerdata(receiver);
            if (data.ignoreList().contains(player.getUniqueId())) {
                continue;
            }
            if (cl.equalsIgnoreCase("holding")) {
                if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    player.sendMessage(ChatColor.RED + "You're not holding anything!");
                    return false;
                }
                boolean friends = false;
                if (senderData.isFriendsOnlyChat()) {
                    friends = true;
                    if (receiver != player && !senderData.getFriends().contains(receiver.getUniqueId())) {
                        continue;
                    }
                }

                IChatBaseComponent component = bukkitStackToChatComponent(player.getInventory().getItemInMainHand());
                component.setChatModifier(component.getChatModifier().setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ITEM, (IChatBaseComponent) new ChatComponentText(convertItemStackToJsonRegular(player.getInventory().getItemInMainHand())))));
                @SuppressWarnings("deprecation")
                PlayerList h = MinecraftServer.getServer().getPlayerList();
                h.getPlayer(receiver.getName()).sendMessage(new IChatBaseComponent[]{
                        new ChatComponentText((friends ? "~ " : "* ") + player.getDisplayName() + " is holding ").addSibling(component)
                });

                continue;
            }
            receiver.sendMessage("* " + player.getDisplayName() + ChatColor.WHITE + " " + message);
        }
        System.out.println("* " + player.getDisplayName() + ChatColor.WHITE + " " + message);

        return true;
    }

    @Override
    public String command() {

        return "me";
    }

    @Override
    public String perm() {

        return "froobbasics.me";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

    private IChatBaseComponent bukkitStackToChatComponent(ItemStack stack) {
        net.minecraft.server.v1_13_R2.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        return nms.A();
    }

    private String convertItemStackToJsonRegular(org.bukkit.inventory.ItemStack itemStack) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStack.save(compound);

        return compound.toString();
    }

}
