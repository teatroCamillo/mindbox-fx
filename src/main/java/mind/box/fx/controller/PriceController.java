package mind.box.fx.controller;

import mind.box.fx.model.PriceDTO;
import mind.box.fx.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(final PriceService priceService){
        this.priceService = priceService;
    }

    @GetMapping("/getLastPrice")
    public ResponseEntity<PriceDTO> getLastPrice(){
        return new ResponseEntity<>(priceService.getLastPrice(), HttpStatus.OK);
    }
}
