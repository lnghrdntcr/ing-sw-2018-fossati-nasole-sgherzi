package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Scanner;

/**
 * Links two SchemaCardFace, one per side.
 */
public class SchemaCard implements Serializable {

    private EnumMap<Side, SchemaCardFace> schemaCardFaces = new EnumMap<>(Side.class);

    /**
     * @param front the SchemaCardFace to put on front
     * @param back  the SchemaCardFace to put on back
     */
    private SchemaCard(SchemaCardFace front, SchemaCardFace back) {
        schemaCardFaces.put(Side.FRONT, front);
        schemaCardFaces.put(Side.BACK, back);
    }

    /**
     * Load all the SchemaCard contained in the JSON file specified
     *
     * @param fileName the JSON file to load. See README.md for file specifications
     * @return a List containing all the SchemaCard defined in the JSON file
     * @throws FileNotFoundException if the file specified does not exist
     * @throws JSONException         if the file specified is badly formatted
     */
    public static List<SchemaCard> loadSchemaCardsFromJson(String fileName, boolean loadOnlyOtherSource) throws FileNotFoundException, JSONException {

        ArrayList<SchemaCard> list = new ArrayList<>();

        InputStream defaultFilePath = null;

        // I'm loading the default schema cards
        if (fileName.equals(Settings.getDefaultSchemaCardDatabase())) {
            defaultFilePath = (new PrivateObjective(GameColor.RED)).getClass().getClassLoader().getResourceAsStream(Settings.getDefaultSchemaCardDatabase());
        }

        InputStream jsonFile = null;

        if(!loadOnlyOtherSource){
            if (defaultFilePath == null) {
                jsonFile = new FileInputStream(new File(fileName));
            } else {
                jsonFile = defaultFilePath;
            }
        } else {
            jsonFile = new FileInputStream(new File(fileName));
        }

        BufferedInputStream bufferedInputStream = new BufferedInputStream(jsonFile);
        StringBuilder builder;
        try (Scanner scanner = new Scanner(bufferedInputStream)) {
            builder = new StringBuilder();

            //read all lines
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
        }

        //now we have the whole file loaded, let's parse the JSON
        JSONArray jsonArray = new JSONArray(builder.toString());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                list.add(loadSchemaCardFromJsonObj(obj));
            } catch (JSONException | IllegalArgumentException e) {
                //we can continue, probably the problem is only in this set of faces
                System.err.println(Schema.class.getCanonicalName() + ": cannot load invalid item at position " + i + " " + e);
            }
        }


        if (fileName.equals(Settings.getDefaultSchemaCardDatabase()) && !Settings.getSchemaCardDatabase().equals("")) {
            try {
                list.addAll(SchemaCard.loadSchemaCardsFromJson(Settings.getSchemaCardDatabase()));
            } catch (FileNotFoundException | JSONException e) {
                CLIPrinter.printError("Could not load custom schema card file.");
            }

        }

        return list;
    }

    /**
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws JSONException
     */
    public static List<SchemaCard> loadSchemaCardsFromJson(String fileName) throws FileNotFoundException, JSONException {

        return SchemaCard.loadSchemaCardsFromJson(fileName, false);

    }


    /**
     * Load a SchemaCard from JSON rapresentation
     *
     * @param obj a JSON object. See README.md for file specifications
     * @return a SchemaCard object initialize as the obj in input
     * @throws JSONException            if the obj is malformed
     * @throws IllegalArgumentException if the obj is malformed (invalid face)
     */
    public static SchemaCard loadSchemaCardFromJsonObj(JSONObject obj) throws JSONException, IllegalArgumentException {
        SchemaCardFace front = SchemaCardFace.loadFromJson(obj.getJSONObject("front"));
        SchemaCardFace back = SchemaCardFace.loadFromJson(obj.getJSONObject("back"));
        return new SchemaCard(front, back);
    }

    /**
     * Get a single face of this SchemaCard
     *
     * @param side the side to get (FRONT or BACK)
     * @return The front face or the back face of this card
     */
    public SchemaCardFace getFace(Side side) {
        return schemaCardFaces.get(side);
    }

    /**
     * Get the JSON represetnation of this object
     *
     * @return a {@link JSONObject} that represetns the SchermaCard
     */
    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("front", schemaCardFaces.get(Side.FRONT).toJsonObject());
        jsonObject.put("back", schemaCardFaces.get(Side.BACK).toJsonObject());
        return jsonObject;
    }
}







