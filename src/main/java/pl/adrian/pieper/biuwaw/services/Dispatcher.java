/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.services;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Adi
 */
public class Dispatcher implements Serializable {
    
    private static Dispatcher instance =new Dispatcher();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Dispatcher getInstance() {
        return instance;
    }
    
    public interface BroadcastListener {
        void receiveBroadcast(String message);
    }

    private final LinkedList<BroadcastListener> listeners = new LinkedList<>();

    public synchronized void register(BroadcastListener listener) {
        listeners.add(listener);
    }

    public synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public synchronized void broadcast(final String message) {
        listeners.stream().forEach((listener) -> {
            executorService.execute(() -> {
                listener.receiveBroadcast(message);
            });
        });
    }
}
