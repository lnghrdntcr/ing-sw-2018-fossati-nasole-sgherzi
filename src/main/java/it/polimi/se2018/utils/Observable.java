package it.polimi.se2018.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The classes that extend this can be observed by an {@link Observer}
 * @param <T> the object type that can be passed to the {@link Observer}
 */
public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<Observer<T>>();

    /**
     * Register an observer for this class
     * The observer will then be informed when notify is called
     * @param observer the observer to be register
     */
    public void register(Observer<T> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Remove the observer for this class
     * The observer won't be informed by this class anymore
     * @param observer
     */
    public void deregister(Observer<T> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notify the observers about changes in this class
     * @param message the message to be passed
     */
    protected void notify(T message) {
        synchronized (observers) {
            for (Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }

}