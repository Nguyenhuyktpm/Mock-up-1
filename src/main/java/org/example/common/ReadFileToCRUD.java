package org.example.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFileToCRUD {
    public List<String> readFile(String filePath) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line ;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                list.add(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
