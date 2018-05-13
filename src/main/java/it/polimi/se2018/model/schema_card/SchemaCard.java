package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.Schema;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Scanner;

public class SchemaCard {

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
    public static List<SchemaCard> loadSchemaCardsFromJson(String fileName) throws FileNotFoundException, JSONException {

        ArrayList<SchemaCard> list = new ArrayList<>();

        File jsonFile = new File(fileName);
        if (!jsonFile.exists())
            throw new FileNotFoundException(SchemaCard.class.getCanonicalName() + ": file does not exist -> " + fileName);

        //let's load the whole string in memory

        //open file and set up reader
        FileInputStream fileInputStream = new FileInputStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
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

        return list;
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

    public SchemaCardFace getFace(Side side) {
        return schemaCardFaces.get(side);
    }

}







