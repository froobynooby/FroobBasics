package com.froobworld.froobbasics;

import com.froobworld.froobbasics.commands.*;
import com.froobworld.froobbasics.data.Spawn;
import com.froobworld.froobbasics.listeners.DiscordListener;
import com.froobworld.froobbasics.listeners.EntityListener;
import com.froobworld.froobbasics.listeners.PlayerListener;
import com.froobworld.froobbasics.managers.*;
import com.froobworld.frooblib.FroobPlugin;
import com.froobworld.frooblib.uuid.UUIDManager;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class FroobBasics extends FroobPlugin {
    final Plugin discord = Bukkit.getServer().getPluginManager().getPlugin("DiscordSRV");
    private UUIDManager uuidManager;
    private HelpManager helpManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private PlayerManager playerManager;
    private PortalManager portalManager;
    private AfkManager afkManager;
    private PunishmentManager punishmentManager;
    private MessageManager messageManager;
    private TeleportManager teleportManager;
    private PollManager pollManager;
    private Spawn spawn;
    private DiscordListener discordListener;

    public static Plugin getPlugin() {

        return getPlugin(FroobBasics.class);
    }

    public void onEnable() {
        initiateManagers();
        regCommands();
        regEvents();
    }

    public void onDisable() {
        playerManager.task();
        punishmentManager.task();
        if (discord != null) {
            DiscordSRV.api.unsubscribe(discordListener);
        }
    }

    public void initiateManagers() {
        new Config(this);
        spawn = new Spawn();

        uuidManager = uuidManager();
        helpManager = new HelpManager();
        homeManager = new HomeManager();
        warpManager = new WarpManager();
        playerManager = new PlayerManager();
        portalManager = new PortalManager();
        afkManager = new AfkManager();
        punishmentManager = new PunishmentManager();
        messageManager = new MessageManager();
        teleportManager = new TeleportManager();
        discordListener = new DiscordListener();
        pollManager = new PollManager();

        registerManager(helpManager);
        registerManager(homeManager);
        registerManager(warpManager);
        registerManager(playerManager);
        registerManager(portalManager);
        registerManager(afkManager);
        registerManager(punishmentManager);
        registerManager(messageManager);
        registerManager(teleportManager);
        registerManager(pollManager);
        registerManager(new BroadcastManager());
    }

    public void regCommands() {
        registerCommand(new HelpCommand(helpManager));
        registerCommand(new HomeCommand(homeManager, playerManager));
        registerCommand(new SethomeCommand(homeManager));
        registerCommand(new HomesCommand(homeManager));
        registerCommand(new DelhomeCommand(homeManager));
        registerCommand(new WarpCommand(warpManager, playerManager));
        registerCommand(new SetwarpCommand(warpManager));
        registerCommand(new WarpsCommand(warpManager));
        registerCommand(new DelwarpCommand(warpManager));
        registerCommand(new MailCommand(playerManager, uuidManager));
        registerCommand(new SeenCommand(playerManager, uuidManager));
        registerCommand(new FirstjoinCommand(playerManager, uuidManager));
        registerCommand(new SetportalCommand(portalManager));
        registerCommand(new DelportalCommand(portalManager));
        registerCommand(new LinkportalsCommand(portalManager));
        registerCommand(new PortalsCommand(portalManager));
        registerCommand(new FriendCommand(playerManager, uuidManager));
        registerCommand(new AfkCommand(afkManager));
        registerCommand(new PlayerlistCommand(playerManager, afkManager));
        registerCommand(new MuteCommand(punishmentManager, uuidManager));
        registerCommand(new UnmuteCommand(punishmentManager, uuidManager));
        registerCommand(new StafflogCommand(punishmentManager, uuidManager));
        registerCommand(new BanCommand(punishmentManager, uuidManager));
        registerCommand(new UnbanCommand(punishmentManager, uuidManager));
        registerCommand(new JailCommand(punishmentManager, uuidManager));
        registerCommand(new UnjailCommand(punishmentManager, uuidManager));
        registerCommand(new JailsCommand(punishmentManager));
        registerCommand(new SetjailCommand(punishmentManager));
        registerCommand(new DeljailCommand(punishmentManager));
        registerCommand(new VanishCommand(playerManager));
        registerCommand(new CheckCommand(punishmentManager, uuidManager));
        registerCommand(new MessageCommand(messageManager, playerManager, punishmentManager));
        registerCommand(new ReplyCommand(messageManager, playerManager, punishmentManager));
        registerCommand(new SpawnCommand(spawn, playerManager));
        registerCommand(new SetspawnCommand(spawn));
        registerCommand(new WeatherCommand());
        registerCommand(new TeleportCommand(playerManager));
        registerCommand(new TphereCommand(playerManager));
        registerCommand(new TpaCommand(teleportManager, playerManager));
        registerCommand(new TpahereCommand(teleportManager, playerManager));
        registerCommand(new TpacceptCommand(teleportManager, playerManager));
        registerCommand(new TpdenyCommand(teleportManager));
        registerCommand(new MotdCommand(playerManager, afkManager));
        registerCommand(new RulesCommand());
        registerCommand(new IgnoreCommand(playerManager, uuidManager));
        registerCommand(new BackCommand(playerManager));
        registerCommand(new PingCommand());
        registerCommand(new MeCommand(playerManager, punishmentManager));
        registerCommand(new NamesCommand(uuidManager));
        registerCommand(new TptoggleCommand(playerManager));
        registerCommand(new PollCommand(pollManager));
    }

    public void regEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(playerManager, afkManager, punishmentManager, spawn), this);
        pm.registerEvents(new EntityListener(playerManager), this);
        if (discord != null) {
            DiscordSRV.api.subscribe(discordListener);
        }
    }

    public PlayerManager getPlayerManager() {

        return playerManager;
    }

}
