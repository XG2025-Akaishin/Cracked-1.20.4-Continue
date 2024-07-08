package me.alpha432.oyvey.event;

public class Event {
    //private Stage era = Stage.PRE;
    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }
    //public boolean isPre() {
    //    return era == Stage.PRE;
    //}
}
