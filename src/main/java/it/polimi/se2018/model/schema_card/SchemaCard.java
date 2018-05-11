package it.polimi.se2018.model.schema_card;

import java.util.EnumMap;

public class SchemaCard {

    private EnumMap<Side, SchemaCardFace> schemaCardFaces = new EnumMap<>(Side.class);

    public SchemaCard(SchemaCardFace front, SchemaCardFace back) {
        if (front==null || back==null){
            throw new IllegalArgumentException(getClass().getCanonicalName()+": front and back cannot be null");
        }
        schemaCardFaces.put(Side.FRONT, front);
        schemaCardFaces.put(Side.BACK, back);
    }

    public static SchemaCard loadFromJson() {
        //TODO: implement the method
        return null;
    }

    public SchemaCardFace getFace(Side side) {
        return schemaCardFaces.get(side);
    }

}
