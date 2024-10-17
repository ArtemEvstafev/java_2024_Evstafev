import org.garage.Car;
import org.garage.Garage;
import org.garage.HashGarage;
import org.garage.Owner;

import java.util.*;

public class Main {
    public static void main(String[] args) {
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

        map.forEach((owner, cars) -> {
            System.out.println("Owner: " + owner.name() + " " + owner.lastName() + ", Age: " + owner.age());
            cars.forEach(car -> {
                System.out.println("    Car: " + car.brand() + " " + car.modelName() + " (Car ID: " + car.carId() + ")");
            });
        });

        Garage garage = new HashGarage(map);
    }
}