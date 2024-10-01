package garage;

import java.util.*;

public class HashGarage implements Garage {

    private final NavigableSet<Car>   carsByVelocity;
    private final NavigableSet<Car>   carsByPower;
    private final HashMap<Integer,   Car>     cars = new HashMap<>();
    private final HashMap<Integer, Owner>   owners = new HashMap<>();
    private final HashMap<String,  List<Car>>   brands = new HashMap<>();
    private final HashMap<Integer, List<Car>> property = new HashMap<>();

    public HashGarage(Map<Owner, List<Car>> garage) {

        Comparator<Car> comparatorVelocity = Comparator.comparing(Car::maxVelocity).thenComparing(Car::carId);
        Comparator<Car> comparatorPower    = Comparator.comparing(Car::      power).thenComparing(Car::carId);
        carsByVelocity = new TreeSet<>(comparatorVelocity);
        carsByPower    = new TreeSet<>(comparatorPower);

        for (Map.Entry<Owner, List<Car>> entry : garage.entrySet()) {

            Owner owner = entry.getKey();
            owners  .put(owner.ownerId(), owner);
            property.put(owner.ownerId(), new ArrayList<>());
            for (Car car : entry.getValue()) {
                carsByPower   .add(car);
                carsByVelocity.add(car);
                cars.put(car.carId(), car);
                property.get(owner.ownerId()).add(car);
                if (brands.containsKey(car.brand())) {
                    brands.get(car.brand()).add(car);
                }
                else{
                    brands.put(car.brand(), new ArrayList<>(List.of(car)));
                }
            }
        }
    }

    @Override
    public Collection<Owner> allCarsUniqueOwners() {
        return owners.values();
    }

    @Override
    public Collection<Car> topThreeCarsByMaxVelocity() {
        if (carsByVelocity.size() > 2) {
            List<Car> top3 = new ArrayList<>();
            Iterator<Car> iterator = carsByVelocity.descendingIterator();
            for (int i = 0; i < 3 && iterator.hasNext(); i++) {
                top3.add(iterator.next());
            }
            return top3;
        }
        return carsByVelocity.descendingSet();
    }

    @Override
    public Collection<Car> allCarsOfBrand(String brand) {
        if (brands.containsKey(brand)) {
            return brands.get(brand);
        }
        return List.of();
    }

    @Override
    public Collection<Car> carsWithPowerMoreThan(int power) {
        try{
            return carsByPower.tailSet(new Car(0,
                    "",
                    "",
                    0,
                    power,
                    0), false);
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Collection<Car> allCarsOfOwner(Owner owner) {
        if (property.containsKey(owner.ownerId())) {
            return property.get(owner.ownerId());
        }
        return List.of();
    }

    @Override
    public int meanOwnersAgeOfCarBrand(String brand) {
        float res = 0.f;
        HashSet<Owner> people = new HashSet<>();
        for (Car car : cars.values()) {
            if (car.brand().equals(brand) && people.add(owners.get(car.ownerId()))) {
                res += (float) owners.get(car.ownerId()).age();
            }
        }
        return !people.isEmpty() ? Math.round(res / people.size()) : 0;
    }

    @Override
    public int meanCarNumberForEachOwner() {
        float res = property.values().stream().mapToInt(List::size).sum();
        return res > 0 ? Math.round(res / property.values().size()) : 0;
    }

    @Override
    public Car removeCar(int carId) {
        if (cars.containsKey(carId)) {
            Car car = cars.get(carId);

              brands.get(car.  brand()).remove(car);
            property.get(car.ownerId()).remove(car);
            carsByVelocity.remove(car);
            carsByPower   .remove(car);

            if (allCarsOfOwner(owners.get(car.ownerId())).isEmpty()) {
                owners.remove(car.ownerId());
            }
        }
        return cars.remove(carId);
    }

    @Override
    public void addNewCar(Car car, Owner owner) {
        owners.put(owner.ownerId(), owner);
          cars.put(  car.  carId(),   car);
        carsByVelocity.add(car);
        carsByPower   .add(car);

        if (brands.containsKey(car.brand())) {
            brands.get(car.brand()).add(car);
        }
        else{
            brands.put(car.brand(), new ArrayList<>(List.of(car)));
        }
        if (property.containsKey(owner.ownerId())) {
            property.get(owner.ownerId()).add(car);
        }
        else{
            property.put(owner.ownerId(), new ArrayList<>(List.of(car)));
        }
    }
}
