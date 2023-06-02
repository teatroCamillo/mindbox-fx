package mind.box.fx.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVLoader {

    public static List<String> loadMessages(String path) {

        List<String> messages = new ArrayList<>();
        try (
                var fr = new FileReader(path, StandardCharsets.UTF_8);
                var reader = new CSVReader(fr)
        ) {
            String[] nextLineArr;

            while ((nextLineArr = reader.readNext()) != null) {
                    String nextLine = Arrays.toString(nextLineArr);
                    messages.add(nextLine.substring(1, nextLine.length()-1));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }
}
