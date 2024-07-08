package me.alpha432.oyvey.features.modules.chat.commandv;


import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class CustomCommand extends Module {
   private Setting<String> custom = this.register(new Setting("Custom", "/help"));

   public CustomCommand() {
      super("CustomCommand", "Message command custom", Module.Category.CHAT, true, false, false);
   }

   public void onTick() {
      if (!CustomCommand.fullNullCheck()) {
         //mc.player.networkHandler.sendChatMessage(((String)this.custom.getValue()));
         this.sendPlayerMsg((String)this.custom.getValue());
         this.disable();
      }
   }

   public void sendPlayerMsg(String message) {
      mc.inGameHud.getChatHud().addToMessageHistory(message);

      if (message.startsWith("/")) mc.player.networkHandler.sendChatCommand(message.substring(1));
      else mc.player.networkHandler.sendChatMessage(message);
  }
}
