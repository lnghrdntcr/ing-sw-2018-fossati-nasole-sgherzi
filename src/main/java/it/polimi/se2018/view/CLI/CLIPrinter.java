package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.TurnHolder;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.model_view.PlayerImmutable;
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

    public static void printPlayer(PlayerImmutable player) {
        System.out.println("TODO: stampa player!!");
    }

    public static void printError(String errore) {
        System.out.println("TODO: stampa errore: "+errore);
    }

    public static void printQuestion(String question) {
        System.out.println("TODO: stampa domanda: "+question);
    }
    public static void printMenuLine(int option, String line){
        System.out.println(ansi().fg(RED).a(option).fg(BLUE).a(line).reset());
    }

    public static void renderDraftboard(DraftBoardImmutable draftBoardImmutable) {
    }

    public static void renderRoundTrack(DiceHolderImmutable diceHolderImmutable) {
    }
}
