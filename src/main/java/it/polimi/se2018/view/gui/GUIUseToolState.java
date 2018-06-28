package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.AbstractUseToolState;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIUseToolState extends AbstractUseToolState implements EventHandler<MouseEvent> {

    public GUIUseToolState(GameTable gameTable) {
        super(gameTable);
    }

    @FXML
    private VBox root;

    @FXML
    private HBox toolBox;

    @FXML
    private ToolCard[] toolCards = new ToolCard[Settings.TOOLCARDS_N];

    @Override
    public void process(String input) {

    }

    @Override
    public void unrealize() {

        if (root != null) {
            Platform.runLater(() -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
                root = null;
            });
        }

    }

    @Override
    public void render() {
        if (root == null) {
            buildInterface();
        }

        Platform.runLater(() ->{
            for(int i=0; i<Settings.TOOLCARDS_N; i++){
                toolCards[i].setToolCard(getGameTable().getToolCardImmutable(i));
            }
        });
    }

    private void buildInterface() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/ChooseToolcard.fxml"));

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Platform.runLater(() -> {

            Stage secondStage = new Stage();
            secondStage.setResizable(false);
            Scene scene = new Scene(root);
            secondStage.setScene(scene);

            secondStage.setOnCloseRequest((windowEvent) -> processCancel());
            secondStage.show();


            toolBox = (HBox) scene.lookup("#toolBox") ;

            for(int i=0; i<Settings.TOOLCARDS_N; i++){
                toolCards[i] = new ToolCard();
                toolBox.getChildren().add(toolCards[i]);
                toolCards[i].addEventHandler(MouseEvent.MOUSE_CLICKED, this);
            }
        });
    }

    @Override
    public void handle(MouseEvent event) {
        int index=-1;
        for(int i=0; i<Settings.TOOLCARDS_N; i++){
            if(event.getSource()==toolCards[i]){
                index = i;
            }
        }

        try {
            processUseToolCard(index);
        }catch (InputError ie){
            GUIUtils.showError(ie.getMessage());
        }
    }
}
