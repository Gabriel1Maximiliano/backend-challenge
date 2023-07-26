package challenge.desafio.challenge.Domain.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "url_metrics")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter @Setter
    @Column(name = "long_url")
    private String long_url;

    @Getter @Setter
    @Column(name = "short_url")
    private String short_url;

    @Getter @Setter
    @Column(name = "date_of_create")
    private Date dateOfCreate;

    @Getter @Setter
    @Column(name = "is_active")
    private int is_active;

    @Getter @Setter
    @Column(name = "amount_of_clicks")
    private int amount_of_clicks;


    @Getter @Setter
    @Column(name = "deleted_of_time")
    private Date deleted_of_time;

    @Getter @Setter
    @Column(name = "retrieved_time")
    private long retrieved_time;

    @Getter @Setter
    @Column(name = "was_created")
    private int was_created;

    @Getter @Setter
    @Column(name = "creation_time")
    private Long creation_time;


}