package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Log;

import java.io.IOException;

public class PrintSchema {
    public static void main(String args[]){
       SchemaCardFace cardFace = null;
        try {
            cardFace= SchemaCard.loadSchemaCardsFromJson("./gameData/resources/schemaCards/schemaCardBase.scf").get(0).getFace(Side.FRONT);
        }catch (IOException e){
            Log.e("Praise our lord and savior: Fossa");
        }

        CLIPrinter.printSchema(new Schema(cardFace));
    }
}
