package garage;

import java.util.*;

public class HashGarage implements Garage {

    private final NavigableSet<Car>   carsByVelocity;
    private final NavigableSet<Car>   carsByPower;
    private final HashMap<Integer,   Car>     cars = new HashMap<>();
    private final HashMap<Integer, Owner>   owners = new HashMap<>();
    private final HashMap<String,  List<Car>>   brands = new HashMap<>();
    private final HashMap<Integer, List<Car>> property = new HashMap<>();
//    private Map<Owner, List<Car>> garage;

    public HashGarage(Map<Owner, List<Car>> garage) {

        Comparator<Car> comparatorVelocity = Comparator.comparing(Car::maxVelocity);
        Comparator<Car> comparatorPower    = Comparator.comparing(Car::power);
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

        return List.of();
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
        return List.of();
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
        return 0;
    }

    @Override
    public int meanCarNumberForEachOwner() {
        return 0;
    }

    @Override
    public Car removeCar(int carId) {
        if (cars.containsKey(carId)) {
              brands.get(cars.get(carId).brand()).remove(carId);
        }
        if (cars.containsKey(carId)) {
            property.get(cars.get(carId).ownerId()).remove(carId);
        }
        return cars.remove(carId);
    }

    @Override
    public void addNewCar(Car car, Owner owner) {
        owners.put(owner.ownerId(), owner);
          cars.put(  car.  carId(),   car);

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

    public HashMap<Integer, Owner> getOwners() {
        return owners;
    }

    public HashMap<Integer, Car> getCars() {
        return cars;
    }

    public void soutOwners(){
        owners.forEach((id, owner) -> {
            System.out.println("Owner ID: " + owner.ownerId() + ", Owner: " + owner.name() + " " + owner.lastName() + ", Age: " + owner.age());
        });
    }

    public void soutGarage(){
        soutOwners();
        soutCars();
    }

    public void soutCars(){
        cars.forEach((id, car) -> {
            System.out.println("Car ID: " + car.carId() + ", Car: " + car.brand() + " " + car.modelName());
        });
    }

    public void soutBrands(){
//        System.out.println(brands);
        brands.forEach((brand, ids) -> {
            System.out.print(brand + " ");
            ids.forEach(id -> {
                System.out.print(id + " ");
            });
            System.out.println('\n');
        });
    }
}
