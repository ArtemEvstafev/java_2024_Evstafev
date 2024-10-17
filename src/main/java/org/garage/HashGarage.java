package org.garage;

import java.util.*;

public class HashGarage implements Garage {

    private final NavigableSet<Car>   carsByVelocity = new TreeSet<>(Comparator.comparing(Car::maxVelocity).reversed().thenComparing(Car::carId));
    private final NavigableSet<Car>   carsByPower    = new TreeSet<>(Comparator.comparing(Car::      power).thenComparing(Car::carId, Comparator.reverseOrder()));
    private final Map<Integer,   Car>   carsById = new HashMap<>();
    private final Map<Integer, Owner> ownersById = new HashMap<>();
    private final Map<String,  Set<Car>>   brands = new HashMap<>();
    private final Map<Integer, Set<Car>> property = new HashMap<>();

    public HashGarage(Map<Owner, List<Car>> garage) {
        if (garage != null) {
            for (Map.Entry<Owner, List<Car>> entry : garage.entrySet()) {
                Owner owner = entry.getKey();
                for (Car car : entry.getValue()) {
                    addNewCar(car, owner);
                }
            }
        }
    }

    @Override
    public Collection<Owner> allCarsUniqueOwners() {
        return ownersById.values();
    }

    @Override
    public Collection<Car> topThreeCarsByMaxVelocity() {
        if (carsByVelocity.size() < 4) {
            return carsByVelocity;
        }
        List<Car> top3 = new ArrayList<>();
        Iterator<Car> iterator = carsByVelocity.iterator();
        for (int i = 0; i < 3 && iterator.hasNext(); i++) {
            top3.add(iterator.next());
        }
        return top3;
    }

    @Override
    public Collection<Car> allCarsOfBrand(String brand) {
        return brands.getOrDefault(brand, Collections.emptySet());
    }

    @Override
    public Collection<Car> carsWithPowerMoreThan(int power) {
        return carsByPower.tailSet(new Car(0,
        "",
        "",
        0,
        power,
        0), false);
    }

    @Override
    public Collection<Car> allCarsOfOwner(Owner owner) {
       return property.getOrDefault(owner.ownerId(), Collections.emptySet());
    }

    @Override
    public int meanOwnersAgeOfCarBrand(String brand) {
        long res = 0;
        HashSet<Integer> people = new HashSet<>();
        for (Car car : allCarsOfBrand(brand)) {
            if (car.brand().equals(brand) && people.add(car.ownerId())) {
                res += ownersById.get(car.ownerId()).age();
            }
        }
        return !people.isEmpty() ? Math.round( (float) res / people.size()) : 0;
    }

    @Override
    public int meanCarNumberForEachOwner() {
        return !ownersById.isEmpty() ? Math.round( (float) carsById.size() / ownersById.size()) : 0;
    }

    @Override
    public Car removeCar(int carId) {
        Car car = carsById.remove(carId);
        if (car == null) {
            return null;
        }
          brands.get(car.  brand()).remove(car);
        property.get(car.ownerId()).remove(car);
        carsByVelocity.remove(car);
        carsByPower   .remove(car);
        if (allCarsOfOwner(ownersById.get(car.ownerId())).isEmpty()) {
            ownersById.remove(car.ownerId());
        }
        return car;
    }

    @Override
    public void addNewCar(Car car, Owner owner) {
        if (car == null || owner == null) {
            return;
        }
        ownersById.put(owner.ownerId(), owner);
          carsById.put(  car.  carId(),   car);
        carsByVelocity.add(car);
        carsByPower   .add(car);

        brands  .computeIfAbsent(car  .brand(),   k -> new HashSet<>()).add(car);
        property.computeIfAbsent(owner.ownerId(), k -> new HashSet<>()).add(car);
    }
}
