package me.alpha432.oyvey.event;

public class StageEvent extends Event {
    private final Stage stage;

    public StageEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

}

