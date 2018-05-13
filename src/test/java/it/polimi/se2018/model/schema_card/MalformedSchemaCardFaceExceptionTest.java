package it.polimi.se2018.model.schema_card;

import org.junit.Test;

import static org.junit.Assert.*;

public class MalformedSchemaCardFaceExceptionTest {

    final static String message = "This is a test message!11!!!oneone";

    @Test
    public void constructorTests(){
        try {
            throw new MalformedSchemaCardFaceException();
        } catch (MalformedSchemaCardFaceException e) {
            assertNull(e.getMessage());
            assertNull(e.getCause());
        }

        try {
            throw new MalformedSchemaCardFaceException(message);
        } catch (MalformedSchemaCardFaceException e) {
            assertEquals(message, e.getMessage());
            assertNull(e.getCause());
        }

        try {
            throw new MalformedSchemaCardFaceException(new IllegalArgumentException());
        } catch (MalformedSchemaCardFaceException e) {
            //assertNull(e.getMessage());
            assertTrue(e.getCause() instanceof  IllegalArgumentException);
        }

        try {
            throw new MalformedSchemaCardFaceException(message, new IllegalArgumentException());
        } catch (MalformedSchemaCardFaceException e) {
            assertEquals(message, e.getMessage());
            assertTrue(e.getCause() instanceof  IllegalArgumentException);
        }

    }

}