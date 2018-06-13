package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;

public class CLIPrinter {

    public static void setup(){
        AnsiConsole.systemInstall();
/*        System.out.println( ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset() );*/
    }

    public static void printSchemaCardFace(SchemaCardFace schemaCardFace){

    }

    public static void printMenuLine(int option, String line){
        System.out.println(ansi().fg(RED).a(option).fg(BLUE).a(line).reset());
    }

}
