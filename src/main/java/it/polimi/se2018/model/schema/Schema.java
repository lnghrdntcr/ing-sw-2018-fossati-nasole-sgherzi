package it.polimi.se2018.model.schema;

import it.polimi.se2018.model.schema_card.SchemaCardFace;

import java.awt.*;

public class Schema {

    private DiceFace diceFaces[] = new DiceFace[20];
    private SchemaCardFace schemaCardFace;

    public Schema(SchemaCardFace schemaCardFace){

    }

    public DiceFace getDiceFace(Point point){
        return null;
    }

    public void setDiceFace(Point point, DiceFace diceFace){

    }

    public boolean isDiceAllowed(Point point, DiceFace diceFace){
        return false;
    }

    public SchemaCardFace getSchemaCardFace() {
        return schemaCardFace;
    }
}
