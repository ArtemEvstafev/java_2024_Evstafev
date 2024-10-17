package org.garage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HashGarageTest {

    @Test
    void allCarsUniqueOwners() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Owner> owners = garage.allCarsUniqueOwners();

        assertEquals(12, owners.size());
        assertTrue(owners.contains(new Owner( 1, "John", "Doe", 45)));
        assertTrue(owners.contains(new Owner(11, "John", "Doe", 35)));
    }

    @Test
    void topThreeCarsByMaxVelocity() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> cars = garage.topThreeCarsByMaxVelocity();

        assertEquals(3, cars.size());
        assertTrue(cars.contains(new Car(4, "Tesla", "Model S", 300, 670, 3)));
        assertTrue(cars.contains(new Car(2, "Chevrolet", "Corvette", 290, 500, 1)));
        assertTrue(cars.contains(new Car(8, "Chevrolet", "Corvette", 290, 500, 7)));
    }

    @Test
    void topThreeCarsByMaxVelocityLess3() {
        Map<Owner, List<Car>> map = new HashMap<>();

        map.put(new Owner(5, "Robert", "Brown", 50), List.of(
                new Car(6, "BMW", "X5", 240, 340, 5)));

        map.put(new Owner(6, "Linda", "Williams", 29), List.of(
                new Car(7, "Audi", "A4", 230, 220, 6)));

        Garage garage = new HashGarage(map);

        Collection<Car> cars = garage.topThreeCarsByMaxVelocity();

        assertEquals(2, cars.size());
    }


    @Test
    void allCarsOfBrand() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> audiCars = garage.allCarsOfBrand("Audi");

        assertEquals(3, audiCars.size());
        assertTrue(audiCars.contains(new Car(7,  "Audi", "A4", 230, 220, 6)));
        assertTrue(audiCars.contains(new Car(13, "Audi", "A4", 230, 220, 12)));
        assertTrue(audiCars.contains(new Car(14, "Audi", "A4", 230, 220, 12)));
    }

    @Test
    void carsWithPowerMoreThan() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> cars = garage.carsWithPowerMoreThan(340);
        System.out.println(cars);
        assertEquals(4, cars.size());
    }

    @Test
    void carsWithPowerMoreThanEmpty() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> cars = garage.carsWithPowerMoreThan(1000);

        assertTrue(cars.isEmpty());
    }

    @Test
    void carsWithPowerMoreThanFull() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> cars = garage.carsWithPowerMoreThan(-1);

        assertEquals(14, cars.size());
    }

    @Test
    void allCarsOfOwner1() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> carpark = garage.allCarsOfOwner(new Owner(1, "John", "Doe", 45));

        assertEquals(2, carpark.size());
        assertTrue(carpark.contains(new Car(1, "Ford",       "Mustang", 250, 450, 1)));
        assertTrue(carpark.contains(new Car(2, "Chevrolet", "Corvette", 290, 500, 1)));
    }

    @Test
    void allCarsOfOwner2() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Car> carpark = garage.allCarsOfOwner(new Owner(12, "Patricia", "Martinez", 27));

        assertEquals(2, carpark.size());
        assertTrue(carpark.contains(new Car(13, "Audi", "A4", 230, 220, 12)));
        assertTrue(carpark.contains(new Car(14, "Audi", "A4", 230, 220, 12)));
    }
    @Test
    void meanOwnersAgeOfCarBrand() {
        Garage garage = new HashGarage(initTestMap());

        assertEquals((29 + 27) / 2, garage.meanOwnersAgeOfCarBrand("Audi"));
    }

    @Test
    void meanCarNumberForEachOwner() {
        Garage garage = new HashGarage(initTestMap());

        assertEquals(1, garage.meanCarNumberForEachOwner());
    }

    @Test
    void removeCar() {
        Garage garage = new HashGarage(initTestMap());

        garage.removeCar(1);
        Owner JohnDoe = new Owner(1, "John", "Doe", 45);
        assertTrue(garage.allCarsUniqueOwners().contains(JohnDoe));
        assertFalse(garage.allCarsOfOwner(JohnDoe).isEmpty());
        garage.removeCar(2);
        assertFalse(garage.allCarsUniqueOwners().contains(JohnDoe));
        garage.removeCar(3);
        assertFalse(garage.allCarsUniqueOwners().contains(new Owner(2, "Jane", "Smith", 32)));
    }

    @Test
    void addNewCar() {
        Garage garage = new HashGarage(initTestMap());

        Owner RyanGosling = new Owner(13, "Ryan", "Gosling", 43);
        Car car = new Car(15, "Chevrolet", "Chevelle Malibu", 221, 225, 13);

//        garage.soutGarage();
        garage.addNewCar(car, RyanGosling);
//        garage.soutGarage();

        assertTrue(garage.allCarsUniqueOwners().contains(RyanGosling));
        assertTrue(garage.allCarsOfOwner(RyanGosling).contains(car));
    }

    private Map<Owner, List<Car>> initTestMap() {
        Map<Owner, List<Car>> map = new HashMap<>();

        map.put(new Owner(1, "John", "Doe", 45), List.of(
                new Car(1, "Ford", "Mustang", 250, 450, 1),
                new Car(2, "Chevrolet", "Corvette", 290, 500, 1)));

        map.put(new Owner(2, "Jane", "Smith", 32), List.of(
                new Car(3, "Toyota", "Camry", 220, 200, 2)));

        map.put(new Owner(3, "Michael", "Johnson", 28), List.of(
                new Car(4, "Tesla", "Model S", 300, 670, 3)));

        map.put(new Owner(4, "Emily", "Davis", 22), List.of(
                new Car(5, "Honda", "Civic", 190, 150, 4)));

        map.put(new Owner(5, "Robert", "Brown", 50), List.of(
                new Car(6, "BMW", "X5", 240, 340, 5)));

        map.put(new Owner(6, "Linda", "Williams", 29), List.of(
                new Car(7, "Audi", "A4", 230, 220, 6)));

        map.put(new Owner(7, "David", "Miller", 36), List.of(
                new Car(8, "Chevrolet", "Corvette", 290, 500, 7)));

        map.put(new Owner(8, "Susan", "Taylor", 40), List.of(
                new Car(9, "Mercedes", "C-Class", 250, 300, 8)));

        map.put(new Owner(9, "James", "Anderson", 31), List.of(
                new Car(10, "Nissan", "Altima", 210, 180, 9)));

        map.put(new Owner(10, "John", "Smith", 45), List.of(
                new Car(11, "Toyota", "Corolla", 180, 140, 10)));

        map.put(new Owner(11, "John", "Doe", 35), List.of(
                new Car(12, "Ford", "Focus", 200, 160, 11)));

        map.put(new Owner(12, "Patricia", "Martinez", 27), List.of(
                new Car(13, "Audi", "A4", 230, 220, 12),
                new Car(14, "Audi", "A4", 230, 220, 12)));

        return map;
    }
}