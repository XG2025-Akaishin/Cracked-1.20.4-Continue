package me.alpha432.oyvey.event.impl.autoarmor;

import me.alpha432.oyvey.event.Stage;
import me.alpha432.oyvey.event.Event;

public class PlayerMotionUpdate extends Event {
    private final Stage stage;
    public PlayerMotionUpdate(Stage stage)
    {
        this.stage = stage;
        //super(p_Era);
    }
    public Stage getStage() {
        return stage;
    }
}
