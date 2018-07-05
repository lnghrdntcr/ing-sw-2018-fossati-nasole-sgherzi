package it.polimi.se2018.model;

/**
 * An object that extends this interface can be cloned as an immutable instance
 * @param <T> the type of the immutable instance
 */
public interface ImmutableCloneable<T> {
    /**
     * Gets an immutable instance of the object
     * @return an immutable instance of the object
     */
    public T getImmutableInstance();
}
