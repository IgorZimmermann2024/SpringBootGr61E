package de.ait.javalessons.controller;

import de.ait.javalessons.model.Car;
import de.ait.javalessons.repository.CarRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/cars")
public class RestApiCarController {

    private final CarRepository carRepository;

    public RestApiCarController(CarRepository carRepository) {
        this.carRepository = carRepository;
        /**this.carRepository.saveAll(List.of(
                new Car("1", "Audi A4"),
                new Car("2", "BMW M5"),
                new Car("3", "Kia XCEED"),
                new Car("4", "Mazda 6"),
                new Car("5", "Mercedes Benz CLX"),
                new Car("6", "Skoda Octavia")
        ));*/
    }

    //@RequestMapping(value = "/cars", method = RequestMethod.GET)
    @GetMapping
    Iterable<Car> getCars() {
        log.info("Getting all cars");
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Car> getCarById(@PathVariable String id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            log.info("Car with id {} found", id);
            return ResponseEntity.status(HttpStatus.OK).body(car.get());
        }
        log.warn("Car with id {} not found", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<Car> postCar(@Valid @RequestBody Car car) {
        Car savedCar = carRepository.save(car);
        log.info("Car with id {} posted", car.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @PutMapping("/{id}")
    ResponseEntity<Car> putCar(@PathVariable String id, @RequestBody Car car) {
        if (carRepository.existsById(id)) {
            Car carInDatabase = carRepository.findById(id).get();
            car.setId(carInDatabase.getId());
            car.setName(carInDatabase.getName());
            Car savedCar = carRepository.save(car);
            log.info("Car with id {} updated", id);
            return new ResponseEntity<>(savedCar, HttpStatus.OK);
        }
        return postCar(car);
    }

    @DeleteMapping("/{id}")
    void deleteCar(@PathVariable String id) {
        carRepository.deleteById(id);
        log.info("Car with id {} deleted", id);
    }

}
