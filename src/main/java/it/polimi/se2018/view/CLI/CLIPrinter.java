package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.TurnHolder;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.DiceFace;
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
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CLIPrinter {

    public static void setup() {
        AnsiConsole.systemInstall();
        /*        System.out.println( ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset() );*/
    }

    public static void printSchemaCardFace(SchemaCardFace schemaCardFace) {
        System.out.println("Name: " + schemaCardFace.getName() + "\nDifficulty: " + schemaCardFace.getDifficulty());
        for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
            printLineSeparator(Settings.CARD_WIDTH);
            for (int x = 0; x < Settings.CARD_WIDTH; x++) {
                printRestriction(schemaCardFace.getRestriction(new Point(x, y)));
            }
            System.out.println("|\n");
        }
        printLineSeparator(Settings.CARD_WIDTH);
    }

    //support method for printSchemaCardFace
    private static void printRestriction(CellRestriction cellRestriction) {
        if (cellRestriction instanceof NoRestriction) {
            System.out.println("| ");
        } else if (cellRestriction instanceof NumberRestriction) {
            System.out.println("|" + ((NumberRestriction) cellRestriction).getNumber());
        } else {
            System.out.println("|" + ansi().bg(((ColorRestriction) cellRestriction).getColor().getAnsiColor()).a(" ").reset());
        }
    }

    //support method for printSchemaCardFace
    private static void printLineSeparator(int param) {
        for (int x = 0; x < param; x++) {
            System.out.println("+-");
        }
        System.out.println("+\n");
    }

    public static void printPrivateObjective(PrivateObjective privateObjective){
        Ansi.Color color = privateObjective.getColor().getAnsiColor();
        System.out.println(ansi().bg(color).a(" ").reset());
        System.out.println(ansi().fg(color).a(">").reset() + "Sum the values of all the" + ansi().fg(color).a(color.toString()).reset() + "dice");
        System.out.println(ansi().fg(color).a("<").reset());
        System.out.println(ansi().bg(color).a(" ").reset());
    }

    public static void printPlayer(CLIGameTable cliGameTable, PlayerImmutable player) {
        System.out.println(player.getName());
        printSchema(cliGameTable.getSchemas(player.getName()));
    }

    public static void printError(String error) {
        System.out.println(ansi().fg(Ansi.Color.RED).a(error).reset());
    }

    public static void printQuestion(String question) {
        System.out.println(ansi().fg(Ansi.Color.BLUE).a(question).reset());
    }

    public static void printMenuLine(int option, String line) {
        System.out.println(ansi().fg(RED).a(option).fg(BLUE).a(line).reset());
    }

    public static void printDraftBoard(DraftBoardImmutable draftBoardImmutable) {
        printLineSeparator(draftBoardImmutable.getDices().length);
        for(int i = 0; i < draftBoardImmutable.getDices().length; i++){
            System.out.println("|" +
                    ansi().bg(draftBoardImmutable.getDices()[i].getColor().getAnsiColor())
                            .a(draftBoardImmutable.getDices()[i].getNumber()));
        }
        System.out.println("|\n");

        printLineSeparator(draftBoardImmutable.getDices().length);

        for(int i = 0; i < draftBoardImmutable.getDices().length; i++){
            System.out.println(" " + (i));
        }
    }

    public static void printDiceHolder(DiceHolderImmutable diceHolderImmutable) {
        for(int i=0; i<Settings.TURNS; i++){
            //TODO:
        }

    }

    public static void printToolcard(ToolCardImmutable toolCardImmutable){

    }

    public static void printPublicObjectives(PublicObjective publicObjective) {
    }

    public static void printSchema(Schema schema){
        System.out.println("Name: " + schema.getSchemaCardFace().getName() + "\nDifficulty: " + schema.getSchemaCardFace().getDifficulty());
        for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
            printLineSeparator(Settings.CARD_WIDTH);
            for (int x = 0; x < Settings.CARD_WIDTH; x++) {
                Point point = new Point(x, y);
                if(schema.getDiceFace(point)==null) {
                    printRestriction(schema.getSchemaCardFace().getRestriction(new Point(x, y)));
                }else{
                    printDice(schema.getDiceFace(point));
                }
            }
            System.out.println("|\n");
        }
        printLineSeparator(Settings.CARD_WIDTH);
    }

    private static void printDice(DiceFace diceFace) {
        System.out.println("|" + ansi().bg(diceFace.getColor().getAnsiColor()).a(""+diceFace.getNumber()).reset());
    }

    public static void printTokens(int remaining, int total) {

        for (int i = 0; i < remaining; i++) {
            System.out.println(ansi().bg(WHITE).reset());
        }
        for (int i = 0; i < (total - remaining); i++) {
            System.out.println(ansi().bg(RED).reset());
        }

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
