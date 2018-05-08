package it.polimi.se2018.model.schema_card;

public class SchemaCard {

  private SchemaCardFace[] schemaCardFaces = new SchemaCardFace[2];

  public SchemaCard (SchemaCardFace front, SchemaCardFace back){

  }

  public static SchemaCard loadFromJson(){
    // TO ADD jsonObject as a parameter
    return null;
  }

  public SchemaCardFace getFace(Side side){
    return null;
  }

}
