package me.alpha432.oyvey;

import me.alpha432.oyvey.features.modules.client.RPC;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.manager.tntmanager.PlayerManager;
import me.alpha432.oyvey.manager.tntmanager.TntCManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OyVey implements ModInitializer, ClientModInitializer {
    public static final String NAME = "Cracked";
    public static final String VERSION = "2.0 - 1.20.4";

    public static float TIMER = 1f;

    public static final Logger LOGGER = LogManager.getLogger("Cracked");
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    //TNT
    public static PlayerManager playerManager;
    public static TntCManager tntCManager;
    //RenderStorage
    //Totem AutoCrystal
    public static AsyncManager asyncManager;
    public static CombatManager combatManager;
    public static HudManager hudManager;
    //Imagen
    //DesktopNotifier
 


    @Override 
    public void onInitialize() {
        combatManager = new CombatManager();
        asyncManager = new AsyncManager();
        hudManager = new HudManager();
        //StorageRender
        tntCManager = new TntCManager();
        playerManager = new PlayerManager();
        eventManager = new EventManager();
        serverManager = new ServerManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();
        if (isOnWindows())
            RPC.getInstance().startRpc();
    }

    public static boolean isOnWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    @Override 
    public void onInitializeClient() {
        MinecraftClient mc = MinecraftClient.getInstance();
        //for (IPayload payload : IPayload.getPayloads()) {
        //payload.run();
        //}
        eventManager.init();
        moduleManager.init();

        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
    }
}
