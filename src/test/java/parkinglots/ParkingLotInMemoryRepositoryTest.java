package parkinglots;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import parkingspots.*;
import repositories.ParkingSpotInMemoryRepository;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;
import vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static parkingspots.ParkingSpotType.*;

class ParkingLotInMemoryRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithoutElectricChargerWhenNonElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", false);
        smallParkingSpot.setVehicle(motorcycle);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithElectricChargerWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(true);
        Vehicle motorcycle = new Motorcycle("", true);
        smallParkingSpot.setVehicle(motorcycle);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleByPlateNumberShouldReturnMediumParkingSpotWithElectricChargerWhenElectricCarIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(true);
        Vehicle car = new Car("", true);
        mediumParkingSpot.setVehicle(car);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(mediumParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(car.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(car, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleByPlateNumberShouldReturnMediumParkingSpotWithoutElectricChargerWhenNonElectricCarIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(false);
        Vehicle car = new Car("", false);
        mediumParkingSpot.setVehicle(car);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(mediumParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(car.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(car, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleByPlateNumberShouldReturnLargeParkingSpotWithElectricChargerWhenElectricTruckIsSetOnThatParkingSpot(){
        ParkingSpot largeParkingSpot = new LargeParkingSpot(true);
        Vehicle truck = new Car("", true);
        largeParkingSpot.setVehicle(truck);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(largeParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(truck.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(truck, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleByPlateNumberShouldReturnLargeParkingSpotWithoutElectricChargerWhenNonElectricTruckIsSetOnThatParkingSpot(){
        ParkingSpot largeParkingSpot = new LargeParkingSpot(true);
        Vehicle truck = new Car("", true);
        largeParkingSpot.setVehicle(truck);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(largeParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(truck.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(truck, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithoutElectricChargerWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", true);
        smallParkingSpot.setVehicle(motorcycle);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @ParameterizedTest
    @MethodSource("vehicleGenerator")
    void findVehicleByPlateNumberShouldReturnAnEmptyOptionalForVehicleWhenItIsNotSetOnAParkingSpot(Vehicle vehicle){
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        parkingSpotList.add(new SmallParkingSpot(false));
        parkingSpotList.add(new SmallParkingSpot(true));
        parkingSpotList.add(new MediumParkingSpot(false));
        parkingSpotList.add(new MediumParkingSpot(true));
        parkingSpotList.add(new LargeParkingSpot(false));
        parkingSpotList.add(new LargeParkingSpot(true));

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(parkingSpotList);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(vehicle.getPlateNumber());

        assertTrue(parkingSpotOptional.isEmpty());
    }

    private static Stream<Arguments> vehicleGenerator(){
        return Stream.of(
                Arguments.of(new Motorcycle("", false)),
                Arguments.of(new Motorcycle("", true)),
                Arguments.of(new Car("", false)),
                Arguments.of(new Car("", true)),
                Arguments.of(new Truck("", false)),
                Arguments.of(new Truck("", true)));
    }

    @Test
    void findVehicleByPlateNumberShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", true);
        mediumParkingSpot.setVehicle(motorcycle);

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(List.of(mediumParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @ParameterizedTest
    @EnumSource(ParkingSpotType.class)
    void getEmptyParkingSpotOfTypeAnyShouldReturnEmptyOptionalIfThereIsNoEmptyParkingSpotOfTheSpecifiedType(ParkingSpotType parkingSpotType){
        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(new ArrayList<>());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);
        assertTrue(parkingSpotOptional.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(ParkingSpotType.class)
    void getEmptyParkingSpotWithoutElectricChargerOfTypeAnyShouldReturnOptionalOfParkingSpotOfSpecifiedTypeWhenThereIsAtLeastOneEmptyParkingSpotWithoutElectricChargerOfTheSpecifiedType(ParkingSpotType parkingSpotType){
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        parkingSpotList.add(new SmallParkingSpot(false));
        parkingSpotList.add(new MediumParkingSpot(false));
        parkingSpotList.add(new LargeParkingSpot(false));

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(parkingSpotList);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpotType);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(parkingSpotType, parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
    }

    @ParameterizedTest
    @EnumSource(ParkingSpotType.class)
    void getEmptyParkingSpotWithElectricChargerOfTypeAnyShouldReturnOptionalOfParkingSpotOfSpecifiedTypeWhenThereIsAtLeastOneEmptyParkingSpotWithElectricChargerOfTheSpecifiedType(ParkingSpotType parkingSpotType){
        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        parkingSpotList.add(new SmallParkingSpot(true));
        parkingSpotList.add(new MediumParkingSpot(true));
        parkingSpotList.add(new LargeParkingSpot(true));

        ParkingSpotInMemoryRepository parkingLotInMemoryRepository = new ParkingSpotInMemoryRepository(parkingSpotList);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithElectricChargerOfType(parkingSpotType);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(parkingSpotType, parkingSpotOptional.get().getParkingSpotType());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
    }
}