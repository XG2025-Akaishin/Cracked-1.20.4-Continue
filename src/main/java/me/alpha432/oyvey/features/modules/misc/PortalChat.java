package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.mixin.portalchat.IEntity;
import me.alpha432.oyvey.features.modules.Module;

public class PortalChat extends Module {

    public PortalChat() {
        super("PortalChat", "PortalChat", Category.MISC, true, false, false);
    }

    @Override
    public void onUpdate() {
        ((IEntity)mc.player).setInNetherPortal(false);
    }
}
