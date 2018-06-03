package it.polimi.se2018.utils;

/**
 * A class should implement this interface if it wants to receive message from {@link Observable} classes
 * @param <T> the object type that can be passed to the {@link Observer}
 */
public interface Observer<T> {
    /**
     * This methot will be called each time the {@link Observable} class needs to inform this {@link Observer} about changes
     * @param message a message passed from the {@link Observable}
     */
    public void update( T message);
}
