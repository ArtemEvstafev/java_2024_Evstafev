package garage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HashGarageTest {

    private Map<Owner, List<Car>> initTestMap() {
        Map<Owner, List<Car>> map = new HashMap<>();

        map.put(new Owner(1, "John", "Doe", 45), Arrays.asList(
                new Car(1, "Ford", "Mustang", 250, 450, 1),
                new Car(2, "Chevrolet", "Corvette", 290, 500, 1)));

        map.put(new Owner(2, "Jane", "Smith", 32), Arrays.asList(
                new Car(3, "Toyota", "Camry", 220, 200, 2)));

        map.put(new Owner(3, "Michael", "Johnson", 28), Arrays.asList(
                new Car(4, "Tesla", "Model S", 300, 670, 3)));

        map.put(new Owner(4, "Emily", "Davis", 22), Arrays.asList(
                new Car(5, "Honda", "Civic", 190, 150, 4)));

        map.put(new Owner(5, "Robert", "Brown", 50), Arrays.asList(
                new Car(6, "BMW", "X5", 240, 340, 5)));

        map.put(new Owner(6, "Linda", "Williams", 29), Arrays.asList(
                new Car(7, "Audi", "A4", 230, 220, 6)));

        map.put(new Owner(7, "David", "Miller", 36), Arrays.asList(
                new Car(8, "Chevrolet", "Corvette", 290, 500, 7)));

        map.put(new Owner(8, "Susan", "Taylor", 40), Arrays.asList(
                new Car(9, "Mercedes", "C-Class", 250, 300, 8)));

        map.put(new Owner(9, "James", "Anderson", 31), Arrays.asList(
                new Car(10, "Nissan", "Altima", 210, 180, 9)));

        map.put(new Owner(10, "John", "Smith", 45), Arrays.asList(
                new Car(11, "Toyota", "Corolla", 180, 140, 10)));

        map.put(new Owner(11, "John", "Doe", 35), Arrays.asList(
                new Car(12, "Ford", "Focus", 200, 160, 11)));

        map.put(new Owner(12, "Patricia", "Martinez", 27), Arrays.asList(
                new Car(13, "Audi", "A4", 230, 220, 12),
                new Car(14, "Audi", "A4", 230, 220, 12)));

        return map;
    }

    @BeforeEach
    void setUp() {
        Garage garage = new HashGarage(initTestMap());
    }

    @Test
    void allCarsUniqueOwners() {
        Garage garage = new HashGarage(initTestMap());

        Collection<Owner> owners = garage.allCarsUniqueOwners();

//        owners.forEach(owner -> {
//            System.out.println("Owner: " + owner.name() + " " + owner.lastName() + ", Age: " + owner.age() + " (Owner ID: " + owner.ownerId() + ")");
//        });

        assertEquals(12, owners.size());
        assertTrue(owners.contains(new Owner(1, "John", "Doe", 45)));
        assertTrue(owners.contains(new Owner(11, "John", "Doe", 35)));
    }

    @Test
    void topThreeCarsByMaxVelocity() {
    }

    @Test
    void allCarsOfBrand() {
        HashGarage garage = new HashGarage(initTestMap());

        Collection<Car> audiCars = garage.allCarsOfBrand("Audi");

        assertEquals(3, audiCars.size());
        assertTrue(audiCars.contains(new Car(7, "Audi", "A4", 230, 220, 6)));
        assertTrue(audiCars.contains(new Car(13, "Audi", "A4", 230, 220, 12)));
        assertTrue(audiCars.contains(new Car(14, "Audi", "A4", 230, 220, 12)));
    }

    @Test
    void carsWithPowerMoreThan() {
    }

    @Test
    void allCarsOfOwner1() {
        HashGarage garage = new HashGarage(initTestMap());

        Collection<Car> carpark = garage.allCarsOfOwner(new Owner(1, "John", "Doe", 45));

        assertEquals(2, carpark.size());
        assertTrue(carpark.contains(new Car(1, "Ford", "Mustang", 250, 450, 1)));
        assertTrue(carpark.contains(new Car(2, "Chevrolet", "Corvette", 290, 500, 1)));
    }
    @Test
    void allCarsOfOwner2() {
        HashGarage garage = new HashGarage(initTestMap());

        Collection<Car> carpark = garage.allCarsOfOwner(new Owner(12, "Patricia", "Martinez", 27));

        assertEquals(2, carpark.size());
        assertTrue(carpark.contains(new Car(13, "Audi", "A4", 230, 220, 12)));
        assertTrue(carpark.contains(new Car(14, "Audi", "A4", 230, 220, 12)));
    }

    @Test
    void meanOwnersAgeOfCarBrand() {
    }

    @Test
    void meanCarNumberForEachOwner() {

    }

    @Test
    void removeCar() {
        HashGarage garage = new HashGarage(initTestMap());

        garage.soutGarage();
        garage.removeCar(1);
        garage.removeCar(2);
        garage.removeCar(3);
        garage.soutGarage();
    }

    @Test
    void addNewCar() {
        HashGarage garage = new HashGarage(initTestMap());

        Owner RyanGosling = new Owner(13, "Ryan", "Gosling", 43);
        Car car = new Car(15, "Chevrolet", "Chevelle Malibu", 221, 225, 13);

//        garage.soutGarage();
        garage.addNewCar(car, RyanGosling);
//        garage.soutGarage();

        assertTrue(garage.getOwners().containsValue(RyanGosling));
        assertTrue(garage.getCars  ().containsValue(car));
    }
}