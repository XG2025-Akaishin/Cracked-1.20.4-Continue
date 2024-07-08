package me.alpha432.oyvey.features.modules.chat.commandv;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import org.apache.commons.lang3.RandomStringUtils;
import me.alpha432.oyvey.features.modules.chat.commandv.utils.Timer;

public class MessageSpammer extends Module {
   private final Timer timer = new Timer();
   private Setting<String> custom = this.register(new Setting("Custom", "CustomSpam"));
   private Setting<Integer> random = this.register(new Setting("Random", 1, 1, 20));
   private Setting<Integer> delay = this.register(new Setting("Delay", 100, 0, 10000));

   public MessageSpammer() {
      super("SpammerTest", "MessageSpam", Module.Category.CHAT, true, false, false);
   }

   public void onTick() {
      if (!fullNullCheck()) {
         if (this.timer.passedMs((long)(Integer)this.delay.getValue())) {
            mc.player.networkHandler.sendChatMessage((String)this.custom.getValue() + "[" + RandomStringUtils.randomAlphanumeric((Integer)this.random.getValue()) + "]");
            this.timer.reset();
         }
      }
   }

   public void onLogin() {
      if (this.isEnabled()) {
         this.disable();
         this.enable();
      }

   }
}
