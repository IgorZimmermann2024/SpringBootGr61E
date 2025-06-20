package de.ait.javalessons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
@Entity
public class Book {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @NotBlank(message = "Id не должно быть пустым")
    private  String id;

    @NotBlank(message = "Название не должно быть пустым")
    private String title;

    @NotBlank (message = "Автор книги должен быть обязательно")
    private String author;

    @NotBlank (message = "Год выпуска не может быть пустым")
    private int publishYear;

}
