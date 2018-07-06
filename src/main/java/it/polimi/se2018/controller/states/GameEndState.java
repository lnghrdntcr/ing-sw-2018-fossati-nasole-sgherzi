package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.ScoreHolder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The state that handles the end of the game, computing the score and declaring the winner
 *
 * @since 29/05/2018
 */
public class GameEndState extends State {

    public GameEndState(Controller controller, GameTableMultiplayer model) {
        super(controller, model);
        controller.setGameEnded(true);
        this.computeScore();
        // TODO: Figure out a slightly better idea to end the game.
    }

    @Override
    public void syncPlayer(String playerName) {

    }

    /**
     * The event handler for the EndGameEvent triggered.
     *
     * @throws IllegalArgumentException If the model is null
     * @throws IllegalStateException    If there are no players on which compute the score.
     */
    private void computeScore() {

        JSONObject root = null;

        ArrayList<ScoreHolder> scoreHolders = getModel().computeAllScores();

        if (scoreHolders.size() != 1) {
            Collections.sort(scoreHolders);
            Collections.reverse(scoreHolders);
        }


        File file = new File("leaderboad.json");

        try {

            if (file.createNewFile()) {
                try (OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file))) {
                    os.write("{}");
                    os.flush();
                }
            }


            InputStream is = new FileInputStream(file);

            JSONTokener tokener = new JSONTokener(is);
            root = new JSONObject(tokener);

            for (ScoreHolder sh : scoreHolders) {

                JSONObject player = null;

                try {
                    player = root.getJSONObject(sh.getPlayerName());
                } catch (JSONException ignored) {
                    player = new JSONObject();
                }

                if (sh.equals(scoreHolders.get(0))) {
                    player.put("victories", player.optInt("victories", 0) + 1);
                } else {
                    player.put("losses", player.optInt("losses", 0) + 1);
                }

                player.put("totalTimePlayed", player.optInt("totalTimePlayed", 0) + (System.currentTimeMillis() / 1000) - getController().getMatchBeginTime());

                root.put(sh.getPlayerName(), player);

            }

            try (OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file))) {
                os.write(root.toString());
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getController().dispatchEvent(new EndGameEvent(this.getClass().getName(), "", "", scoreHolders, root.toString()));

    }

}
