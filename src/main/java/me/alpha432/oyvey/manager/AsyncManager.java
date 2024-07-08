package me.alpha432.oyvey.manager;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import me.alpha432.oyvey.event.impl.autototem.EventSync;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncManager {
    public static ExecutorService executor = Executors.newCachedThreadPool();


    private volatile Iterable<Entity> threadSafeEntityList = Collections.emptyList();
    private volatile List<AbstractClientPlayerEntity> threadSafePlayersList = Collections.emptyList();
    public final AtomicBoolean ticking = new AtomicBoolean(false);


    public Iterable<Entity> getAsyncEntities() {
        return threadSafeEntityList;
    }

    public List<AbstractClientPlayerEntity> getAsyncPlayers() {
        return threadSafePlayersList;
    }


    public void run(Runnable runnable, long delay) {
        executor.execute(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.run();
        });
    }

    public void run(Runnable r) {
        executor.execute(r);
    }

    //@EventHandler
    @Subscribe
    public void onSync(EventSync e) {
        }
}