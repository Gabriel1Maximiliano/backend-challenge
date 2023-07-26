package challenge.desafio.challenge.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private LocalDate dob;
    @Getter
    @Setter
    private Integer age;

    public User() {

    }

    public User(String name, String email, LocalDate dob, Integer age) {

        this.name = name;
        this.email = email;
        this.dob = dob;
        this.age = age;
    }


}
