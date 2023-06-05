package mind.box.fx.controller;

import mind.box.fx.exception.PriceNotFoundException;
import mind.box.fx.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PriceNotFoundExceptionController {

    @ExceptionHandler(value = PriceNotFoundException.class)
    public ResponseEntity<Object> priceNotFoundException(PriceNotFoundException e){
        ErrorMessage errorMessage = new ErrorMessage(404, e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
