package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.DiceHolder;
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
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.utils.Settings;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CLIPrinter {

    public static void setup() {
        AnsiConsole.systemInstall();

    }

    public static void printSchemaCardFace(SchemaCardFace schemaCardFace) {
        System.out.println("Name: " + schemaCardFace.getName() + "\nDifficulty: " + schemaCardFace.getDifficulty());
        for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
            printLineSeparator(Settings.CARD_WIDTH);
            for (int x = 0; x < Settings.CARD_WIDTH; x++) {
                printRestriction(schemaCardFace.getRestriction(new Point(x, y)));
            }
            System.out.println("|");
        }
        printLineSeparator(Settings.CARD_WIDTH);
    }

    //support method for printSchemaCardFace
    private static void printRestriction(CellRestriction cellRestriction) {
        if (cellRestriction instanceof NoRestriction) {
            System.out.print("| ");
        } else if (cellRestriction instanceof NumberRestriction) {
            System.out.print("|" + ((NumberRestriction) cellRestriction).getNumber());
        } else {
            System.out.print("|" + ansi().bg(((ColorRestriction) cellRestriction).getColor().getAnsiColor()).a(" ").reset());
        }
    }

    //support method for EVERYTHING
    private static void printLineSeparator(int param) {
        for (int x = 0; x < param; x++) {
            System.out.print("+-");
        }
        System.out.println("+");
    }

    public static void printPrivateObjective(PrivateObjective privateObjective) {
        Ansi.Color color = privateObjective.getColor().getAnsiColor();
        System.out.print(ansi().bg(color).a(" ").reset());
        System.out.print(ansi().fg(color).a(">").reset() + "Sum the values of all the " + ansi().fg(color).a(color.toString().toLowerCase()).reset() + " dice");
        System.out.print(ansi().fg(color).a("<").reset());
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
        printMenuLine(option, line, true);
    }

    public static void printMenuLine(int option, String line, boolean enabled) {
        System.out.println(ansi().fg(RED).a(option + " => ").fg(enabled ? BLUE : MAGENTA).a(line).reset());
    }


    public static void printDraftBoard(DraftBoardImmutable draftBoardImmutable) {
        printLineSeparator(draftBoardImmutable.getDices().length);
        for (int i = 0; i < draftBoardImmutable.getDices().length; i++) {
            System.out.print("|" +
                    ansi().bg(draftBoardImmutable.getDices()[i].getColor().getAnsiColor())
                            .a(draftBoardImmutable.getDices()[i].getNumber()).reset());
        }
        System.out.println("|");

        printLineSeparator(draftBoardImmutable.getDices().length);

        for (int i = 0; i < draftBoardImmutable.getDices().length; i++) {
            System.out.print(" " + (i));
        }
        System.out.println();
    }

    public static void printDiceHolder(DiceHolderImmutable diceHolderImmutable) {

        int pastTurns = diceHolderImmutable.getDoneTurns();

        int[] tLenght = new int[pastTurns];
        for(int i=0; i<pastTurns; i++){
            tLenght[i]=diceHolderImmutable.getDiceFaces(i).length;
        }
        int maxTLenght = tLenght[0];
        for(int i=1; i<pastTurns; i++){
            if(maxTLenght < tLenght[i]) maxTLenght = tLenght[i];
        }


        System.out.print("           "); //11 spaces
        for (char a = 'A'; a < 'A' + maxTLenght; a++) {
            System.out.print(" " + a + " ");
        }
        System.out.println();

        for (int i = 0; i < pastTurns; i++) {

            DiceFace[] dices = diceHolderImmutable.getDiceFaces(i);
            System.out.print("Turn " + (i + 1) + (i == 9 ? "" : " ") + " => ");


            for (int j = 0; j < dices.length; j++) {
                printDice(dices[j]);
                System.out.print("|");
            }
            System.out.println();

        }

        for (int i = pastTurns; i < Settings.TURNS; i++) {
            System.out.print("Turn " + (i + 1) + (i == 9 ? "" : " ") + " => ");
            System.out.print("|" + ansi().bg(Ansi.Color.WHITE).fg(Ansi.Color.BLACK).a("X").reset() + "|");
            System.out.println();
        }

    }

    public static Point decodePosition(String input, DiceHolderImmutable diceHolderImmutable) {
        int pastTurns = diceHolderImmutable.getDoneTurns();

        int[] tLenght = new int[pastTurns];
        for(int i=0; i<pastTurns; i++){
            tLenght[i]=diceHolderImmutable.getDiceFaces(i).length;
        }
        int maxTLenght = tLenght[0];
        for(int i=1; i<pastTurns; i++){
            if(maxTLenght < tLenght[i]) maxTLenght = tLenght[i];
        }

        if (input == null) throw new NullPointerException("Input should not be null!");
        if (input.length() != 2) return null;
        int x = input.toUpperCase().charAt(0) - 'A';

        int y;

        try {
            y = Integer.parseInt(input.substring(1)) - 1;
        }
        catch (NumberFormatException ex) {
            return null;
        }

        if (y < 0 || y >= pastTurns) return null;
        if (x < 0 || x >= diceHolderImmutable.getDiceFaces(y).length) return null;
        return new Point(x, y);

    }

    public static void printToolcard(ToolCardImmutable toolCardImmutable, int index) {

        System.out.println(ansi().bg(WHITE).fg(RED).a("Toolcard n: " + index).reset());

        String path = "gameData/resources/cli/toolcards/" + toolCardImmutable.getName() + ".json";

        File toolcardResources = new File(path);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(toolcardResources);
        } catch (FileNotFoundException e) {
            Log.d("Resource file linked to " + toolCardImmutable.getName() + " not found.");
            return;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        StringBuilder builder;
        try (Scanner scanner = new Scanner(bufferedInputStream)) {
            builder = new StringBuilder();

            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
        }

        JSONObject root = new JSONObject(builder.toString());

        System.out.println(ansi().fg(GREEN).a(root.getString("title")).reset());
        System.out.println(ansi().fg(BLUE).a("Tokens needed: " + toolCardImmutable.getToken()).reset());
        System.out.println(ansi().fg(BLUE).a(root.getString("description")).reset());

        try {
            System.out.println(ansi().fg(RED).a(root.getString("restriction")).reset());
        } catch (Exception ignored) {
        }

        // Yes, I want to print 2 newlines
        System.out.println("\n");

    }

    public static void printPublicObjectives(PublicObjective publicObjective, int index) {

        System.out.println(ansi().bg(WHITE).fg(RED).a("Public Objective n: " + index).reset());

        String path = "gameData/resources/cli/publicObjectives/" + publicObjective.getClass().getSimpleName() + ".json";

        File publicObjectiveResource = null;

        publicObjectiveResource = new File(path);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(publicObjectiveResource);
        } catch (FileNotFoundException e) {
            Log.d("Resource file linked to " + publicObjective.getClass().getSimpleName() + " not found.");
            return;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        StringBuilder builder;
        try (Scanner scanner = new Scanner(bufferedInputStream)) {
            builder = new StringBuilder();

            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
        }

        JSONObject root = new JSONObject(builder.toString());

        System.out.println(ansi().fg(Ansi.Color.RED).a(root.getString("title")).reset());
        System.out.println(ansi().fg(Ansi.Color.BLUE).a("Points: " + publicObjective.getPoint()).reset());
        System.out.println(ansi().fg(Ansi.Color.BLUE).a(root.getString("description")).reset());
        // Yes, I want to print 2 newlines
        System.out.println("\n");
    }

    public static void printSchema(Schema schema) {
        System.out.println("Name: " + schema.getSchemaCardFace().getName() + "\nDifficulty: " + schema.getSchemaCardFace().getDifficulty());
        System.out.print("  "); //two spaces
        for (char a = 'A'; a < 'A' + Settings.CARD_WIDTH; a++) {
            System.out.print(" " + a);
        }
        System.out.println();
        for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
            System.out.print("  ");
            printLineSeparator(Settings.CARD_WIDTH);
            System.out.print(y + " ");
            for (int x = 0; x < Settings.CARD_WIDTH; x++) {
                Point point = new Point(x, y);
                if (schema.getDiceFace(point) == null) {
                    printRestriction(schema.getSchemaCardFace().getRestriction(new Point(x, y)));
                } else {
                    printDice(schema.getDiceFace(point));
                }
            }
            System.out.println("|");
        }
        System.out.print("  ");
        printLineSeparator(Settings.CARD_WIDTH);

        System.out.println();

    }

    public static void printDice(DiceFace diceFace) {
        System.out.print("|" + ansi().bg(diceFace.getColor().getAnsiColor()).fg(Ansi.Color.BLACK).a("" + diceFace.getNumber()).reset());
    }

    public static void printTokens(int used, int total) {

        System.out.print(ansi().fg(BLUE).a("Tokens "));

        for (int i = 0; i <= (total - used); i++) {
            System.out.print(ansi().bg(WHITE).a("O").reset());
        }
        for (int i = 0; i <= used; i++) {
            System.out.print(ansi().bg(RED).a("X").reset());
        }
        System.out.println();

    }

    public static Point decodePosition(String input) {
        if (input == null) throw new NullPointerException("Input should not be null!");
        if (input.length() != 2) return null;
        int x = input.toUpperCase().charAt(0) - 'A';
        if (x < 0 || x >= Settings.CARD_WIDTH) return null;

        try {
            int y = Integer.parseInt(input.substring(1));
            if (y < 0 || y >= Settings.CARD_HEIGHT) return null;
            return new Point(x, y);
        } catch (NumberFormatException ex) {
            return null;
        }

    }

}
