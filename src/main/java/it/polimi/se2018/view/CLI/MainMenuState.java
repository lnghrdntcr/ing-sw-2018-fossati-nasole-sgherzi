package it.polimi.se2018.view.CLI;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class MainMenuState extends State {

    private HashMap<String, Function<Void, State>>

    public MainMenuState(CLIGameTable gameTable) {
        super(gameTable);
    }

    @Override
    public State process(String input) {
        return null;
    }

    @Override
    public void render() {
        CLIPrinter.printMenuLine(1, "View Draft Board");
        CLIPrinter.printMenuLine(2, "View Players");
        CLIPrinter.printMenuLine(3, "View Round Track");
        CLIPrinter.printMenuLine(4, "View Toolcards and Public Objectives");
        CLIPrinter.printMenuLine(5, "View your Table");
        CLIPrinter.printMenuLine(6, "Place dice");
        CLIPrinter.printMenuLine(7, "Use Toolcard");
        CLIPrinter.printMenuLine(8, "End Turn");
    }
}
