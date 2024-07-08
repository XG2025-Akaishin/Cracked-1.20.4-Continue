package me.alpha432.oyvey.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.chat.CrackedPop;
import me.alpha432.oyvey.features.modules.chat.chatmodifier.ChatModifier;
import me.alpha432.oyvey.features.modules.chat.commandv.CustomCommand;
import me.alpha432.oyvey.features.modules.chat.commandv.MessageSpammer;
import me.alpha432.oyvey.features.modules.chat.moduletools.ModuleTools;
import me.alpha432.oyvey.features.modules.chat.notifications.Notifications;
import me.alpha432.oyvey.features.modules.client.Cape;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.features.modules.client.RPC;
import me.alpha432.oyvey.features.modules.combat.Criticals;
import me.alpha432.oyvey.features.modules.combat.autoarmor.AutoArmor;
import me.alpha432.oyvey.features.modules.combat.autocrystal.AutoCrystal;
import me.alpha432.oyvey.features.modules.combat.autototem.AutoTotem;
import me.alpha432.oyvey.features.modules.combat.burrow.Burrow;
import me.alpha432.oyvey.features.modules.combat.tntaura.TNTAura;
import me.alpha432.oyvey.features.modules.misc.AutoBedDupe;
import me.alpha432.oyvey.features.modules.misc.AutoFrameDupe;
import me.alpha432.oyvey.features.modules.misc.EntityNotifier;
import me.alpha432.oyvey.features.modules.misc.MCF;
import me.alpha432.oyvey.features.modules.misc.PortalChat;
import me.alpha432.oyvey.features.modules.misc.autoauth.AutoAuth;
import me.alpha432.oyvey.features.modules.misc.autoframedupe.FrameDupeTest;
import me.alpha432.oyvey.features.modules.misc.fakeplayer.FakePlayer;
import me.alpha432.oyvey.features.modules.movement.AutoWalk;
import me.alpha432.oyvey.features.modules.movement.InvMove;
import me.alpha432.oyvey.features.modules.movement.ReverseStep;
import me.alpha432.oyvey.features.modules.movement.Step;
import me.alpha432.oyvey.features.modules.movement.autosprint.AutoSprint;
import me.alpha432.oyvey.features.modules.movement.autosprint.Sprint;
import me.alpha432.oyvey.features.modules.movement.flight.Flight;
import me.alpha432.oyvey.features.modules.movement.noslow.NoSlow;
import me.alpha432.oyvey.features.modules.player.AutoRespawn;
import me.alpha432.oyvey.features.modules.player.FastPlace;
import me.alpha432.oyvey.features.modules.player.VelocityExplosion;
import me.alpha432.oyvey.features.modules.player.ViewLock;
import me.alpha432.oyvey.features.modules.player.XCarry;
import me.alpha432.oyvey.features.modules.player.fastuse.FastUse;
import me.alpha432.oyvey.features.modules.player.hotbarreplenish.HotbarReplenish;
import me.alpha432.oyvey.features.modules.player.middleclick.MiddleClick;
import me.alpha432.oyvey.features.modules.player.velocity.Velocity;
import me.alpha432.oyvey.features.modules.render.AspectRatio;
import me.alpha432.oyvey.features.modules.render.FreeLook;
import me.alpha432.oyvey.features.modules.render.HandModifier;
import me.alpha432.oyvey.features.modules.render.cameraclip.NoCameraClip;
import me.alpha432.oyvey.features.modules.render.fov.FOV;
import me.alpha432.oyvey.features.modules.render.fullbright.Fullbright;
import me.alpha432.oyvey.util.traits.Jsonable;
import me.alpha432.oyvey.util.traits.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager implements Jsonable, Util {
    public List<Module> modules = new ArrayList<>();
    public List<Module> sortedModules = new ArrayList<>();
    public List<Module> alphabeticallySortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<>();

    public void init() {
        modules.add(new HUD());
        modules.add(new AutoWalk());
        modules.add(new TNTAura());
        modules.add(new AutoSprint());
        modules.add(new InvMove());
        modules.add(new FrameDupeTest());
        modules.add(new ClickGui());
        modules.add(new Criticals());
        modules.add(new FreeLook());
        modules.add(new HandModifier());
        modules.add(new AutoAuth());
        modules.add(new FastUse());
        modules.add(new MCF());
        modules.add(new Step());
        modules.add(new ReverseStep());
        modules.add(new FastPlace());
        modules.add(new VelocityExplosion());
        modules.add(new HotbarReplenish());
        modules.add(new ViewLock());
        modules.add(new XCarry());
        modules.add(new AspectRatio());
        modules.add(new NoSlow());
        modules.add(new FOV());
        modules.add(new CrackedPop());
        modules.add(new CustomCommand());
        modules.add(new MessageSpammer());
        modules.add(new PortalChat());
        modules.add(new Cape());
        modules.add(new AutoCrystal());
        modules.add(new AutoTotem());
        modules.add(new ChatModifier());
        modules.add(new ModuleTools());
        modules.add(new Notifications());
        modules.add(new AutoArmor());
        modules.add(new FakePlayer());
        modules.add(new Flight());
        modules.add(new Fullbright());
        modules.add(new Sprint());
        modules.add(new Velocity());
        modules.add(new NoCameraClip());
        modules.add(new AutoFrameDupe());
        modules.add(new EntityNotifier());
        modules.add(new Burrow());
        modules.add(new AutoRespawn());
        modules.add(new MiddleClick());
        modules.add(new RPC());
        modules.add(new AutoBedDupe());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn)
                .sorted(Comparator.comparing(module -> mc.textRenderer.getWidth(module.getFullArrayString()) * (reverse ? -1 : 1)))
                .collect(Collectors.toList());
    }

    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onUnload() {
        this.modules.forEach(EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey <= 0) return;
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    @Override public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Module module : modules) {
            object.add(module.getName(), module.toJson());
        }
        return object;
    }

    @Override public void fromJson(JsonElement element) {
        for (Module module : modules) {
            module.fromJson(element.getAsJsonObject().get(module.getName()));
        }
    }

    @Override public String getFileName() {
        return "modules.json";
    }
}
