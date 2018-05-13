package it.polimi.se2018.model.schema_card;

public class MalformedSchemaCardFaceException extends IllegalArgumentException {
    public MalformedSchemaCardFaceException() {
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
