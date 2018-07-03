package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.ScoreHolder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        // TODO: Add the logic to do other stuff eg. write things to file.
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

        if (getModel() == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null.");

        ArrayList<ScoreHolder> scoreHolders = getModel().computeAllScores();

        if (scoreHolders.isEmpty())
            throw new IllegalStateException(this.getClass().getCanonicalName() + ": There are no players on which compute the score.");
        if (scoreHolders.size() != 1) {
            Collections.sort(scoreHolders);
            Collections.reverse(scoreHolders);
        }


        scoreHolders.forEach(el -> {
            System.out.println(el);
        });

        File file = new File("leaderboad.json");

        try {

            file.createNewFile();


            URI uri = new URI("leaderboard.json");
            JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
            JSONObject root = new JSONObject(tokener);

            for (ScoreHolder sh : scoreHolders) {

                JSONObject player = null;

                try {
                    player = (JSONObject) root.getJSONObject(sh.getPlayerName());
                } catch (JSONException ignored) {
                    player = new JSONObject();
                }

                if (sh.equals(scoreHolders.get(0))) {
                    player.put("victories", player.getInt("victories") + 1);
                } else {
                    player.put("losses", player.getInt("losses") + 1);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        this.getController().dispatchEvent(new EndGameEvent(this.getClass().getName(), "", "", scoreHolders));

    }

}
