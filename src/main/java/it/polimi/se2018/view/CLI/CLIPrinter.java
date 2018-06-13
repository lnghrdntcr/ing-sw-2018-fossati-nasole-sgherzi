package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.TurnHolder;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema_card.*;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.utils.Settings;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.awt.*;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;

public class CLIPrinter {

    public static void setup() {
        AnsiConsole.systemInstall();
        /*        System.out.println( ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset() );*/
    }

    public static void printSchemaCardFace(SchemaCardFace schemaCardFace) {
        System.out.println("Name: " + schemaCardFace.getName() + "\nDifficulty: " + schemaCardFace.getDifficulty());
        for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
            printLineSeparator();
            for (int x = 0; x < Settings.CARD_WIDTH; x++) {
                printRestriction(schemaCardFace.getRestriction(new Point(x, y)));
            }
            System.out.println("|\n");
        }
        printLineSeparator();
    }

    //support method for printSchemaCardFace
    private static void printRestriction(CellRestriction cellRestriction) {
        if (cellRestriction instanceof NoRestriction) {
            System.out.println("| ");
        } else if (cellRestriction instanceof NumberRestriction) {
            System.out.println("|" + ((NumberRestriction) cellRestriction).getNumber());
        } else {
            System.out.println("|" + ansi().bg(((ColorRestriction) cellRestriction).getColor().getAnsiColor()).a(" "));
        }
    }

    //support method for printSchemaCardFace
    private static void printLineSeparator() {
        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            System.out.println("+-");
        }
        System.out.println("+\n");
    }

    public static void printPrivateObjective(PrivateObjective privateObjective){
        Ansi.Color color = privateObjective.getColor().getAnsiColor();
        System.out.println(ansi().bg(color).a(" "));
        System.out.println(ansi().fg(color).a(">") + "Sum the values of all the" + ansi().fg(color).a(color.toString()) + "dice");
        System.out.println(ansi().fg(color).a("<"));
        System.out.println(ansi().bg(color).a(" "));
    }

    public static void printPlayer(PlayerImmutable player) {
        System.out.println("TODO: stampa player!!");
    }

    public static void printError(String errore) {
        System.out.println("TODO: stampa errore: " + errore);
    }

    public static void printQuestion(String question) {
        System.out.println("TODO: stampa domanda: " + question);
    }

    public static void printMenuLine(int option, String line) {
        System.out.println(ansi().fg(RED).a(option).fg(BLUE).a(line).reset());
    }

    public static void printDraftBoard(DraftBoardImmutable draftBoardImmutable) {
    }

    public static void printDiceHolder(DiceHolderImmutable diceHolderImmutable) {

    }

    public static void printToolcard(ToolCardImmutable toolCardImmutable){

    }

    public static void printPublicObjectives(PublicObjective publicObjective) {
    }

    public static void printSchema(Schema schema){

    }

    public static Point decodePosition(String input) {
        if(input==null) throw new NullPointerException("Input should not be null!");
        if(input.length()>2) return null;
        int x = input.toUpperCase().charAt(0)-'A';
        if(x<0||x>= Settings.CARD_WIDTH) return null;


        try {
            int y = Integer.parseInt(input.substring(1));
            if(y<0||y>= Settings.CARD_HEIGHT) return null;
            return new Point(x, y);
        }catch (NumberFormatException ex){
            return null;
        }

    }
}
