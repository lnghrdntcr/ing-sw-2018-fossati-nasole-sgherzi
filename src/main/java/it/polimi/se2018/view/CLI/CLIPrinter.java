package it.polimi.se2018.view.CLI;

import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;

public class CLIPrinter {

    public static void setup(){
        AnsiConsole.systemInstall();
/*        System.out.println( ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset() );*/
    }

}
