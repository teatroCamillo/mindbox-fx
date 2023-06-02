package mind.box.fx.service;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mind.box.fx.exception.PriceNotFoundException;
import mind.box.fx.model.PriceDTO;
import mind.box.fx.util.CSVLoader;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

@Data
@Slf4j
@Service
public class PriceService implements Message {

    private final TreeMap<Long, PriceDTO> priceDTOMap = new TreeMap<>();
    private static final String TIMESTAMP_PATTERN = "dd-MM-yyyy HH:mm:ss:SSS";
    private final BigDecimal COMMISSION = new BigDecimal("0.001");

    /**
     * A messaging system.
     */
    @PostConstruct
    public void init() {
        final List<String> messages = CSVLoader.loadMessages("src/main/resources/static/data.csv");
//        onMessage(messages.get(0));
//        onMessage(messages.get(1));
//        onMessage(messages.get(2));
    }

    public PriceDTO getLastPrice(){
        if(priceDTOMap.size() == 0) throw new PriceNotFoundException();
        return priceDTOMap.lastEntry().getValue();
    }

    @Override
    public void onMessage(final String message) {
        final String[] parts = Arrays.stream(message.split(",")).map(String::trim).toArray(String[]::new);
        final Long key = Long.valueOf(parts[0]);
        final PriceDTO priceDTO = PriceDTO.builder()
                .id(key)
                .name(parts[1])
                .bid(includeCommission(new BigDecimal(parts[2]), false))
                .ask(includeCommission(new BigDecimal(parts[3]), true))
                .timestamp(convertStringToTimestamp(parts[4]))
                .build();
        priceDTOMap.put(key, priceDTO);
    }

    /**
     * Method includes a commission to value ask and bid.
     * @param price
     * @param askBid: true - ask, false - bid
     * @return new BigDecimal
     */
    private BigDecimal includeCommission(final BigDecimal price, final boolean askBid){
        final BigDecimal result = askBid ?
                price.add(price.multiply(COMMISSION)) :
                price.subtract(price.multiply(COMMISSION));
        return result.setScale(price.scale(), RoundingMode.HALF_UP);
    }

    public static Timestamp convertStringToTimestamp(final String timestamp)  {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
        final LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(timestamp));
        return Timestamp.valueOf(localDateTime);
    }
}
