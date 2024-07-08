package me.alpha432.oyvey.features.modules.chat;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.*;
import java.awt.*;
import com.google.common.eventbus.Subscribe;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.chat.moduletools.ModuleTools;
import me.alpha432.oyvey.features.modules.chat.moduletools.utils.TextUtil;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.commands.Command;

public class CrackedPop extends Module {
    private final Object2IntMap<UUID> totemPopMap = new Object2IntOpenHashMap<>();
    public static String lagText = "\u0101\u0201\u0301\u0401\u0501\u0601\u0701\u0801\u0901\u0a01\u0b01\u0e01\u0f01\u1001\u1101\u1201\u1301\u1401\u1501\u1601\u1701\u1801\u1901\u1a01\u1b01\u1c01\u1d01\u1e01\u1f01\u2101\u2201\u2301\u2401\u2501\u2701\u2801\u2901\u2a01\u2b01\u2c01\u2d01\u2e01\u2f01\u3001\u3101\u3201\u3301\u3401\u3501\u3601\u3701\u3801\u3901\u3a01\u3b01\u3c01\u3d01\u3e01\u3f01\u4001\u4101\u4201\u4301\u4401\u4501\u4601\u4701\u4801\u4901\u4a01\u4b01\u4c01\u4d01\u4e01\u4f01\u5001\u5101\u5201\u5301\u5401\u5501\u5601\u5701\u5801\u5901\u5a01\u5b01\u5c01\u5d01\u5e01\u5f01\u6001\u6101\u6201\u6301\u6401\u6501\u6601\u6701\u6801\u6901\u6a01\u6b01\u6c01\u6d01\u6e01\u6f01\u7001\u7101\u7201\u7301\u7401\u7501\u7601\u7701\u7801\u7901\u7a01\u7b01\u7c01\u7d01\u7e01\u7f01\u8001\u8101\u8201\u8301\u8401\u8501\u8601\u8701\u8801\u8901\u8a01\u8b01\u8c01\u8d01\u8e01\u8f01\u9001\u9101\u9201\u9301\u9401\u9501\u9601\u9701\u9801\u9901\u9a01\u9b01\u9c01\u9d01\u9e01\u9f01\ua001\ua101\ua201\ua301\ua401\ua501\ua601\ua701\ua801\ua901\uaa01\uab01\uac01\uad01\uae01\uaf01\ub001\ub101\ub201\ub301\ub401\ub501\ub601\ub701\ub801\ub901\uba01\ubb01\ubc01\ubd01";
    private final Setting<Boolean> totemPops = this.register(new Setting<Boolean>("totemPops", false));
    private final Setting<Boolean> totemsIgnoreOthers = this.register(new Setting<Boolean>("totemsIgnoreOthers", false, v -> this.totemPops.getValue()));
    private final Setting<Boolean> totemsIgnoreOwn = this.register(new Setting<Boolean>("totemsIgnoreOthers", false, v -> this.totemPops.getValue()));
    private final Setting<Boolean> totemsIgnoreFriends = this.register(new Setting<Boolean>("totemsIgnoreOthers", false, v -> this.totemPops.getValue()));
    private final Setting<Boolean> popevent = this.register(new Setting<Boolean>("PopEvent", false));
    private final Setting<Boolean> ignoreFriends = this.register(new Setting<Boolean>("ignoreFriends", false, v -> this.popevent.getValue()));
    private final Setting<Boolean> poplag = this.register(new Setting<Boolean>("PopLag", false, v -> this.popevent.getValue()));
    private final Setting<Boolean> popmsg = this.register(new Setting<Boolean>("MessagePop", false, v -> this.popevent.getValue()));
    public Setting<String> msgpop = this.register(new Setting<String>("PopMessage", "Ez pop", v -> this.popmsg.getValue()));
    private final Setting<Boolean> popez = this.register(new Setting<Boolean>("MessageEz", false, v -> this.popevent.getValue()));
    public Setting<String> msgez = this.register(new Setting<String>("MessageDeath", "ezzzz rat", v -> this.popez.getValue()));


    public CrackedPop() {
        super("CrackedPop", "ezzzz kits noobs", Module.Category.CHAT, true, false, false);
    }

    @Subscribe//@EventHandler
    public void onReceivePacket(PacketEvent.Receive event) {
        ChatHud chatHud = mc.inGameHud.getChatHud();
        if (!totemPops.getValue()) return;
        if (!(event.packet instanceof EntityStatusS2CPacket p)) return;

        if (p.getStatus() != 35) return;

        Entity entity = p.getEntity(mc.world);

        if (!(entity instanceof PlayerEntity)) return;

        if ((entity.equals(mc.player) && totemsIgnoreOwn.getValue())
            || (OyVey.friendManager.isFriend(((PlayerEntity) entity)) && totemsIgnoreOthers.getValue())
            || (!OyVey.friendManager.isFriend(((PlayerEntity) entity)) && totemsIgnoreFriends.getValue())
        ) return;

        synchronized (totemPopMap) {
            int pops = totemPopMap.getOrDefault(entity.getUuid(), 0);
            totemPopMap.put(entity.getUuid(), ++pops);
            Command.sendMessage(Formatting.GRAY + " popped " + entity.getName().getString() + " " + Formatting.GREEN + ( ( pops) > 1 ? pops + " " + Formatting.GRAY +"totems" : Formatting.GREEN + "1" + Formatting.GRAY + " totem"));//+ pops + strg);
            if (popevent.getValue()){
            if (!ignoreFriends.getValue() || (ignoreFriends.getValue() && !OyVey.friendManager.isFriend((PlayerEntity)entity))) {
                if (poplag.getValue()){
                   this.sendPlayerMsg("/msg "+ entity.getName().getString() +" "+ lagText);
                }
                if (popmsg.getValue()){
                   this.sendPlayerMsg("/msg "+ entity.getName().getString() +" "+ (String)this.msgpop.getValue());
                }
                if (popez.getValue()){
                   this.sendPlayerMsg("/msg " + entity.getName().getString() +" "+ (String)this.msgez.getValue());
                }
            }
        }
    }
}

    public void sendPlayerMsg(String message) {
        mc.inGameHud.getChatHud().addToMessageHistory(message);
  
        if (message.startsWith("/")) mc.player.networkHandler.sendChatCommand(message.substring(1));
        else mc.player.networkHandler.sendChatMessage(message);
    }
}
