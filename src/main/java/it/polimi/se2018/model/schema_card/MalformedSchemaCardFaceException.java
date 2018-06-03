package it.polimi.se2018.model.schema_card;

/**
 * An exception that is thrown when a SchemaCardFace is malformed
 */
public class MalformedSchemaCardFaceException extends IllegalArgumentException {
    public MalformedSchemaCardFaceException() {
        super();
    }

    public MalformedSchemaCardFaceException(String s) {
        super(s);
    }

    public MalformedSchemaCardFaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedSchemaCardFaceException(Throwable cause) {
        super(cause);
    }
}
