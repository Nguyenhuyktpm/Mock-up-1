package org.example.factory.filehandilng;

import java.util.List;

public interface SalesManager {

    List<?> readFile(String folderPath);

   <T> void addToRepository(List<T> elements);

    <T> void writeFile(List<T> elements,String folderPath);


}
