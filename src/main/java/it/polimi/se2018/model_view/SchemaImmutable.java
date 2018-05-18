package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;

/**
 * Schema to be used in View
 * @author Francesco Sgherzi
 * @since 18/05/2018
 */
public class SchemaImmutable {

  private DiceFace[][] diceFaces;
  private SchemaCardFace schemaCardFace;

  public SchemaImmutable(DiceFace[][] diceFaces, SchemaCardFace schemaCardFace){
    this.diceFaces = diceFaces;
    this.schemaCardFace = schemaCardFace;
  }

  public DiceFace[][] getDiceFaces() {
    return diceFaces;
  }

  public SchemaCardFace getSchemaCardFace() {
    return schemaCardFace;
  }
}
