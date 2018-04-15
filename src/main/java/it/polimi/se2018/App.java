package it.polimi.se2018;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 **/

public class App {
  public static void main( String[] args ) {
    List<String> helloWorld = Arrays.asList("Hello ", "World!");
    helloWorld.forEach(System.out::print);
  }
}
