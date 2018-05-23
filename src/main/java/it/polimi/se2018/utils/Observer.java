package it.polimi.se2018.utils;

public interface Observer<T> {
    public void update( T message);
}
