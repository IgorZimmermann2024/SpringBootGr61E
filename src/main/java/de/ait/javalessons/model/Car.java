package de.ait.javalessons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Car {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @NotBlank(message = "Id не должно быть пустым")
    private String id;

    @NotBlank(message = "Название не должно быть пустым")
    private  String name;

}
