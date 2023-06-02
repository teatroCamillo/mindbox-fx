package mind.box.fx.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CSVLoaderTest {

    private List<String> messages;

    @BeforeEach
    void setUp(){
        messages = new ArrayList<>();
        messages.add("106, EUR/USD, 1.1000, 1.2000, 01-06-2020 12:01:01:001");
        messages.add("107, EUR/JPY, 119.60, 119.90, 01-06-2020 12:01:02:002");
        messages.add("108, GBP/USD, 1.2500, 1.2560, 01-06-2020 12:01:02:002");
        messages.add("109, GBP/USD, 1.2499, 1.2561, 01-06-2020 12:01:02:100");
        messages.add("110, EUR/JPY, 119.61, 119.91, 01-06-2020 12:01:02:110");
    }

    @Test
    void Given_FilePath_When_LoadMessages_Then_LoadedDataEqualToPrepared() {
        final List<String> loaded = CSVLoader.loadMessages("src/main/resources/static/data.csv");
        for(int i = 0; i < messages.size(); i++) assertThat(loaded.get(i)).isEqualTo(messages.get(i));
    }
}