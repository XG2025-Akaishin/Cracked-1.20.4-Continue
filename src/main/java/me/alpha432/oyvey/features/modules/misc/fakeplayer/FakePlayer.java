package me.alpha432.oyvey.features.modules.misc.fakeplayer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import me.alpha432.oyvey.features.modules.misc.fakeplayer.utils.ChatUtils;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FakePlayer extends Module {

    public Setting<String> name = this.register(new Setting<String>("Name", "AnarchyGooD"));

    public FakePlayer() {
        super("FakePlayer", "FakePlayer", Category.MISC, true, false, false);
    }

    private OtherClientPlayerEntity FakePlayer;

    @Override
    public void onEnable() {
        super.onEnable();
        FakePlayer = null;

        if (mc.world == null || mc.player == null) {
            this.toggle();
            //this.toggle(true);
            return;
        }

        // If getting uuid from mojang doesn't work we use another uuid
        try {
            FakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString(getUuid(name.getValue())), name.getValue()));
        } catch (Exception e) {
            FakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString(getUuid(mc.player.getName().getString())), name.getValue()));
            ChatUtils.warningMessage("Failed to load uuid, setting another one.");
        }
        ChatUtils.sendMessage(String.format("%s has been spawned.", name.getValue()));

        FakePlayer.copyFrom(mc.player);
        FakePlayer.headYaw = mc.player.getHeadYaw();
        mc.world.addEntity(FakePlayer);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.world == null || mc.player == null ) return;
        mc.world.removeEntity(FakePlayer.getId(), Entity.RemovalReason.UNLOADED_WITH_PLAYER);
    }

    // Getting uuid from a name
    public static String getUuid(String name) {
        Gson gson = new Gson();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if(UUIDJson.isEmpty()) return "invalid name";
            JsonObject UUIDObject = gson.fromJson(UUIDJson, JsonObject.class);
            return reformatUuid(UUIDObject.get("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    // Reformating a short uuid type into a long uuid type
    private static String reformatUuid(String uuid) {
        String longUuid = "";

        longUuid += uuid.substring(1, 9) + "-";
        longUuid += uuid.substring(9, 13) + "-";
        longUuid += uuid.substring(13, 17) + "-";
        longUuid += uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);

        return longUuid;
    }
}
