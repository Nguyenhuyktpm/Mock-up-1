package org.example.factory.validation;

import java.io.IOException;

public interface ValidationManager<T> {
    boolean validate(T object);
}
