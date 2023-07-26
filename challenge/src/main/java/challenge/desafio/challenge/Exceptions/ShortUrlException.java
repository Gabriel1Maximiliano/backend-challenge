package challenge.desafio.challenge.Exceptions;

import lombok.Data;


@Data
public class ShortUrlException extends Exception {

    private String description;


    public ShortUrlException(String description, String message, Throwable casue) {
        super(message, casue);
        this.description = description;
    }

    public ShortUrlException(String message) {
        super(message);

    }
}