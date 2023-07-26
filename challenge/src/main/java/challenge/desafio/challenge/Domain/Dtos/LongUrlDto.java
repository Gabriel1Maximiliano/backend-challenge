package challenge.desafio.challenge.Domain.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class LongUrlDto {


    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    @JsonProperty("long_url")
    private String url;

}
