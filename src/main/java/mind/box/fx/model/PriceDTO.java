package mind.box.fx.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class PriceDTO {

    private Long id;
    private String name;
    private BigDecimal bid;
    private BigDecimal ask;
    private Timestamp timestamp;

}
