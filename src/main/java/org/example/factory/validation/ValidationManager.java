package org.example.factory.validation;

import java.io.IOException;

public interface ValidationManager<T> {
    <T2>  boolean isElementInFile(T2 element, String filePath) throws IOException;



    boolean validate(T object);
}
