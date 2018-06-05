package it.polimi.se2018.utils;

public class Tuple <T, R>{

    private final T first;
    private final R second;

    public Tuple(T t, R r){
        this.first = t;
        this.second = r;
    }

    public T getFirst(){
        return this.first;
    }

    public R getSecond(){
        return this.second;
    }

    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }
}
